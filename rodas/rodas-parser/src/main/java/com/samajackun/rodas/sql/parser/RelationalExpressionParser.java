package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.BinaryExpressionsFactories;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IExpressionFactory;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;

public final class RelationalExpressionParser extends AbstractParser<Expression>
{
	private static final RelationalExpressionParser INSTANCE=new RelationalExpressionParser();

	public static RelationalExpressionParser getInstance()
	{
		return RelationalExpressionParser.INSTANCE;
	}

	private RelationalExpressionParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_OPERATOR_OR_NEGATION, EXPECTING_TERMINAL, EXPECTING_OPERATOR, EXPECTING_UNIT_OPERATOR
	}

	@Override
	public Expression parse(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		return parseRelationalExpression(tokenizer);
	};

	Expression parseRelationalExpression(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		State state=State.INITIAL;
		Expression t;
		IExpressionFactory expressionFactory=null;
		do
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
					if (token1 != null)
					{
						switch (token1.getType())
						{
							case OPERATOR_EQUALS:
								expressionFactory=BinaryExpressionsFactories.getInstance().createEqualsExpressionFactory(token1.getValue(), expression);
								state=State.EXPECTING_TERMINAL;
								break;
							case OPERATOR_DISTINCT1:
							case OPERATOR_DISTINCT2:
								expressionFactory=BinaryExpressionsFactories.getInstance().createNotEqualsExpressionFactory(token1.getValue(), expression);
								state=State.EXPECTING_TERMINAL;
								break;
							case OPERATOR_GREATER:
								expressionFactory=BinaryExpressionsFactories.getInstance().createGreaterThanExpressionFactory(token1.getValue(), expression);
								state=State.EXPECTING_TERMINAL;
								break;
							case OPERATOR_GREATER_OR_EQUALS:
								expressionFactory=BinaryExpressionsFactories.getInstance().createGreaterThanOrEqualsExpressionFactory(token1.getValue(), expression);
								state=State.EXPECTING_TERMINAL;
								break;
							case OPERATOR_LOWER:
								expressionFactory=BinaryExpressionsFactories.getInstance().createLowerThanExpressionFactory(token1.getValue(), expression);
								state=State.EXPECTING_TERMINAL;
								break;
							case OPERATOR_LOWER_OR_EQUALS:
								expressionFactory=BinaryExpressionsFactories.getInstance().createLowerThanOrEqualsExpressionFactory(token1.getValue(), expression);
								state=State.EXPECTING_TERMINAL;
								break;
							default:
								tokenizer.pushBack(token1);
								state=State.COMPLETE;
						}
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		while (tokenizer.tokenWasRead() && state != State.COMPLETE);
		return expression;
	}
}
