package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionList;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;

public final class ExpressionParser extends AbstractParser<Expression>
{
	private static final ExpressionParser INSTANCE=new ExpressionParser();

	public static ExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private ExpressionParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_COMMA
	}

	@Override
	public Expression parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		// return SelectSentenceParser.getInstance().parse(tokenizer);
		return LogicalExpressionParser.getInstance().parse(tokenizer);
	};

	public ExpressionList parseExpressionList(ParserTokenizer tokenizer)
		throws ParserException
	{
		ExpressionList expressionList=new ExpressionList();
		State state=State.INITIAL;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					SqlToken token=tokenizer.nextUsefulToken();
					if (token.getType() == SqlToken.Type.PARENTHESIS_END)
					{
						tokenizer.pushBack();
						state=State.COMPLETE;
					}
					else
					{
						tokenizer.pushBack();
						Expression expression=parse(tokenizer);
						if (expression != null)
						{
							expressionList.add(expression);
						}
					}
					state=State.EXPECTING_COMMA;
					break;
				case EXPECTING_COMMA:
					if (tokenizer.hasMoreTokens())
					{
						SqlToken token2=tokenizer.nextUsefulToken();
						switch (token2.getType())
						{
							case COMMA:
								state=State.INITIAL;
								break;
							default:
								tokenizer.pushBack();
								state=State.COMPLETE;
						}
						break;
					}
					else
					{
						tokenizer.pushBack();
						state=State.COMPLETE;
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expressionList;
	}
}
