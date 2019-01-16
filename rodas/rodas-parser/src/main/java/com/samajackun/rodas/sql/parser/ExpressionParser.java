package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionList;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;

public final class ExpressionParser extends AbstractParser<Expression>
{
	private static final ExpressionParser INSTANCE=new ExpressionParser();

	public static ExpressionParser getInstance()
	{
		return ExpressionParser.INSTANCE;
	}

	private ExpressionParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_COMMA
	}

	@Override
	public Expression parse(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		// return SelectSentenceParser.getInstance().parse(tokenizer);
		return LogicalExpressionParser.getInstance().parse(tokenizer);
	};

	public ExpressionList parseExpressionList(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		ExpressionList expressionList=new ExpressionList();
		State state=State.INITIAL;
		do
		{
			switch (state)
			{
				case INITIAL:
					SqlToken token=tokenizer.nextUsefulToken();
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
							Expression expression=parse(tokenizer);
							if (expression != null)
							{
								expressionList.add(expression);
							}
						}
					}
					state=State.EXPECTING_COMMA;
					break;
				case EXPECTING_COMMA:
					SqlToken token2=tokenizer.nextUsefulToken();
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
