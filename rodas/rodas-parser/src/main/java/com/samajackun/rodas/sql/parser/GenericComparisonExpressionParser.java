package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.AndExpression;
import com.samajackun.rodas.core.model.BetweenExpression;
import com.samajackun.rodas.core.model.BinaryExpressionsFactories;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IExpressionFactory;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.IsOfTypeExpression;
import com.samajackun.rodas.core.model.NotExpression;
import com.samajackun.rodas.core.model.NullConstantExpression;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;
import com.samajackun.rodas.sql.tokenizer.SqlToken.Type;

public class GenericComparisonExpressionParser extends AbstractParser<Expression> implements PartialParser
{
	private enum State {
		INITIAL, COMPLETE, EXPECTING_OPERATOR_OR, EXPECTING_CONJUNCTION_EXPRESSION, EXPECTING_OPERATOR, EXPECTING_UNIT_OPERATOR, EXPECTING_NOT_EXPRESSION, EXPECTING_OPERATOR_AND, EXPECTING_OF_OR_NOT_OR_NULL, EXPECTING_INTERVAL_BOUNDS, EXPECTING_NULL, EXPECTING_IN_OR_BETWEEN, EXPECTING_TYPE, EXPECTING_TYPE_NAME, EXPECTING_NOT_OR_NULL
	}

	public GenericComparisonExpressionParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	@Override
	public Expression parse(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		return parseComparisonExpression(tokenizer);
	};

	Expression parseNotExpression(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		State state=State.EXPECTING_NOT_EXPRESSION;
		String operator=null;
		Expression t;
		while (tokenizer.tokenWasRead() && state != State.COMPLETE)
		{
			SqlToken token=tokenizer.nextOptionalUsefulToken();
			if (token != null)
			{
				switch (token.getType())
				{
					case OPERATOR_NOT:
						operator=token.getValue();
						t=parseNotExpression(tokenizer);
						expression=new NotExpression(operator, t);
						break;
					default:
						tokenizer.pushBack(token);
						expression=parseComparisonExpression(tokenizer);
				}
			}
			state=State.COMPLETE;
		}
		return expression;
	}

