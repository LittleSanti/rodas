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
import com.samajackun.rodas.sql.tokenizer.SqlToken.Type;

public class SqlTokenizer extends AbstractTokenizer<SqlToken, SqlTokenizerSettings>
{
	private static final Map<String, SqlToken.Type> OPERATORS=SqlTokenizer.createOperatorsMap();

	private static final Map<String, SqlToken.Type> KEYWORDS=SqlTokenizer.createKeywordsMap();

	public SqlTokenizer(PushBackSource source) throws TokenizerException, IOException
	{
		this(source, new SqlTokenizerSettings());
	}

	public SqlTokenizer(PushBackSource source, SqlTokenizerSettings settings) throws TokenizerException, IOException
	{
		super(source, settings);
	}

	private static Map<String, Type> createKeywordsMap()
	{
		Map<String, Type> map=new HashMap<>(20);
		map.put("SELECT", SqlToken.Type.KEYWORD_SELECT);
		map.put("FROM", SqlToken.Type.KEYWORD_FROM);
		map.put("WHERE", SqlToken.Type.KEYWORD_WHERE);
		map.put("GROUP", SqlToken.Type.KEYWORD_GROUP);
		map.put("HAVING", SqlToken.Type.KEYWORD_HAVING);
		map.put("ORDER", SqlToken.Type.KEYWORD_ORDER);
		map.put("AS", SqlToken.Type.KEYWORD_AS);
		map.put("BY", SqlToken.Type.KEYWORD_BY);
		map.put("NULL", SqlToken.Type.KEYWORD_NULL);
		map.put("TRUE", SqlToken.Type.TRUE);
		map.put("FALSE", SqlToken.Type.FALSE);
		map.put("ALL", SqlToken.Type.KEYWORD_ALL);
		map.put("DISTINCT", SqlToken.Type.KEYWORD_DISTINCT);
		map.put("DISTINCTROW", SqlToken.Type.KEYWORD_DISTINCTROW);
		map.put("INNER", SqlToken.Type.KEYWORD_INNER);
		map.put("OUTER", SqlToken.Type.KEYWORD_OUTER);
		map.put("JOIN", SqlToken.Type.KEYWORD_JOIN);
		map.put("LEFT", SqlToken.Type.KEYWORD_LEFT);
		map.put("RIGHT", SqlToken.Type.KEYWORD_RIGHT);
		map.put("ON", SqlToken.Type.KEYWORD_ON);
		map.put("ASC", SqlToken.Type.KEYWORD_ASC);
		map.put("DESC", SqlToken.Type.KEYWORD_DESC);
		map.put("FOR", SqlToken.Type.KEYWORD_FOR);
		map.put("UPDATE", SqlToken.Type.KEYWORD_UPDATE);
		map.put("USING", SqlToken.Type.KEYWORD_USING);
		map.put("WITH", SqlToken.Type.KEYWORD_WITH);
		return map;
	}

	private static Map<String, Type> createOperatorsMap()
	{
		Map<String, Type> map=new HashMap<>(20);
		map.put("AND", SqlToken.Type.OPERATOR_AND);
		map.put("OR", SqlToken.Type.OPERATOR_OR);
		map.put("NOT", SqlToken.Type.OPERATOR_NOT);
		map.put("IS", SqlToken.Type.OPERATOR_IS);
		map.put("OF", SqlToken.Type.OPERATOR_OF);
		map.put("TYPE", SqlToken.Type.OPERATOR_TYPE);
		map.put("LIKE", SqlToken.Type.OPERATOR_LIKE);
		map.put("IN", SqlToken.Type.OPERATOR_IN);
		map.put("ANY", SqlToken.Type.OPERATOR_ANY);
		map.put("SOME", SqlToken.Type.OPERATOR_SOME);
		map.put("EXISTS", SqlToken.Type.OPERATOR_EXISTS);
		map.put("PRIOR", SqlToken.Type.OPERATOR_PRIOR);
		map.put("CONNECT_BY_ROOT", SqlToken.Type.OPERATOR_CONNECT_BY_ROOT);
		map.put("BETWEEN", SqlToken.Type.OPERATOR_BETWEEN);
		return map;
	}

	private enum State {
		INITIAL, READING_LETTERS, READING_INTEGER_DIGITS, READING_DECIMAL_DIGITS, READING_EXPONENT_DIGITS, READING_EXPONENT_DIGITS_WITHOUT_SIGN, READING_COMMENT, READING_TEXT_LITERAL, READING_DOUBLE_QUOTED_TEXT_LITERAL, READING_NAMED_PARAMETER, READING_DECIMAL_AND_EXPONENT_DIGITS, READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN
	};

