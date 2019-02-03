package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionCollection;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;

public class GenericExpressionCollectionParser extends AbstractParser<ExpressionCollection>
{
	protected GenericExpressionCollectionParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_COMMA
	}

	@Override
	public ExpressionCollection parse(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		ExpressionCollection expressionList=new ExpressionCollection();
		State state=State.INITIAL;
		do
		{
			switch (state)
			{
				case INITIAL:
					SqlToken token=tokenizer.nextOptionalUsefulToken();
					if (token != null)
					{
						if (token.getType() == SqlToken.Type.PARENTHESIS_END)
						{
							tokenizer.pushBack(token);
							state=State.COMPLETE;
						}
						else
						{
							tokenizer.pushBack(token);
							Expression expression=getParserFactory().getExpressionParser().parse(tokenizer);
							if (expression != null)
							{
								expressionList.add(expression);
							}
						}
					}
					state=State.EXPECTING_COMMA;
					break;
				case EXPECTING_COMMA:
					SqlToken token2=tokenizer.nextOptionalUsefulToken();
					if (token2 != null)
					{
						switch (token2.getType())
						{
							case COMMA:
								state=State.INITIAL;
								break;
							default:
								tokenizer.pushBack(token2);
								state=State.COMPLETE;
						}
						break;
					}
					else
					{
						tokenizer.pushBack(token2);
						state=State.COMPLETE;
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		while (tokenizer.tokenWasRead() && state != State.COMPLETE);
		return expressionList;
	}

}
