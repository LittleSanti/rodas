package com.samajackun.rodas.sql.parser.tokenizer;

import java.util.HashMap;
import java.util.Map;

import com.samajackun.rodas.sql.parser.tokenizer.SqlToken.Type;

public class SqlTokenizer extends AbstractTokenizer<SqlToken, SqlTokenizerSettings>
{
	private static final Map<String, SqlToken.Type> OPERATORS=createOperatorsMap();

	private static final Map<String, SqlToken.Type> KEYWORDS=createKeywordsMap();

	public SqlTokenizer(CharSequence src) throws TokenizerException
	{
		this(src, new SqlTokenizerSettings());
	}

	public SqlTokenizer(CharSequence src, SqlTokenizerSettings settings) throws TokenizerException
	{
		super(src, settings);
	}

	private static Map<String, Type> createKeywordsMap()
	{
		Map<String, Type> map=new HashMap<String, Type>(20);
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
		Map<String, Type> map=new HashMap<String, Type>(20);
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
	protected SqlToken fetch(Source source)
		throws TokenizerException
	{
		State state=State.INITIAL;
		SqlToken token=null;
		int initialIndex=source.getCurrentIndex();
		StringBuilder trailingText=new StringBuilder(80);

		while (token == null && source.getCurrentIndex() < source.getCharSequence().length())
		{
			char c=source.getCharSequence().charAt(source.getCurrentIndex());
			char c2=1 + source.getCurrentIndex() < source.getCharSequence().length()
				? source.getCharSequence().charAt(1 + source.getCurrentIndex())
				: (char)0;
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
						initialIndex=source.getCurrentIndex();
						state=State.READING_LETTERS;
					}
					else if (Character.isDigit(c))
					{
						initialIndex=source.getCurrentIndex();
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
							source.incCurrentIndex();
							initialIndex=source.getCurrentIndex() + 1;
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_DIV);
						}
					}
					else if (c == '\'')
					{
						initialIndex=source.getCurrentIndex();
						state=State.READING_TEXT_LITERAL;
					}
					else if (c == '\"')
					{
						initialIndex=source.getCurrentIndex();
						state=State.READING_DOUBLE_QUOTED_TEXT_LITERAL;
					}
					else if (c == ':')
					{
						initialIndex=1 + source.getCurrentIndex();
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
							token=SqlToken.TOKEN_DISTINCT2;
							source.incCurrentIndex();
						}
						else
						{
							throw new TokenizerException("Illegal symbol " + c);
						}
					}
					else if (c == '|')
					{
						if (c2 == '|')
						{
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_CONCATENATION);
							source.incCurrentIndex();
						}
						else
						{
							throw new TokenizerException("Illegal symbol " + c);
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
							token=SqlToken.TOKEN_DISTINCT1;
							source.incCurrentIndex();
						}
						else if (c2 == '=')
						{
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_LOWER_OR_EQUALS);
							source.incCurrentIndex();
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
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_GREATER_OR_EQUALS);
							source.incCurrentIndex();
						}
						else
						{
							token=createNewTokenIfNecessary(trailingText, SqlToken.TOKEN_GREATER);
						}
					}
					else
					{
						throw new TokenizerException("Unexpected symbol '" + c + "'");
					}
					break;
				case READING_COMMENT:
					if (c == '*' && c2 == '/')
					{
						switch (this.getSettings().getCommentsBehaviour())
						{
							case IGNORE:
								break;
							case PRODUCE_TOKENS:
								token=new SqlToken(Type.COMMENT, trailingText.toString() + "/*" + source.getCharSequence().subSequence(initialIndex, source.getCurrentIndex()).toString() + "*/");
								break;
							case INCLUDE_IN_FOLLOWING_TOKEN:
								trailingText.append("/*" + source.getCharSequence().subSequence(initialIndex, source.getCurrentIndex()).toString() + "*/");
								break;
						}
						state=State.INITIAL;
						source.incCurrentIndex();
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
						token=createTextToken(trailingText, source, initialIndex);
						source.decCurrentIndex();
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
						token=createToken(trailingText, source, SqlToken.Type.INTEGER_NUMBER_LITERAL, initialIndex);
						source.decCurrentIndex();
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
						throw new TokenizerException("Wrong number format: Double period in constant");
					}
					else
					{
						token=createToken(trailingText, source, SqlToken.Type.DECIMAL_NUMBER_LITERAL, initialIndex);
						source.decCurrentIndex();
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
						throw new TokenizerException("Wrong number format: Double E in constant");
					}
					else if (c == '.')
					{
						throw new TokenizerException("Wrong number format: Period not allowed in exponent");
					}
					else
					{
						token=createToken(trailingText, source, SqlToken.Type.DECIMAL_NUMBER_LITERAL, initialIndex);
						source.decCurrentIndex();
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
						throw new TokenizerException("Wrong number format: Double E in constant");
					}
					else if (c == '.')
					{
						throw new TokenizerException("Wrong number format: Period not allowed in exponent");
					}
					else
					{
						token=createToken(trailingText, source, SqlToken.Type.DECIMAL_NUMBER_LITERAL, initialIndex);
						source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_EXPONENT_DIGITS:
					if (c == '-' || c == '+')
					{
						// Seguir en este estado
					}
					else if (Character.isDigit(c))
					{
						state=State.READING_EXPONENT_DIGITS_WITHOUT_SIGN;
					}
					else if (c == 'E' || c == 'e')
					{
						throw new TokenizerException("Wrong number format: Double E in constant");
					}
					else if (c == '.')
					{
						throw new TokenizerException("Wrong number format: Period not allowed in exponent");
					}
					else
					{
						token=createToken(trailingText, source, SqlToken.Type.INTEGER_NUMBER_LITERAL, initialIndex);
						source.decCurrentIndex();
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
						throw new TokenizerException("Wrong number format: Double E in constant");
					}
					else if (c == '.')
					{
						throw new TokenizerException("Wrong number format: Period not allowed in exponent");
					}
					else
					{
						token=createToken(trailingText, source, SqlToken.Type.INTEGER_NUMBER_LITERAL, initialIndex);
						source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				case READING_TEXT_LITERAL:
					if (c == '\'')
					{
						if (c2 == '\'')
						{
							// Comilla escapeada:
							source.incCurrentIndex();
						}
						else
						{
							token=createToken(trailingText, source, SqlToken.Type.TEXT_LITERAL, initialIndex, 1 + source.getCurrentIndex());
							state=State.INITIAL;
						}
					}
					break;
				case READING_DOUBLE_QUOTED_TEXT_LITERAL:
					if (c == '\"')
					{
						token=createToken(trailingText, source, SqlToken.Type.DOUBLE_QUOTED_TEXT_LITERAL, 1 + initialIndex, source.getCurrentIndex());
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
						token=createToken(trailingText, source, SqlToken.Type.NAMED_PARAMETER, initialIndex, source.getCurrentIndex());
						source.decCurrentIndex();
						state=State.INITIAL;
					}
					break;
				default:
					// No hay más casos.
					break;
			}
			source.incCurrentIndex();
		}
		// Completar el resto, si queda:
		switch (state)
		{
			case READING_LETTERS:
				token=createTextToken(trailingText, source, initialIndex);
				break;
			case READING_INTEGER_DIGITS:
			case READING_EXPONENT_DIGITS_WITHOUT_SIGN:
				token=createToken(trailingText, source, SqlToken.Type.INTEGER_NUMBER_LITERAL, initialIndex);
				break;
			case READING_DECIMAL_DIGITS:
			case READING_DECIMAL_AND_EXPONENT_DIGITS_WITHOUT_SIGN:
				token=createToken(trailingText, source, SqlToken.Type.DECIMAL_NUMBER_LITERAL, initialIndex);
				break;
			case READING_NAMED_PARAMETER:
				token=createToken(trailingText, source, SqlToken.Type.NAMED_PARAMETER, initialIndex);
				break;
			case READING_COMMENT:
				throw new TokenizerException("Comment not closed");
			case READING_TEXT_LITERAL:
				throw new TokenizerException("Text literal not closed");
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

	private SqlToken createToken(StringBuilder text, Source source, SqlToken.Type type, int initialIndex, int endIndex)
	{
		return new SqlToken(type, text.toString() + source.getCharSequence().subSequence(initialIndex, endIndex).toString());
	}

	private SqlToken createToken(StringBuilder text, Source source, SqlToken.Type type, int initialIndex)
	{
		return createToken(text, source, type, initialIndex, source.getCurrentIndex());
	}

	private SqlToken createTextToken(StringBuilder leftText, Source source, int initialIndex)
	{
		String text=source.getCharSequence().subSequence(initialIndex, source.getCurrentIndex()).toString();
		// Decide si se trata de una palabra reservada, un operador, o un identificador:
		SqlToken.Type type=KEYWORDS.get(text.toUpperCase());
		if (type == null)
		{
			type=OPERATORS.get(text.toUpperCase());
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
}
