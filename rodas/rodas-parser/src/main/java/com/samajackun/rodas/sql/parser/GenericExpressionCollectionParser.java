package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionCollection;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;

public class GenericExpressionCollectionParser extends AbstractParser<ExpressionCollection>
{
	public GenericExpressionCollectionParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_COMMA
	}

	@Override
	public ExpressionCollection parse(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
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
					Token token=tokenizer.nextOptionalUsefulToken();
					if (token != null)
					{
						if (token.getType().equals(SqlTokenTypes.PARENTHESIS_END))
						{
							tokenizer.pushBack(token);
							state=State.COMPLETE;
						}
						else
						{
							tokenizer.pushBack(token);
							Expression expression=getParserFactory().getExpressionParser().parse(tokenizer, parserContext);
							if (expression != null)
							{
								expressionList.add(expression);
							}
						}
					}
					state=State.EXPECTING_COMMA;
					break;
				case EXPECTING_COMMA:
					Token token2=tokenizer.nextOptionalUsefulToken();
					if (token2 != null)
					{
						switch (token2.getType())
						{
							case SqlTokenTypes.COMMA:
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
