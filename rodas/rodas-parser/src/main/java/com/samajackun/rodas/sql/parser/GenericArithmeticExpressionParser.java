package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.AddExpression;
import com.samajackun.rodas.core.model.AsteriskExpression;
import com.samajackun.rodas.core.model.BooleanConstantExpression;
import com.samajackun.rodas.core.model.ConcatExpression;
import com.samajackun.rodas.core.model.DivideExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionCollection;
import com.samajackun.rodas.core.model.FunctionCallExpression;
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
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;

public class GenericArithmeticExpressionParser extends AbstractParser<Expression> implements PartialParser
{
	private enum State {
		INITIAL, COMPLETE, EXPECTING_OPERATOR, EXPECTING_TERMINAL, EXPECTING_TERMINAL_MULTIPLY, EXPECTING_TERMINAL_DIVIDE, EXPECTING_TERMINAL_PLUS, EXPECTING_TERMINAL_MINUS, EXPECTING_TERMINAL_CONCATENATION, EXPECTING_COMMA_OR_PAREHENTESIS_CLOSING
	}

	public GenericArithmeticExpressionParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	@Override
	public Expression parse(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		return parseAddingExpression(tokenizer, parserContext);
	};

	protected Expression parseTerminal(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		// Reconocer expresiones constante e identificador
		Expression expression=null;
		ExpressionCollection expressionList=null;
		State state=State.INITIAL;
		do
		{
			Token token=tokenizer.nextOptionalUsefulToken();
			if (token == null)
			{
				break;
			}
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case SqlTokenTypes.INTEGER_NUMBER_LITERAL:
							expression=new NumericConstantExpression(token.getValue(), Long.parseLong(token.getValue()));
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.DECIMAL_NUMBER_LITERAL:
							expression=new NumericConstantExpression(token.getValue(), Double.parseDouble(token.getValue()));
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.TEXT_LITERAL:
							expression=new TextConstantExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.KEYWORD_NULL:
							expression=new NullConstantExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.TRUE:
							expression=BooleanConstantExpression.createTrueConstrantExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.FALSE:
							expression=BooleanConstantExpression.createFalseConstrantExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.IDENTIFIER:
							// Lookahead
							Token token2=tokenizer.nextOptionalUsefulToken();
							if (token2 != null)
							{
								if (token2.getType().equals(SqlTokenTypes.PARENTHESIS_START))
								{
									ExpressionCollection argumentsExpressions=getParserFactory().getExpressionCollectionParser().parse(tokenizer, parserContext);
									FunctionCallExpression functionExpression=new FunctionCallExpression(new IdentifierExpression(token.getValue()), argumentsExpressions.getExpressions());
									tokenizer.matchToken(SqlTokenTypes.PARENTHESIS_END);
									expression=functionExpression;
								}
								else if (token2.getType().equals(SqlTokenTypes.PERIOD))
								{
									expression=parsePrefixedExpression(tokenizer, token.getValue());
								}
								else
								{
									expression=unexpectedTokenAfterIdentifier(tokenizer, parserContext, token, token2);
									if (expression == null)
									{
										expression=new IdentifierExpression(token.getValue());
										tokenizer.pushBack(token2);
									}
								}
							}
							else
							{
								expression=new IdentifierExpression(token.getValue());
							}
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.NAMED_PARAMETER:
							expression=new NamedParameterExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.PARENTHESIS_START:
							expression=parseSelectOrExpression(tokenizer, parserContext);
							// tokenizer.matchToken(SqlToken.Type.PARENTHESIS_END);
							// expression=new ParehentesizedExpression(expression);
							state=State.EXPECTING_COMMA_OR_PAREHENTESIS_CLOSING;
							break;
						case SqlTokenTypes.ASTERISK:
							expression=new AsteriskExpression(token.getValue());
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.KEYWORD_SELECT:
							tokenizer.pushBack(token);
							// TODO Aquí hay que comprobar si SelectSentenceParser podría necesitar delgar sobre SumaParser,
							// si dentro de la select aparecieran fórmulas sintácticas de SumaScript.
							expression=SelectSentenceParser.getInstance().parse(tokenizer, parserContext);
							state=State.COMPLETE;
							break;
						default:
							expression=unexpectedToken(tokenizer, parserContext, token);
							state=State.COMPLETE;
							break;
					}
					break;
				case EXPECTING_COMMA_OR_PAREHENTESIS_CLOSING:
					switch (token.getType())
					{
						case SqlTokenTypes.PARENTHESIS_END:
							expression=new ParehentesizedExpression(expression);
							state=State.COMPLETE;
							break;
						case SqlTokenTypes.COMMA:
							if (expressionList == null)
							{
								expressionList=new ExpressionCollection();
								expressionList.add(expression);
							}
							expression=expressionList;
							Expression t=getParserFactory().getExpressionParser().parse(tokenizer, parserContext);
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

	protected Expression unexpectedToken(AbstractMatchingTokenizer tokenizer, ParserContext parserContext, Token token)
		throws ParserException
	{
		throw new UnexpectedTokenException(token);
	}

	protected Expression unexpectedTokenAfterIdentifier(AbstractMatchingTokenizer tokenizer, ParserContext parserContext, Token identifierToken, Token unexpectedToken)
		throws ParserException,
		IOException
	{
		Expression expression=new IdentifierExpression(identifierToken.getValue());
		tokenizer.pushBack(unexpectedToken);
		return expression;
	}

	private Expression parseSelectOrExpression(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		Expression expression;
		Token sqlToken=tokenizer.nextToken();
		if (sqlToken == null)
		{
			throw new IncompleteExpressionException();
		}
		tokenizer.pushBack(sqlToken);
		switch (sqlToken.getType())
		{
			case SqlTokenTypes.KEYWORD_SELECT:
				expression=getParserFactory().getSelectSentenceParser().parse(tokenizer, parserContext);
				break;
			default:
				expression=getParserFactory().getExpressionParser().parse(tokenizer, parserContext);
		}
		return expression;
	}

	protected Expression parseSignExpression(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		do
		{
			Token token=tokenizer.nextToken();
			if (token != null)
			{
				switch (token.getType())
				{
					case SqlTokenTypes.OPERATOR_PLUS:
						expression=parseTerminal(tokenizer, parserContext);
						expression=new UnitPlusExpression(token.getValue(), expression);
						break;
					case SqlTokenTypes.OPERATOR_MINUS:
						expression=parseTerminal(tokenizer, parserContext);
						expression=new UnitMinusExpression(token.getValue(), expression);
						break;
					default:
						tokenizer.pushBack(token);
						expression=parseTerminal(tokenizer, parserContext);
				}
			}
		}
		while (tokenizer.tokenWasRead() && expression == null);
		return expression;
	}

	protected Expression parseMultiplyingExpression(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
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
					expression=parseSignExpression(tokenizer, parserContext);
					state=State.EXPECTING_OPERATOR;
					break;
				case EXPECTING_OPERATOR:
					Token token=tokenizer.nextOptionalUsefulToken();
					if (token != null)
					{
						switch (token.getType())
						{
							case SqlTokenTypes.ASTERISK:
								operator=token.getValue();
								state=State.EXPECTING_TERMINAL_MULTIPLY;
								break;
							case SqlTokenTypes.OPERATOR_DIV:
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
					t=parseSignExpression(tokenizer, parserContext);
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
					t=parseSignExpression(tokenizer, parserContext);
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

	protected Expression parseAddingExpression(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
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
					expression=parseMultiplyingExpression(tokenizer, parserContext);
					state=State.EXPECTING_OPERATOR;
					break;
				case EXPECTING_OPERATOR:
					Token token=tokenizer.nextOptionalUsefulToken();
					if (token == null)
					{
						break;
					}
					switch (token.getType())
					{
						case SqlTokenTypes.OPERATOR_PLUS:
							operator=token.getValue();
							state=State.EXPECTING_TERMINAL_PLUS;
							break;
						case SqlTokenTypes.OPERATOR_MINUS:
							operator=token.getValue();
							state=State.EXPECTING_TERMINAL_MINUS;
							break;
						case SqlTokenTypes.OPERATOR_CONCATENATION:
							operator=token.getValue();
							state=State.EXPECTING_TERMINAL_CONCATENATION;
							break;
						default:
							tokenizer.pushBack(token);
							state=State.COMPLETE;
					}
					break;
				case EXPECTING_TERMINAL_PLUS:
					t=parseMultiplyingExpression(tokenizer, parserContext);
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
					t=parseMultiplyingExpression(tokenizer, parserContext);
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
					t=parseMultiplyingExpression(tokenizer, parserContext);
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

	protected Expression parsePrefixedExpression(AbstractMatchingTokenizer tokenizer, String prefix)
		throws ParserException,
		IOException
	{
		Expression expression=null;
		Token token=tokenizer.nextToken();
		if (token != null)
		{
			switch (token.getType())
			{
				case SqlTokenTypes.IDENTIFIER:
					expression=new IdentifierExpression(prefix, token.getValue());
					break;
				case SqlTokenTypes.ASTERISK:
					expression=new AsteriskExpression(prefix, token.getValue());
					break;
				default:
					throw new UnexpectedTokenException(token);
			}
		}
		return expression;
	}
}
