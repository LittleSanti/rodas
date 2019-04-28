package com.samajackun.rodas.sql.tokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.AbstractTokenizer;
import com.samajackun.rodas.parsing.tokenizer.IllegalDoubleEException;
import com.samajackun.rodas.parsing.tokenizer.IllegalDoublePeriodException;
import com.samajackun.rodas.parsing.tokenizer.IllegalPeriodInExponentException;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.parsing.tokenizer.UnclosedCommentException;
import com.samajackun.rodas.parsing.tokenizer.UnclosedTextLiteralException;
import com.samajackun.rodas.parsing.tokenizer.UnexpectedSymbolException;

public class SqlTokenizer extends AbstractTokenizer
{
	private static final Map<String, String> OPERATORS=SqlTokenizer.createOperatorsMap();

	private static final Map<String, String> KEYWORDS=SqlTokenizer.createKeywordsMap();

	public SqlTokenizer(PushBackSource source) throws TokenizerException, IOException
	{
		this(source, new TokenizerSettings());
	}

	public SqlTokenizer(PushBackSource source, TokenizerSettings settings) throws TokenizerException, IOException
	{
		super(source, settings);
	}

	private static Map<String, String> createKeywordsMap()
	{
		Map<String, String> map=new HashMap<>(20);
		map.put("SELECT", SqlTokenTypes.KEYWORD_SELECT);
		map.put("FROM", SqlTokenTypes.KEYWORD_FROM);
		map.put("WHERE", SqlTokenTypes.KEYWORD_WHERE);
		map.put("GROUP", SqlTokenTypes.KEYWORD_GROUP);
		map.put("HAVING", SqlTokenTypes.KEYWORD_HAVING);
		map.put("ORDER", SqlTokenTypes.KEYWORD_ORDER);
		map.put("AS", SqlTokenTypes.KEYWORD_AS);
		map.put("BY", SqlTokenTypes.KEYWORD_BY);
		map.put("NULL", SqlTokenTypes.KEYWORD_NULL);
		map.put("TRUE", SqlTokenTypes.TRUE);
		map.put("FALSE", SqlTokenTypes.FALSE);
		map.put("ALL", SqlTokenTypes.KEYWORD_ALL);
		map.put("DISTINCT", SqlTokenTypes.KEYWORD_DISTINCT);
		map.put("DISTINCTROW", SqlTokenTypes.KEYWORD_DISTINCTROW);
		map.put("INNER", SqlTokenTypes.KEYWORD_INNER);
		map.put("OUTER", SqlTokenTypes.KEYWORD_OUTER);
		map.put("JOIN", SqlTokenTypes.KEYWORD_JOIN);
		map.put("LEFT", SqlTokenTypes.KEYWORD_LEFT);
		map.put("RIGHT", SqlTokenTypes.KEYWORD_RIGHT);
		map.put("ON", SqlTokenTypes.KEYWORD_ON);
		map.put("ASC", SqlTokenTypes.KEYWORD_ASC);
		map.put("DESC", SqlTokenTypes.KEYWORD_DESC);
		map.put("FOR", SqlTokenTypes.KEYWORD_FOR);
		map.put("UPDATE", SqlTokenTypes.KEYWORD_UPDATE);
		map.put("USING", SqlTokenTypes.KEYWORD_USING);
		map.put("WITH", SqlTokenTypes.KEYWORD_WITH);
		return map;
	}

	private static Map<String, String> createOperatorsMap()
	{
		Map<String, String> map=new HashMap<>(20);
		map.put("AND", SqlTokenTypes.OPERATOR_AND);
		map.put("OR", SqlTokenTypes.OPERATOR_OR);
		map.put("NOT", SqlTokenTypes.OPERATOR_NOT);
		map.put("IS", SqlTokenTypes.OPERATOR_IS);
		map.put("OF", SqlTokenTypes.OPERATOR_OF);
		map.put("TYPE", SqlTokenTypes.OPERATOR_TYPE);
		map.put("LIKE", SqlTokenTypes.OPERATOR_LIKE);
		map.put("IN", SqlTokenTypes.OPERATOR_IN);
		map.put("ANY", SqlTokenTypes.OPERATOR_ANY);
		map.put("SOME", SqlTokenTypes.OPERATOR_SOME);
		map.put("EXISTS", SqlTokenTypes.OPERATOR_EXISTS);
		map.put("PRIOR", SqlTokenTypes.OPERATOR_PRIOR);
		map.put("CONNECT_BY_ROOT", SqlTokenTypes.OPERATOR_CONNECT_BY_ROOT);
		map.put("BETWEEN", SqlTokenTypes.OPERATOR_BETWEEN);
		return map;
	}

