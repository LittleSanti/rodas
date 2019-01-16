package com.samajackun.rodas.sql.parser;

import java.io.IOException;

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
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;

public final class ArithmeticExpressionParser extends AbstractParser<Expression>
{
	private static final ArithmeticExpressionParser INSTANCE=new ArithmeticExpressionParser();

	public static ArithmeticExpressionParser getInstance()
	{
		return ArithmeticExpressionParser.INSTANCE;
	}

	private ArithmeticExpressionParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, EXPECTING_OPERATOR, EXPECTING_TERMINAL, EXPECTING_TERMINAL_MULTIPLY, EXPECTING_TERMINAL_DIVIDE, EXPECTING_TERMINAL_PLUS, EXPECTING_TERMINAL_MINUS, EXPECTING_TERMINAL_CONCATENATION, EXPECTING_COMMA_OR_PAREHENTESIS_CLOSING
	}

	@Override
	public Expression parse(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		return parseAddingExpression(tokenizer);
	};

	Expression parseTerminal(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		// Reconocer expresiones constante e identificador
		Expression expression=null;
		ExpressionList expressionList=null;
		State state=State.INITIAL;
		do
		{
			SqlToken token=tokenizer.nextUsefulToken();
			if (token == null)
			{
				break;
			}
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case INTEGER_NUMBER_LITERAL:
							expression=new NumericConstantExpression(token.getValue(), Long.parseLong(token.getValue()));
							state=State.COMPLETE;
							break;
						case DECIMAL_NUMBER_LITERAL:
							expression=new NumericConstantExpression(token.getValue(), Double.parseDouble(token.getValue()));
							state=State.COMPLETE;
							break;
						case TEXT_LITERAL:
							expression=new TextConstantExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case KEYWORD_NULL:
							expression=new NullConstantExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case TRUE:
							expression=BooleanConstantExpression.createTrueConstrantExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case FALSE:
							expression=BooleanConstantExpression.createFalseConstrantExpression(token.getValue());
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
									FunctionExpression functionExpression=new FunctionExpression(token.getValue());
									functionExpression.getArguments().addAll(argumentsExpressions.getExpressions());
									tokenizer.matchToken(SqlToken.Type.PARENTHESIS_END);
									expression=functionExpression;
								}
								else if (token2.getType() == SqlToken.Type.PERIOD)
								{
									expression=parsePrefixedExpression(tokenizer, token.getValue());
								}
								else
								{
									expression=new IdentifierExpression(token.getValue());
									tokenizer.pushBack(token2);
								}
							}
							else
							{
								expression=new IdentifierExpression(token.getValue());
							}
							state=State.COMPLETE;
							break;
						case NAMED_PARAMETER:
							expression=new NamedParameterExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case PARENTHESIS_START:
							expression=parseSelectOrExpression(tokenizer);
							// tokenizer.matchToken(SqlToken.Type.PARENTHESIS_END);
							// expression=new ParehentesizedExpression(expression);
							state=State.EXPECTING_COMMA_OR_PAREHENTESIS_CLOSING;
							break;
						case ASTERISK:
							expression=new AsteriskExpression(token.getValue());
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
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		if (expression == null)
		{
			throw new ParserException("End of stream");
		}
		return expression;
	}

	private Expression parseSelectOrExpression(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		Expression expression;
		SqlToken sqlToken=tokenizer.nextToken();
		if (sqlToken == null)
		{
			throw new IncompleteExpressionException();
		}
		tokenizer.pushBack(sqlToken);
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

	Expression parseSignExpression(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		do
		{
			SqlToken token=tokenizer.nextToken();
			if (token != null)
			{
				switch (token.getType())
				{
					case OPERATOR_PLUS:
						expression=parseTerminal(tokenizer);
						expression=new UnitPlusExpression(token.getValue(), expression);
						break;
					case OPERATOR_MINUS:
						expression=parseTerminal(tokenizer);
						expression=new UnitMinusExpression(token.getValue(), expression);
						break;
					default:
						tokenizer.pushBack(token);
						expression=parseTerminal(tokenizer);
				}
			}
		}
		while (tokenizer.tokenWasRead() && expression == null);
		return expression;
	}

	Expression parseMultiplyingExpression(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		Expression t;
		do
		{
			switch (state)
			{
				case INITIAL:
					expression=parseSignExpression(tokenizer);
					state=State.EXPECTING_OPERATOR;
					break;
				case EXPECTING_OPERATOR:
					SqlToken token=tokenizer.nextUsefulToken();
					if (token != null)
					{
						switch (token.getType())
						{
							case ASTERISK:
								operator=token.getValue();
								state=State.EXPECTING_TERMINAL_MULTIPLY;
								break;
							case OPERATOR_DIV:
								operator=token.getValue();
								state=State.EXPECTING_TERMINAL_DIVIDE;
								break;
							default:
								tokenizer.pushBack(token);
								state=State.COMPLETE;
						}
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
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		return expression;
	}

	Expression parseAddingExpression(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		State state=State.INITIAL;
		String operator=null;
		Expression t;
		do
		{
			switch (state)
			{
				case INITIAL:
					expression=parseMultiplyingExpression(tokenizer);
					state=State.EXPECTING_OPERATOR;
					break;
				case EXPECTING_OPERATOR:
					SqlToken token=tokenizer.nextUsefulToken();
					if (token == null)
					{
						break;
					}
					switch (token.getType())
					{
						case OPERATOR_PLUS:
							operator=token.getValue();
							state=State.EXPECTING_TERMINAL_PLUS;
							break;
						case OPERATOR_MINUS:
							operator=token.getValue();
							state=State.EXPECTING_TERMINAL_MINUS;
							break;
						case OPERATOR_CONCATENATION:
							operator=token.getValue();
							state=State.EXPECTING_TERMINAL_CONCATENATION;
							break;
						default:
							tokenizer.pushBack(token);
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
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		return expression;
	}

	Expression parsePrefixedExpression(MatchingSqlTokenizer tokenizer, String prefix)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		SqlToken token=tokenizer.nextToken();
		if (token != null)
		{
			switch (token.getType())
			{
				case IDENTIFIER:
					expression=new IdentifierExpression(prefix, token.getValue());
					break;
				case ASTERISK:
					expression=new AsteriskExpression(prefix, token.getValue());
					break;
				default:
					throw new UnexpectedTokenException(token);
			}
		}
		return expression;
	}
}
