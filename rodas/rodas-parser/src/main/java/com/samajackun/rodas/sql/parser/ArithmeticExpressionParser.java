package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.core.model.AddExpression;
import com.samajackun.rodas.core.model.AsteriskExpression;
import com.samajackun.rodas.core.model.BooleanConstantExpression;
import com.samajackun.rodas.core.model.ConcatExpression;
import com.samajackun.rodas.core.model.DivideExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionList;
import com.samajackun.rodas.core.model.FunctionExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.MultiplyExpression;
import com.samajackun.rodas.core.model.NamedParameterExpression;
import com.samajackun.rodas.core.model.NullConstantExpression;
import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.rodas.core.model.ParehentesizedExpression;
import com.samajackun.rodas.core.model.SubstractExpression;
import com.samajackun.rodas.core.model.TextConstantExpression;
import com.samajackun.rodas.core.model.UnitMinusExpression;
import com.samajackun.rodas.core.model.UnitPlusExpression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public final class ArithmeticExpressionParser extends AbstractParser<Expression>
{
	private static final ArithmeticExpressionParser INSTANCE=new ArithmeticExpressionParser();

	public static ArithmeticExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private ArithmeticExpressionParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_OPERATOR, EXPECTING_TERMINAL, EXPECTING_TERMINAL_MULTIPLY, EXPECTING_TERMINAL_DIVIDE, EXPECTING_TERMINAL_PLUS, EXPECTING_TERMINAL_MINUS, EXPECTING_TERMINAL_CONCATENATION, EXPECTING_COMMA_OR_PAREHENTESIS_CLOSING
	}

	@Override
	public Expression parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		return parseAddingExpression(tokenizer);
	};

	Expression parseTerminal(ParserTokenizer tokenizer)
		throws ParserException
	{
		// Reconocer expresiones constante e identificador
		Expression expression=null;
		ExpressionList expressionList=null;
		State state=State.INITIAL;
		while (tokenizer.hasMoreTokens() && state != State.COMPLETE)
		{
			SqlToken token=tokenizer.nextUsefulToken();
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case INTEGER_NUMBER_LITERAL:
							expression=new NumericConstantExpression(token.getImage(), Long.parseLong(token.getImage()));
							state=State.COMPLETE;
							break;
						case DECIMAL_NUMBER_LITERAL:
							expression=new NumericConstantExpression(token.getImage(), Double.parseDouble(token.getImage()));
							state=State.COMPLETE;
							break;
						case TEXT_LITERAL:
							expression=new TextConstantExpression(token.getImage().substring(1, token.getImage().length() - 1));
							state=State.COMPLETE;
							break;
						case KEYWORD_NULL:
							expression=new NullConstantExpression(token.getImage());
							state=State.COMPLETE;
							break;
						case TRUE:
							expression=BooleanConstantExpression.createTrueConstrantExpression(token.getImage());
							state=State.COMPLETE;
							break;
						case FALSE:
							expression=BooleanConstantExpression.createFalseConstrantExpression(token.getImage());
							state=State.COMPLETE;
							break;
						case IDENTIFIER:
							// Lookahead
							SqlToken token2=tokenizer.nextUsefulToken();
							if (token2 != null)
							{
								if (token2.getType() == SqlToken.Type.PARENTHESIS_START)
								{
									ExpressionList argumentsExpressions=ExpressionParser.getInstance().parseExpressionList(tokenizer);
									FunctionExpression functionExpression=new FunctionExpression(token.getImage());
									functionExpression.getArguments().addAll(argumentsExpressions.getExpressions());
									tokenizer.matchToken(SqlToken.Type.PARENTHESIS_END);
									expression=functionExpression;
								}
								else if (token2.getType() == SqlToken.Type.PERIOD)
								{
									expression=parsePrefixedExpression(tokenizer, token.getImage());
								}
								else
								{
									expression=new IdentifierExpression(token.getImage());
									tokenizer.pushBack();
								}
							}
							else
							{
								expression=new IdentifierExpression(token.getImage());
							}
							state=State.COMPLETE;
							break;
						case NAMED_PARAMETER:
							expression=new NamedParameterExpression(token.getImage());
							state=State.COMPLETE;
							break;
						case PARENTHESIS_START:
							expression=parseSelectOrExpression(tokenizer);
							// tokenizer.matchToken(SqlToken.Type.PARENTHESIS_END);
							// expression=new ParehentesizedExpression(expression);
							state=State.EXPECTING_COMMA_OR_PAREHENTESIS_CLOSING;
							break;
						case ASTERISK:
							expression=new AsteriskExpression(token.getImage());
							state=State.COMPLETE;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case EXPECTING_COMMA_OR_PAREHENTESIS_CLOSING:
					switch (token.getType())
					{
						case PARENTHESIS_END:
							expression=new ParehentesizedExpression(expression);
							state=State.COMPLETE;
							break;
						case COMMA:
							if (expressionList == null)
							{
								expressionList=new ExpressionList();
								expressionList.add(expression);
							}
							expression=expressionList;
							Expression t=ExpressionParser.getInstance().parse(tokenizer);
							if (t != null)
							{
								expressionList.add(t);
							}
							else
							{
								throw new ParserException("Expected expression");
							}
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				default:
					throw new IllegalStateException(state.toString());
			}
		}
		if (expression == null)
		{
			throw new ParserException("End of stream");
		}
		return expression;
	}

	private Expression parseSelectOrExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		if (tokenizer.hasMoreTokens())
		{
			Expression expression;
			SqlToken sqlToken=tokenizer.nextToken();
			tokenizer.pushBack();
			switch (sqlToken.getType())
			{
				case KEYWORD_SELECT:
					expression=SelectSentenceParser.getInstance().parse(tokenizer);
					break;
				default:
					expression=ExpressionParser.getInstance().parse(tokenizer);
			}
			return expression;
		}
		else
		{
			throw new IncompleteExpressionException();
		}
	}

	Expression parseSignExpression(ParserTokenizer tokenizer)
		throws ParserException
	{
		Expression expression=null;
		while (tokenizer.hasMoreTokens() && expression == null)
		{
			SqlToken token=tokenizer.nextToken();
			switch (token.getType())
			{
				case OPERATOR_PLUS:
					expression=parseTerminal(tokenizer);
					expression=new UnitPlusExpression(token.getImage(), expression);
					break;
				case OPERATOR_MINUS:
					expression=parseTerminal(tokenizer);
					expression=new UnitMinusExpression(token.getImage(), expression);
					break;
				default:
					tokenizer.pushBack();
					expression=parseTerminal(tokenizer);
			}
		}
		return expression;
	}

	Expression parseMultiplyingExpression(ParserTokenizer tokenizer)
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
					expression=parseSignExpression(tokenizer);
					state=State.EXPECTING_OPERATOR;
					break;
				case EXPECTING_OPERATOR:
					SqlToken token=tokenizer.nextUsefulToken();
					switch (token.getType())
					{
						case ASTERISK:
							operator=token.getImage();
							state=State.EXPECTING_TERMINAL_MULTIPLY;
							break;
						case OPERATOR_DIV:
							operator=token.getImage();
							state=State.EXPECTING_TERMINAL_DIVIDE;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
					}
					break;
				case EXPECTING_TERMINAL_MULTIPLY:
					t=parseSignExpression(tokenizer);
					if (t != null)
					{
						expression=new MultiplyExpression(operator, expression, t);
						state=State.EXPECTING_OPERATOR;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				case EXPECTING_TERMINAL_DIVIDE:
					t=parseSignExpression(tokenizer);
					if (t != null)
					{
						expression=new DivideExpression(operator, expression, t);
						state=State.EXPECTING_OPERATOR;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}

	Expression parseAddingExpression(ParserTokenizer tokenizer)
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
					expression=parseMultiplyingExpression(tokenizer);
					state=State.EXPECTING_OPERATOR;
					break;
				case EXPECTING_OPERATOR:
					SqlToken token=tokenizer.nextUsefulToken();
					switch (token.getType())
					{
						case OPERATOR_PLUS:
							operator=token.getImage();
							state=State.EXPECTING_TERMINAL_PLUS;
							break;
						case OPERATOR_MINUS:
							operator=token.getImage();
							state=State.EXPECTING_TERMINAL_MINUS;
							break;
						case OPERATOR_CONCATENATION:
							operator=token.getImage();
							state=State.EXPECTING_TERMINAL_CONCATENATION;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
					}
					break;
				case EXPECTING_TERMINAL_PLUS:
					t=parseMultiplyingExpression(tokenizer);
					if (t != null)
					{
						expression=new AddExpression(operator, expression, t);
						state=State.EXPECTING_OPERATOR;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				case EXPECTING_TERMINAL_MINUS:
					t=parseMultiplyingExpression(tokenizer);
					if (t != null)
					{
						expression=new SubstractExpression(operator, expression, t);
						state=State.EXPECTING_OPERATOR;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				case EXPECTING_TERMINAL_CONCATENATION:
					t=parseMultiplyingExpression(tokenizer);
					if (t != null)
					{
						expression=new ConcatExpression(operator, expression, t);
						state=State.EXPECTING_OPERATOR;
					}
					else
					{
						throw new IncompleteExpressionException();
					}
					break;
				default:
					throw new IllegalStateException();
			}
		}
		return expression;
	}

	Expression parsePrefixedExpression(ParserTokenizer tokenizer, String prefix)
		throws ParserException
	{
		Expression expression=null;
		SqlToken token=tokenizer.nextToken();
		switch (token.getType())
		{
			case IDENTIFIER:
				expression=new IdentifierExpression(prefix, token.getImage());
				break;
			case ASTERISK:
				expression=new AsteriskExpression(prefix, token.getImage());
				break;
			default:
				throw new UnexpectedTokenException(token);
		}
		return expression;
	}
}