	Expression parseComparisonExpression(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		// IS [NOT] NULL, LIKE, [NOT] BETWEEN, [NOT] IN, EXISTS, IS OF type comparison
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		IExpressionFactory expressionFactory=null;
		if (tokenizer.tokenWasRead())
		{
			Expression t=getParserFactory().getRelationalExpressionParser().parse(tokenizer);
			if (t != null)
			{
				expression=expression == null
					? t
					: expressionFactory.create(t);
				state=State.EXPECTING_OPERATOR;
			}
			else
			{
				throw new IncompleteExpressionException();
			}
			while (tokenizer.tokenWasRead() && state != State.COMPLETE)
			{
				SqlToken token=tokenizer.nextOptionalUsefulToken();
				if (token != null)
				{
					switch (state)
					{
						case EXPECTING_OPERATOR:
							switch (token.getType())
							{
								case OPERATOR_IS:
									// t=parseNotExpression(tokenizer);
									// expression=new IsExpression(token.getValue(), expression, t);
									expressionFactory=BinaryExpressionsFactories.getInstance().createIsExpressionFactory(token.getValue(), expression);
									operator=token.getValue();
									state=State.EXPECTING_OF_OR_NOT_OR_NULL;
									break;
								case OPERATOR_LIKE:
									expressionFactory=BinaryExpressionsFactories.getInstance().createLikeExpressionFactory(token.getValue(), expression);
									t=getParserFactory().getRelationalExpressionParser().parse(tokenizer);
									if (t != null)
									{
										expression=expression == null
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
									expressionFactory=BinaryExpressionsFactories.getInstance().createInExpressionFactory(token.getValue(), expression);
									Expression t2=getParserFactory().getRelationalExpressionParser().parse(tokenizer);
									expression=expressionFactory.create(t2);
									state=State.EXPECTING_OPERATOR;
									break;
								case OPERATOR_BETWEEN:
									AndExpression andExp=parseAndExpression(tokenizer);
									expression=new BetweenExpression(token.getValue(), expression, andExp);
									break;
								default:
									tokenizer.pushBack(token);
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
									operator=operator + token.getValue();
									state=State.EXPECTING_TYPE;
									break;
								case OPERATOR_NOT:
									expressionFactory=BinaryExpressionsFactories.getInstance().createNotExpressionFactory(token.getValue(), expressionFactory);
									state=State.EXPECTING_NOT_OR_NULL;
									break;
								case KEYWORD_NULL:
									expression=expressionFactory.create(new NullConstantExpression(token.getValue()));
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
									expressionFactory=BinaryExpressionsFactories.getInstance().createNotExpressionFactory(token.getValue(), expressionFactory);
									break;
								case KEYWORD_NULL:
									expression=expressionFactory.create(new NullConstantExpression(token.getValue()));
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
									operator=operator + token.getValue();
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
									expression=new IsOfTypeExpression(operator, expression, new IdentifierExpression(token.getValue()));
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
									expression=expressionFactory.create(new NullConstantExpression(token.getValue()));
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
						// operator=operator + token5.getValue();
						// tgetParserFactory().getExpressionParser().parse(tokenizer);
						// expression=new BinaryExpression(operator, expression, t);
						// state=State.EXPECTING_RELATIONAL_EXPRESSION;
						// break;
						// case OPERATOR_BETWEEN:
						// expressionFactory=BinaryExpressionsFactories.getInstance().createBetweenExpressionFactory(token5.getValue(), expression);
						// tgetParserFactory().getArithmeticExpressionParser().parse(tokenizer);
						// expression=expressionFactory.create(t);
						// state=State.EXPECTING_INTERVAL_BOUNDS;
						// break;
						// default:
						// throw new UnexpectedTokenException(token5);
						// }
						// break;
						// case EXPECTING_RELATIONAL_EXPRESSION:
						// Expression t2=getParserFactory().getRelationalExpressionParser().parse(tokenizer);
						// System.out.println("t2=" + t2.toCode());
						// expression=expressionFactory.create(t2);
						// state=State.EXPECTING_OPERATOR;
						// break;
						default:
							throw new IllegalStateException(state.toString());
					}
				}
			}
		}
		return expression;
	}

	// Expression parseTypeExpression(SqlMatchingTokenizer tokenizer)
	// throws ParserException,IOException
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
	// operator=token2.getValue();
	// t=getParserFactory().getExpressionParser().parse(tokenizer);
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

	Expression parseNullConstant(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		State state=State.INITIAL;
		while (tokenizer.tokenWasRead() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					SqlToken token2=tokenizer.nextOptionalUsefulToken();
					if (token2 != null)
					{
						switch (token2.getType())
						{
							case KEYWORD_NULL:
								expression=new NullConstantExpression(token2.getValue());
								break;
							default:
								throw new UnexpectedTokenException(token2);
						}
					}
					state=State.COMPLETE;
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}

	AndExpression parseAndExpression(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		AndExpression andExpression=null;
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		Expression t;
		while (tokenizer.tokenWasRead() && state != State.COMPLETE)
		{
			switch (state)
			{
				case INITIAL:
					expression=getParserFactory().getArithmeticExpressionParser().parse(tokenizer);
					state=State.EXPECTING_OPERATOR_AND;
					break;
				case EXPECTING_OPERATOR_AND:
					SqlToken token6=tokenizer.nextOptionalUsefulToken();
					if (token6 != null)
					{
						if (token6.getType() == Type.OPERATOR_AND)
						{
							operator=token6.getValue();
							t=getParserFactory().getArithmeticExpressionParser().parse(tokenizer);
							andExpression=new AndExpression(operator, expression, t);
						}
						else
						{
							throw new UnexpectedTokenException(token6);
						}
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
