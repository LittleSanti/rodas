package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.sql.model.AndExpression;
import com.samajackun.rodas.sql.model.BetweenExpression;
import com.samajackun.rodas.sql.model.BinaryExpressionsFactories;
import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.model.IExpressionFactory;
import com.samajackun.rodas.sql.model.IdentifierExpression;
import com.samajackun.rodas.sql.model.IsOfTypeExpression;
import com.samajackun.rodas.sql.model.NotExpression;
import com.samajackun.rodas.sql.model.NullConstantExpression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken.Type;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public final class ComparisonExpressionParser extends AbstractParser<Expression>
{
	private static final ComparisonExpressionParser INSTANCE=new ComparisonExpressionParser();

	public static ComparisonExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private ComparisonExpressionParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_OPERATOR_OR, EXPECTING_CONJUNCTION_EXPRESSION, EXPECTING_OPERATOR, EXPECTING_UNIT_OPERATOR, EXPECTING_NOT_EXPRESSION, EXPECTING_OPERATOR_AND, EXPECTING_OF_OR_NOT_OR_NULL, EXPECTING_INTERVAL_BOUNDS, EXPECTING_NULL, EXPECTING_IN_OR_BETWEEN, EXPECTING_TYPE, EXPECTING_TYPE_NAME, EXPECTING_NOT_OR_NULL
	}

	@Override
	public Expression parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		return parseComparisonExpression(tokenizer);
	};

	Expression parseNotExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		Expression expression=null;
		State state=State.EXPECTING_NOT_EXPRESSION;
		String operator=null;
		Expression t;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			SqlToken token=tokenizer.nextUsefulToken();
			switch (token.getType())
			{
				case OPERATOR_NOT:
					operator=token.getImage();
					t=parseNotExpression(tokenizer);
					expression=new NotExpression(operator, t);
					break;
				default:
					tokenizer.pushBack();
					expression=parseComparisonExpression(tokenizer);
			}
			state=State.COMPLETE;
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
		IExpressionFactory expressionFactory=null;
		if (tokenizer.hasMoreTokens())
		{
			Expression t=RelationalExpressionParser.getInstance().parse(tokenizer);
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
			while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
			{
				SqlToken token=tokenizer.nextUsefulToken();
				switch (state)
				{
					case EXPECTING_OPERATOR:
						switch (token.getType())
						{
							case OPERATOR_IS:
								// t=parseNotExpression(tokenizer);
								// expression=new IsExpression(token.getImage(), expression, t);
								expressionFactory=BinaryExpressionsFactories.getInstance().createIsExpressionFactory(token.getImage(), expression);
								operator=token.getImage();
								state=State.EXPECTING_OF_OR_NOT_OR_NULL;
								break;
							case OPERATOR_LIKE:
								expressionFactory=BinaryExpressionsFactories.getInstance().createLikeExpressionFactory(token.getImage(), expression);
								t=RelationalExpressionParser.getInstance().parse(tokenizer);
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
							// case OPERATOR_EXISTS:
							// state=State.EXPECTING_RELATIONAL_EXPRESSION;
							// break;
							case OPERATOR_IN:
								expressionFactory=BinaryExpressionsFactories.getInstance().createInExpressionFactory(token.getImage(), expression);
								Expression t2=RelationalExpressionParser.getInstance().parse(tokenizer);
								expression=expressionFactory.create(t2);
								state=State.EXPECTING_OPERATOR;
								break;
							case OPERATOR_BETWEEN:
								AndExpression andExp=parseAndExpression(tokenizer);
								expression=new BetweenExpression(token.getImage(), expression, andExp);
								break;
							default:
								tokenizer.pushBack();
								state=State.COMPLETE;
						}
						break;
					// case EXPECTING_INTERVAL_BOUNDS:
					// t=parseNotExpression(tokenizer);
					// if (t != null)
					// {
					// expression=new BinaryExpression(operator, expression, t);
					// state=State.EXPECTING_OPERATOR_AND;
					// }
					// else
					// {
					// throw new IncompleteExpressionException();
					// }
					// break;
					case EXPECTING_OF_OR_NOT_OR_NULL:
						switch (token.getType())
						{
							case OPERATOR_OF:
								// FIXME Pendiente.
								// t=parseTypeExpression(tokenizer);
								// expression=new BinaryExpression(operator, expression, t);
								// state=State.EXPECTING_RELATIONAL_EXPRESSION;
								operator=operator + token.getImage();
								state=State.EXPECTING_TYPE;
								break;
							case OPERATOR_NOT:
								expressionFactory=BinaryExpressionsFactories.getInstance().createNotExpressionFactory(token.getImage(), expressionFactory);
								state=State.EXPECTING_NOT_OR_NULL;
								break;
							case KEYWORD_NULL:
								expression=expressionFactory.create(new NullConstantExpression(token.getImage()));
								state=State.COMPLETE;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					case EXPECTING_NOT_OR_NULL:
						switch (token.getType())
						{
							case OPERATOR_NOT:
								expressionFactory=BinaryExpressionsFactories.getInstance().createNotExpressionFactory(token.getImage(), expressionFactory);
								break;
							case KEYWORD_NULL:
								expression=expressionFactory.create(new NullConstantExpression(token.getImage()));
								state=State.COMPLETE;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					case EXPECTING_TYPE:
						switch (token.getType())
						{
							case OPERATOR_TYPE:
								operator=operator + token.getImage();
								state=State.EXPECTING_TYPE_NAME;
								break;
							default:
								throw new UnexpectedTokenException(token, Type.OPERATOR_TYPE);
						}
						break;
					case EXPECTING_TYPE_NAME:
						switch (token.getType())
						{
							case IDENTIFIER:
								expression=new IsOfTypeExpression(operator, expression, new IdentifierExpression(token.getImage()));
								state=State.COMPLETE;
								break;
							default:
								throw new UnexpectedTokenException(token, Type.OPERATOR_TYPE);
						}
						break;
					case EXPECTING_NULL:
						switch (token.getType())
						{
							case KEYWORD_NULL:
								expression=expressionFactory.create(new NullConstantExpression(token.getImage()));
								state=State.COMPLETE;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					// case EXPECTING_IN_OR_BETWEEN:
					// SqlToken token5=tokenizer.nextUsefulToken();
					// switch (token5.getType())
					// {
					// case OPERATOR_IN:
					// operator=operator + token5.getImage();
					// t=ExpressionParser.getInstance().parse(tokenizer);
					// expression=new BinaryExpression(operator, expression, t);
					// state=State.EXPECTING_RELATIONAL_EXPRESSION;
					// break;
					// case OPERATOR_BETWEEN:
					// expressionFactory=BinaryExpressionsFactories.getInstance().createBetweenExpressionFactory(token5.getImage(), expression);
					// t=ArithmeticExpressionParser.getInstance().parse(tokenizer);
					// expression=expressionFactory.create(t);
					// state=State.EXPECTING_INTERVAL_BOUNDS;
					// break;
					// default:
					// throw new UnexpectedTokenException(token5);
					// }
					// break;
					// case EXPECTING_RELATIONAL_EXPRESSION:
					// Expression t2=RelationalExpressionParser.getInstance().parse(tokenizer);
					// System.out.println("t2=" + t2.toCode());
					// expression=expressionFactory.create(t2);
					// state=State.EXPECTING_OPERATOR;
					// break;
					default:
						throw new IllegalStateException(state.toString());
				}
			}
		}
		return expression;
	}

	// Expression parseTypeExpression(ParserTokenizer tokenizer)
	// throws ParserException
	// {
	// Expression expression=null;
	// State state=State.INITIAL;
	// String operator=null;
	// Expression t;
	// while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
	// {
	// switch (state)
	// {
	// case INITIAL:
	// SqlToken token2=tokenizer.nextUsefulToken();
	// switch (token2.getType())
	// {
	// case OPERATOR_TYPE:
	// operator=token2.getImage();
	// t=ExpressionParser.getInstance().parse(tokenizer);
	// expression=new UnitExpression(operator, t);
	// break;
	// default:
	// throw new UnexpectedTokenException(token2);
	// }
	// state=State.COMPLETE;
	// break;
	// default:
	// throw new IllegalStateException();
	// }
	// }
	// return expression;
	// }

	Expression parseNullConstant(ParserTokenizer tokenizer)
		throws ParserException
	{
		Expression expression=null;
		State state=State.INITIAL;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					SqlToken token2=tokenizer.nextUsefulToken();
					switch (token2.getType())
					{
						case KEYWORD_NULL:
							expression=new NullConstantExpression(token2.getImage());
							break;
						default:
							throw new UnexpectedTokenException(token2);
					}
					state=State.COMPLETE;
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}

	AndExpression parseAndExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		AndExpression andExpression=null;
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		Expression t;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					expression=ArithmeticExpressionParser.getInstance().parse(tokenizer);
					state=State.EXPECTING_OPERATOR_AND;
					break;
				case EXPECTING_OPERATOR_AND:
					SqlToken token6=tokenizer.nextUsefulToken();
					if (token6.getType() == Type.OPERATOR_AND)
					{
						operator=token6.getImage();
						t=ArithmeticExpressionParser.getInstance().parse(tokenizer);
						andExpression=new AndExpression(operator, expression, t);
					}
					else
					{
						throw new UnexpectedTokenException(token6);
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		if (andExpression == null)
		{
			throw new IllegalStateException();
		}
		return andExpression;
	}
}