	private enum State {
		INITIAL, READING_LETTERS, READING_INTEGER_DIGITS, READING_DECIMAL_DIGITS, READING_EXPONENT_DIGITS, READING_EXPONENT_DIGITS_WITHOUT_SIGN, READING_COMMENT, READING_TEXT_LITERAL, READING_DOUBLE_QUOTED_TEXT_LITERAL, READING_NAMED_PARAMETER, READING_DECIMAL_AND_EXPONENT_DIGITS, READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN
	};

	@Override
	protected Token fetch(PushBackSource source)
		throws TokenizerException,
		IOException
	{
		State state=State.INITIAL;
		Token token=null;
		// source.startRecord();
		StringBuilder trailingText=new StringBuilder(80);

		char c=0;
		boolean looping=true;
		while (token == null && source.hasMoreChars() && looping)
		{
			c=(char)source.nextChar();
			char c2=(char)source.lookahead();
			switch (state)
			{
				case INITIAL:
					if (Character.isWhitespace(c))
					{
						switch (this.getSettings().getWhitespaceBehaviour())
						{
							case IGNORE:
								break;
							case PRODUCE_TOKENS:
								token=new Token(SqlTokenTypes.WHITESPACE, trailingText.toString() + c);
								break;
							case INCLUDE_IN_FOLLOWING_TOKEN:
								trailingText.append(c);
								break;
						}
					}
					else if (Character.isLetter(c) || c == '$' || c == '_')
					{
						source.unget(c);
						source.startRecord();
						state=State.READING_LETTERS;
					}
					else if (Character.isDigit(c))
					{
						source.unget(c);
						source.startRecord();
						state=State.READING_INTEGER_DIGITS;
					}
					else if (c == '*')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_ASTERISK);
					}
					else if (c == '/')
					{
						if (c2 == '*')
						{
							state=State.READING_COMMENT;
							// source.incCurrentIndex();
							source.startRecord();
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_DIV);
						}
					}
					else if (c == '\'')
					{
						source.unget(c);
						source.startRecord();
						source.nextChar();
						state=State.READING_TEXT_LITERAL;
					}
					else if (c == '\"')
					{
						source.unget(c);
						source.startRecord();
						source.nextChar();
						state=State.READING_DOUBLE_QUOTED_TEXT_LITERAL;
					}
					else if (c == ':')
					{
						source.unget(c);
						source.startRecord();
						source.nextChar();
						state=State.READING_NAMED_PARAMETER;
					}
					else if (c == '?')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_UNNAMED_PARAMETER);
					}
					else if (c == '.')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_PERIOD);
					}
					else if (c == ',')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_COMMA);
					}
					else if (c == ';')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_SEMICOLON);
					}
					else if (c == '(')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_PAREHENTESES_START);
					}
					else if (c == ')')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_PAREHENTESES_END);
					}
					else if (c == '+')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_PLUS);
					}
					else if (c == '-')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_MINUS);
					}
					else if (c == '!')
					{
						if (c2 == '=')
						{
							source.nextChar();
							token=SqlTokens.TOKEN_DISTINCT2;
							// source.incCurrentIndex();
						}
						else
						{
							if (this.getSettings().getUnexpectedSymbolBehaviour() == TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)
							{
								throw new UnexpectedSymbolException(source, c);
							}
							else
							{
								looping=false;
								source.unget(c);
							}
						}
					}
					else if (c == '|')
					{
						if (c2 == '|')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_CONCATENATION);
							// source.incCurrentIndex();
						}
						else
						{
							if (this.getSettings().getUnexpectedSymbolBehaviour() == TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)
							{
								throw new UnexpectedSymbolException(source, c);
							}
							else
							{
								looping=false;
								source.unget(c);
							}
						}
					}
					else if (c == '=')
					{
						token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_EQUALS);
					}
					else if (c == '<')
					{
						if (c2 == '>')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_DISTINCT1);
						}
						else if (c2 == '=')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_LOWER_OR_EQUALS);
						}
						else if (c2 == '<')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_LOWER_LOWER);
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_LOWER);
						}
					}
					else if (c == '>')
					{
						if (c2 == '=')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_GREATER_OR_EQUALS);
						}
						else if (c2 == '>')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_GREATER_GREATER);
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SqlTokens.TOKEN_GREATER);
						}
					}
					else
					{
						if (this.getSettings().getUnexpectedSymbolBehaviour() == TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)
						{
							throw new UnexpectedSymbolException(source, c);
						}
						else
						{
							looping=false;
							source.unget(c);
						}
					}
					break;
				case READING_COMMENT:
					if (c == '*' && c2 == '/')
					{
						source.nextChar();
						switch (this.getSettings().getCommentsBehaviour())
						{
							case IGNORE:
								break;
							case PRODUCE_TOKENS:
								token=new Token(SqlTokenTypes.COMMENT, trailingText.toString() + "/" + source.endRecord().toString());
								break;
							case INCLUDE_IN_FOLLOWING_TOKEN:
								trailingText.append("/" + source.endRecord().toString());
								break;
						}
						state=State.INITIAL;
						// source.incCurrentIndex();
					}
					else
					{
						// Seguir en este estado.
					}
					break;
				case READING_LETTERS:
					if (Character.isLetterOrDigit(c) || c == '_' || c == '$')
					{
						// Seguir en este estado.
					}
					else
					{
						source.unget(c);
						token=createTextToken(trailingText, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_INTEGER_DIGITS:
					if (Character.isDigit(c))
					{
						// Seguir en este estado.
					}
					else if (c == 'E' || c == 'e')
					{
						state=State.READING_EXPONENT_DIGITS;
					}
					else if (c == '.')
					{
						state=State.READING_DECIMAL_DIGITS;
					}
					else
					{
						source.unget(c);
						token=createToken(trailingText, SqlTokenTypes.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_DECIMAL_DIGITS:
					if (Character.isDigit(c))
					{
						// Seguir en este estado.
					}
					else if (c == 'E' || c == 'e')
					{
						state=State.READING_DECIMAL_AND_EXPONENT_DIGITS;
					}
					else if (c == '.')
					{
						throw new IllegalDoublePeriodException(source);
					}
					else
					{
						token=createToken(trailingText, SqlTokenTypes.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_DECIMAL_AND_EXPONENT_DIGITS:
					if (c == '-' || c == '+')
					{
						// Seguir en este estado
					}
					else if (Character.isDigit(c))
					{
						state=State.READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN;
					}
					else if (c == 'E' || c == 'e')
					{
						throw new IllegalDoubleEException(source);
					}
					else if (c == '.')
					{
						throw new IllegalPeriodInExponentException(source);
					}
					else
					{
						token=createToken(trailingText, SqlTokenTypes.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN:
					if (Character.isDigit(c))
					{
						// Seguir en este estado.
					}
					else if (c == 'E' || c == 'e')
					{
						throw new IllegalDoubleEException(source);
					}
					else if (c == '.')
					{
						throw new IllegalPeriodInExponentException(source);
					}
					else
					{
						token=createToken(trailingText, SqlTokenTypes.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_EXPONENT_DIGITS:
					if (c == '-' || c == '+')
					{
						// Seguir en este estado: Podría haber varios signos seguidos y sería una notación válida.
					}
					else if (Character.isDigit(c))
					{
						state=State.READING_EXPONENT_DIGITS_WITHOUT_SIGN;
					}
					else if (c == 'E' || c == 'e')
					{
						throw new IllegalDoubleEException(source);
					}
					else if (c == '.')
					{
						throw new IllegalPeriodInExponentException(source);
					}
					else
					{
						token=createToken(trailingText, SqlTokenTypes.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_EXPONENT_DIGITS_WITHOUT_SIGN:
					if (Character.isDigit(c))
					{
						// Seguir en este estado.
					}
					else if (c == 'E' || c == 'e')
					{
						throw new IllegalDoubleEException(source);
					}
					else if (c == '.')
					{
						throw new IllegalPeriodInExponentException(source);
					}
					else
					{
						source.unget(c);
						token=createToken(trailingText, SqlTokenTypes.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_TEXT_LITERAL:
					if (c == '\'')
					{
						if (c2 == '\'')
						{
							// Comilla escapeada:
							source.nextChar();
						}
						else
						{
							String image=source.endRecord().toString();
							String value=image.substring(1, image.length() - 1).replaceAll("\'\'", "\'");
							token=createToken(trailingText, SqlTokenTypes.TEXT_LITERAL, image, value);
							state=State.INITIAL;
						}
					}
					break;
				case READING_DOUBLE_QUOTED_TEXT_LITERAL:
					if (c == '\"')
					{
						String image=source.endRecord().toString();
						token=createToken(trailingText, SqlTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL, image, image.substring(1, image.length() - 1));
						state=State.INITIAL;
					}
					break;
				case READING_NAMED_PARAMETER:
					if (Character.isLetterOrDigit(c) || c == '_' || c == '$')
					{
						// Seguir en este estado.
					}
					else
					{
						source.unget(c);
						String image=source.endRecord().toString();
						token=createToken(trailingText, SqlTokenTypes.NAMED_PARAMETER, image, image.substring(1));
						// source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				default:
					// No hay más casos.
					break;
			}
			// source.incCurrentIndex();
		}
		// Completar el resto, si queda:
		switch (state)
		{
			case READING_LETTERS:
				token=createTextToken(trailingText, source.endRecord().toString());
				break;
			case READING_INTEGER_DIGITS:
			case READING_EXPONENT_DIGITS_WITHOUT_SIGN:
				token=createToken(trailingText, SqlTokenTypes.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
				break;
			case READING_DECIMAL_DIGITS:
			case READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN:
				token=createToken(trailingText, SqlTokenTypes.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
				break;
			case READING_NAMED_PARAMETER:
				String image=source.endRecord().toString();
				token=createToken(trailingText, SqlTokenTypes.NAMED_PARAMETER, image, image.substring(1));
				break;
			case READING_COMMENT:
				throw new UnclosedCommentException(source);
			case READING_TEXT_LITERAL:
				throw new UnclosedTextLiteralException(source);
			case INITIAL:
			default:
				// Ignorar.
		}
		return token;
	}

	protected Token createNewTokenIfNecessary(StringBuilder text, Token token)
	{
		return text.length() == 0
			? token
			: new Token(token.getType(), text.toString());
	}

	// private Token createToken(StringBuilder text, Source source, int type, int initialIndex, int endIndex)
	// {
	// return new Token(type, text.toString() + source.endRecord().toString());
	// }

	protected Token createToken(StringBuilder trailingText, String type, String image, String value)
	{
		return new Token(type, trailingText.toString() + image, value);
	}

	protected Token createToken(StringBuilder trailingText, String type, String text)
	{
		return new Token(type, trailingText.toString() + text);
	}

	protected Token createTextToken(StringBuilder leftText, String text)
	{
		// Decide si se trata de una palabra reservada, un operador, o un identificador:
		String type=lookupKeywordOrOperator(text.toUpperCase());
		if (type == null)
		{
			type=SqlTokenTypes.IDENTIFIER;
		}
		if (leftText.length() > 0)
		{
			text=leftText.toString() + text;
		}
		return new Token(type, text);
	}

	protected String lookupKeywordOrOperator(String text)
	{
		// Decide si se trata de una palabra reservada o un operador:
		String type=KEYWORDS.get(text.toUpperCase());
		if (type == null)
		{
			type=OPERATORS.get(text.toUpperCase());
		}
		return type;
	}
}