	@Override
	protected SqlToken fetch(PushBackSource source)
		throws TokenizerException,
		IOException
	{
		State state=State.INITIAL;
		SqlToken token=null;
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
								token=new SqlToken(Type.WHITESPACE, trailingText.toString() + c);
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
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_ASTERISK);
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
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_DIV);
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
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_UNNAMED_PARAMETER);
					}
					else if (c == '.')
					{
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_PERIOD);
					}
					else if (c == ',')
					{
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_COMMA);
					}
					else if (c == ';')
					{
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_SEMICOLON);
					}
					else if (c == '(')
					{
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_PAREHENTESES_START);
					}
					else if (c == ')')
					{
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_PAREHENTESES_END);
					}
					else if (c == '+')
					{
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_PLUS);
					}
					else if (c == '-')
					{
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_MINUS);
					}
					else if (c == '!')
					{
						if (c2 == '=')
						{
							source.nextChar();
							token=SqlToken.TOKEN_DISTINCT2;
							// source.incCurrentIndex();
						}
						else
						{
							if (this.getSettings().getUnexpectedSymbolBehaviour() == SqlTokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)
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
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_CONCATENATION);
							// source.incCurrentIndex();
						}
						else
						{
							if (this.getSettings().getUnexpectedSymbolBehaviour() == SqlTokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)
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
						token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_EQUALS);
					}
					else if (c == '<')
					{
						if (c2 == '>')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_DISTINCT1);
						}
						else if (c2 == '=')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_LOWER_OR_EQUALS);
						}
						else if (c2 == '<')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_LOWER_LOWER);
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_LOWER);
						}
					}
					else if (c == '>')
					{
						if (c2 == '=')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_GREATER_OR_EQUALS);
						}
						else if (c2 == '>')
						{
							source.nextChar();
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_GREATER_GREATER);
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_GREATER);
						}
					}
					else
					{
						if (this.getSettings().getUnexpectedSymbolBehaviour() == SqlTokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)
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
								token=new SqlToken(Type.COMMENT, trailingText.toString() + "/" + source.endRecord().toString());
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
						token=createToken(trailingText, SqlToken.Type.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
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
						token=createToken(trailingText, SqlToken.Type.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
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
						token=createToken(trailingText, SqlToken.Type.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
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
						token=createToken(trailingText, SqlToken.Type.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
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
						token=createToken(trailingText, SqlToken.Type.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
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
						token=createToken(trailingText, SqlToken.Type.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
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
							token=createToken(trailingText, SqlToken.Type.TEXT_LITERAL, image, value);
							state=State.INITIAL;
						}
					}
					break;
				case READING_DOUBLE_QUOTED_TEXT_LITERAL:
					if (c == '\"')
					{
						String image=source.endRecord().toString();
						token=createToken(trailingText, SqlToken.Type.DOUBLE_QUOTED_TEXT_LITERAL, image, image.substring(1, image.length() - 1));
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
						token=createToken(trailingText, SqlToken.Type.NAMED_PARAMETER, image, image.substring(1));
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
				token=createToken(trailingText, SqlToken.Type.INTEGER_NUMBER_LITERAL, source.endRecord().toString());
				break;
			case READING_DECIMAL_DIGITS:
			case READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN:
				token=createToken(trailingText, SqlToken.Type.DECIMAL_NUMBER_LITERAL, source.endRecord().toString());
				break;
			case READING_NAMED_PARAMETER:
				String image=source.endRecord().toString();
				token=createToken(trailingText, SqlToken.Type.NAMED_PARAMETER, image, image.substring(1));
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

	private SqlToken createNewTokenIfNecessary(StringBuilder text, SqlToken token)
	{
		return text.length() == 0
			? token
			: new SqlToken(token.getType(), text.toString());
	}

	// private SqlToken createToken(StringBuilder text, Source source, SqlToken.Type type, int initialIndex, int endIndex)
	// {
	// return new SqlToken(type, text.toString() + source.endRecord().toString());
	// }

	private SqlToken createToken(StringBuilder trailingText, SqlToken.Type type, String image, String value)
	{
		return new SqlToken(type, trailingText.toString() + image, value);
	}

	private SqlToken createToken(StringBuilder trailingText, SqlToken.Type type, String text)
	{
		return new SqlToken(type, trailingText.toString() + text);
	}

	private SqlToken createTextToken(StringBuilder leftText, String text)
	{
		// Decide si se trata de una palabra reservada, un operador, o un identificador:
		SqlToken.Type type=SqlTokenizer.KEYWORDS.get(text.toUpperCase());
		if (type == null)
		{
			type=SqlTokenizer.OPERATORS.get(text.toUpperCase());
			if (type == null)
			{
				type=SqlToken.Type.IDENTIFIER;
			}
		}
		if (leftText.length() > 0)
		{
			text=leftText.toString() + text;
		}
		return new SqlToken(type, text);
	}

	@Override
	protected void pushBackToken(SqlToken token, PushBackSource source)
		throws IOException
	{
		source.unget(token.getImage());
	}

}
