package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.core.model.AndExpression;
import com.samajackun.rodas.core.model.BetweenExpression;
import com.samajackun.rodas.core.model.BinaryExpressionsFactories;
import com.samajackun.rodas.core.model.ExistsExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IExpressionFactory;
import com.samajackun.rodas.core.model.NotExpression;
import com.samajackun.rodas.core.model.NullConstantExpression;
import com.samajackun.rodas.core.model.OrExpression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public final class LogicalExpressionParser extends AbstractParser<Expression>
{
	private static final LogicalExpressionParser INSTANCE=new LogicalExpressionParser();

	public static LogicalExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private LogicalExpressionParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_OPERATOR_OR, EXPECTING_CONJUNCTION_EXPRESSION, EXPECTING_OPERATOR, EXPECTING_UNIT_OPERATOR, EXPECTING_NOT_EXPRESSION, EXPECTING_OPERATOR_AND, EXPECTING_RELATIONAL_EXPRESSION, EXPECTING_OF_OR_NOT_OR_NULL, EXPECTING_INTERVAL_BOUNDS, EXPECTING_NULL, EXPECTING_IN_OR_BETWEEN
	}

	@Override
	public Expression parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		return parseLogicalExpression(tokenizer);
	};

	Expression parseLogicalExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		Expression t;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					state=State.EXPECTING_CONJUNCTION_EXPRESSION;
					break;
				case EXPECTING_CONJUNCTION_EXPRESSION:
					t=parseConjunctionExpression(tokenizer);
					if (t != null)
					{
						expression=(expression == null)
							? t
							: new OrExpression(operator, expression, t);
						state=State.EXPECTING_OPERATOR_OR;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				case EXPECTING_OPERATOR_OR:
					SqlToken token1=tokenizer.nextUsefulToken();
					switch (token1.getType())
					{
						case OPERATOR_OR:
							operator=token1.getImage();
							state=State.EXPECTING_CONJUNCTION_EXPRESSION;
							break;
						case PARENTHESIS_END:
						case COMMA:
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
							break;
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}

	Expression parseConjunctionExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		Expression t;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					state=State.EXPECTING_NOT_EXPRESSION;
					break;
				case EXPECTING_NOT_EXPRESSION:
					t=parseNotExpression(tokenizer);
					if (t != null)
					{
						expression=(expression == null)
							? t
							: new AndExpression(operator, expression, t);
						state=State.EXPECTING_OPERATOR_AND;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				case EXPECTING_OPERATOR_AND:
					SqlToken token1=tokenizer.nextUsefulToken();
					switch (token1.getType())
					{
						case OPERATOR_AND:
							operator=token1.getImage();
							state=State.EXPECTING_NOT_EXPRESSION;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
							break;
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}

	Expression parseNotExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		Expression t;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					state=State.EXPECTING_NOT_EXPRESSION;
					break;
				case EXPECTING_NOT_EXPRESSION:
					SqlToken token2=tokenizer.nextUsefulToken();
					switch (token2.getType())
					{
						case OPERATOR_NOT:
							operator=token2.getImage();
							t=parseNotExpression(tokenizer);
							expression=new NotExpression(operator, t);
							break;
						case OPERATOR_EXISTS:
							operator=token2.getImage();
							t=parseComparisonExpression(tokenizer);
							expression=new ExistsExpression(operator, t);
							break;
						default:
							tokenizer.pushBack();
							expression=parseComparisonExpression(tokenizer);
					}
					state=State.COMPLETE;
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}

	Expression parseComparisonExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		// IS [NOT] NULL, LIKE, [NOT] BETWEEN, [NOT] IN, EXISTS, IS OF type comparison
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		Expression t;
		IExpressionFactory expressionFactory=null;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					state=State.EXPECTING_RELATIONAL_EXPRESSION;
					break;
				case EXPECTING_RELATIONAL_EXPRESSION:
					t=ComparisonExpressionParser.getInstance().parse(tokenizer);
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
						case OPERATOR_IS:
							// operator=token1.getImage();
							expressionFactory=BinaryExpressionsFactories.getInstance().createIsExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_OF_OR_NOT_OR_NULL;
							break;
						case OPERATOR_LIKE:
							expressionFactory=BinaryExpressionsFactories.getInstance().createLikeExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_RELATIONAL_EXPRESSION;
							break;
						case OPERATOR_IN:
							expressionFactory=BinaryExpressionsFactories.getInstance().createInExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_RELATIONAL_EXPRESSION;
							break;
						case OPERATOR_NOT:
							operator=token1.getImage();
							expressionFactory=BinaryExpressionsFactories.getInstance().createBetweenExpressionFactory(token1.getImage(), expression);
							state=State.EXPECTING_IN_OR_BETWEEN;
							break;
						case OPERATOR_BETWEEN:
							operator=token1.getImage();
							state=State.EXPECTING_INTERVAL_BOUNDS;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
					}
					break;
				case EXPECTING_INTERVAL_BOUNDS:
					t=parseConjunctionExpression(tokenizer);
					if (t != null)
					{
						// FIXME
						expression=new BetweenExpression(operator, expression, (AndExpression)t);
						state=State.COMPLETE;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				case EXPECTING_OF_OR_NOT_OR_NULL:
					SqlToken token3=tokenizer.nextUsefulToken();
					switch (token3.getType())
					{
						case OPERATOR_OF:
							operator=operator + token3.getImage();
							state=State.EXPECTING_RELATIONAL_EXPRESSION;
							break;
						case OPERATOR_NOT:
							// operator=operator + token3.getImage();
							expressionFactory=BinaryExpressionsFactories.getInstance().createIsNotExpressionFactory(token3.getImage(), expressionFactory);
							state=State.EXPECTING_NULL;
							break;
						case KEYWORD_NULL:
							expression=expressionFactory.create(new NullConstantExpression(token3.getImage()));
							state=State.COMPLETE;
							break;
						default:
							throw new UnexpectedTokenException(token3);
					}
					break;
				case EXPECTING_NULL:
					SqlToken token4=tokenizer.nextUsefulToken();
					switch (token4.getType())
					{
						case KEYWORD_NULL:
							expression=expressionFactory.create(new NullConstantExpression(token4.getImage()));
							state=State.COMPLETE;
							break;
						default:
							throw new UnexpectedTokenException(token4);
					}
					break;
				case EXPECTING_IN_OR_BETWEEN:
					SqlToken token5=tokenizer.nextUsefulToken();
					switch (token5.getType())
					{
						case OPERATOR_IN:
							operator=operator + token5.getImage();
							state=State.EXPECTING_RELATIONAL_EXPRESSION;
							break;
						case OPERATOR_BETWEEN:
							operator=token5.getImage();
							state=State.EXPECTING_INTERVAL_BOUNDS;
							break;
						default:
							throw new UnexpectedTokenException(token5);
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}
}
