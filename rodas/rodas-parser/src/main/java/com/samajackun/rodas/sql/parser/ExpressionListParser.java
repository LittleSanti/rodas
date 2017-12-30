package com.samajackun.rodas.sql.parser;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public class ExpressionListParser extends AbstractParser<List<Expression>>
{
	private static final ExpressionListParser INSTANCE=new ExpressionListParser();

	public static ExpressionListParser getInstance()
	{
		return INSTANCE;
	}

	private ExpressionListParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, READ_EXPRESSION,
	};

	@Override
	public List<Expression> parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		List<Expression> expressions=new ArrayList<Expression>();
		State state=State.INITIAL;
		while (state != State.COMPLETE && tokenizer.hasMoreTokens())
		{
			SqlToken token=tokenizer.nextUsefulToken();
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case PARENTHESIS_START:
							SqlToken token2=tokenizer.nextUsefulToken();
							if (token2.getType() == SqlToken.Type.PARENTHESIS_END)
							{
								tokenizer.pushBack();
								state=State.COMPLETE;
							}
							else
							{
								tokenizer.pushBack();

								Expression expression=ExpressionParser.getInstance().parse(tokenizer);
								if (expression != null)
								{
									expressions.add(expression);
								}
							}
							state=State.READ_EXPRESSION;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READ_EXPRESSION:
					switch (token.getType())
					{
						case COMMA:
							Expression expression=ExpressionParser.getInstance().parse(tokenizer);
							if (expression == null)
							{
								throw new ParserException("An expression was expected after a comma");
							}
							expressions.add(expression);
							state=State.READ_EXPRESSION;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
					}
					break;
				default:
					// Ignorar.
			}
		}
		return expressions;
	}

}
