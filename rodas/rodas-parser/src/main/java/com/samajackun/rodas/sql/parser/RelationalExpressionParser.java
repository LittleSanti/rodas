package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.sql.model.BinaryExpressionsFactories;
import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.model.IExpressionFactory;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;

public final class RelationalExpressionParser extends AbstractParser<Expression>
{
	private static final RelationalExpressionParser INSTANCE=new RelationalExpressionParser();

	public static RelationalExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private RelationalExpressionParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_OPERATOR_OR_NEGATION, EXPECTING_TERMINAL, EXPECTING_OPERATOR, EXPECTING_UNIT_OPERATOR
	}

	@Override
	public Expression parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		return parseRelationalExpression(tokenizer);
	};

	Expression parseRelationalExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		Expression expression=null;
		State state=State.INITIAL;
		Expression t;
		IExpressionFactory expressionFactory=null;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					state=State.EXPECTING_TERMINAL;
					break;
				case EXPECTING_TERMINAL:
					t=ArithmeticExpressionParser.getInstance().parse(tokenizer);
					if (t != null)
					{
						expression=(expression == null)
							? t
							: expressionFactory.create(t);
						state=State.EXPECTING_OPERATOR;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				case EXPECTING_OPERATOR:
					SqlToken token1=tokenizer.nextUsefulToken();
					switch (token1.getType())
					{
						case OPERATOR_EQUALS:
							expressionFactory=BinaryExpressionsFactories.getInstance().createEqualsExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_TERMINAL;
							break;
						case OPERATOR_DISTINCT1:
						case OPERATOR_DISTINCT2:
							expressionFactory=BinaryExpressionsFactories.getInstance().createNotEqualsExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_TERMINAL;
							break;
						case OPERATOR_GREATER:
							expressionFactory=BinaryExpressionsFactories.getInstance().createGreaterThanExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_TERMINAL;
							break;
						case OPERATOR_GREATER_OR_EQUALS:
							expressionFactory=BinaryExpressionsFactories.getInstance().createGreaterThanOrEqualsExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_TERMINAL;
							break;
						case OPERATOR_LOWER:
							expressionFactory=BinaryExpressionsFactories.getInstance().createLowerThanExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_TERMINAL;
							break;
						case OPERATOR_LOWER_OR_EQUALS:
							expressionFactory=BinaryExpressionsFactories.getInstance().createLowerThanOrEqualsExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_TERMINAL;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}
}
