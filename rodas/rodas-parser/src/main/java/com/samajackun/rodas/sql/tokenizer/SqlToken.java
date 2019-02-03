package com.samajackun.rodas.sql.tokenizer;

public class SqlToken
{
	public enum Type {
		SEMICOLON, ASTERISK, OPERATOR_PLUS, OPERATOR_MINUS, OPERATOR_DIV, /* PERIOD, */COMMA, PARENTHESIS_START, PARENTHESIS_END, OPERATOR_EQUALS, OPERATOR_DISTINCT1, OPERATOR_DISTINCT2, OPERATOR_LOWER, OPERATOR_LOWER_OR_EQUALS, OPERATOR_GREATER, OPERATOR_GREATER_OR_EQUALS, OPERATOR_OR, OPERATOR_AND, OPERATOR_NOT, OPERATOR_IS, OPERATOR_LIKE, OPERATOR_IN, OPERATOR_ANY, OPERATOR_ALL, OPERATOR_SOME, OPERATOR_EXISTS, INTEGER_NUMBER_LITERAL, DECIMAL_NUMBER_LITERAL, TEXT_LITERAL, DOUBLE_QUOTED_TEXT_LITERAL, COMMENT, IDENTIFIER, KEYWORD_SELECT, KEYWORD_FROM, KEYWORD_WHERE, KEYWORD_GROUP, KEYWORD_HAVING, KEYWORD_ORDER, KEYWORD_AS, KEYWORD_BY, KEYWORD_ALL, KEYWORD_DISTINCT, KEYWORD_DISTINCTROW, KEYWORD_ASC, KEYWORD_DESC, KEYWORD_FOR, KEYWORD_INNER, KEYWORD_JOIN, KEYWORD_LEFT, KEYWORD_RIGHT, KEYWORD_ON, KEYWORD_OUTER, KEYWORD_UPDATE, KEYWORD_USING, KEYWORD_WITH, KEYWORD_NULL, NAMED_PARAMETER, UNNAMED_PARAMETER, OPERATOR_CONCATENATION, OPERATOR_PRIOR, OPERATOR_CONNECT_BY_ROOT, OPERATOR_BETWEEN, OPERATOR_OF, OPERATOR_TYPE, WHITESPACE, PERIOD, TRUE, FALSE, OPERATOR_GREATER_GREATER, OPERATOR_LOWER_LOWER
	};

	public static final SqlToken TOKEN_SEMICOLON=new SqlToken(Type.SEMICOLON, ";");

	public static final SqlToken TOKEN_ASTERISK=new SqlToken(Type.ASTERISK, "*");

	public static final SqlToken TOKEN_PLUS=new SqlToken(Type.OPERATOR_PLUS, "+");

	public static final SqlToken TOKEN_MINUS=new SqlToken(Type.OPERATOR_MINUS, "-");

	public static final SqlToken TOKEN_DIV=new SqlToken(Type.OPERATOR_DIV, "/");

	// public static final Token TOKEN_PERIOD=new Token(Type.PERIOD, ".");

	public static final SqlToken TOKEN_COMMA=new SqlToken(Type.COMMA, ",");

	public static final SqlToken TOKEN_PERIOD=new SqlToken(Type.PERIOD, ".");

	public static final SqlToken TOKEN_PAREHENTESES_START=new SqlToken(Type.PARENTHESIS_START, "(");

	public static final SqlToken TOKEN_PAREHENTESES_END=new SqlToken(Type.PARENTHESIS_END, ")");

	public static final SqlToken TOKEN_EQUALS=new SqlToken(Type.OPERATOR_EQUALS, "=");

	public static final SqlToken TOKEN_DISTINCT1=new SqlToken(Type.OPERATOR_DISTINCT1, "<>");

	public static final SqlToken TOKEN_DISTINCT2=new SqlToken(Type.OPERATOR_DISTINCT2, "!=");

	public static final SqlToken TOKEN_LOWER=new SqlToken(Type.OPERATOR_LOWER, "<");

	public static final SqlToken TOKEN_LOWER_OR_EQUALS=new SqlToken(Type.OPERATOR_LOWER_OR_EQUALS, "<=");

	public static final SqlToken TOKEN_GREATER=new SqlToken(Type.OPERATOR_GREATER, ">");

	public static final SqlToken TOKEN_GREATER_OR_EQUALS=new SqlToken(Type.OPERATOR_GREATER_OR_EQUALS, ">=");

	public static final SqlToken TOKEN_UNNAMED_PARAMETER=new SqlToken(Type.UNNAMED_PARAMETER, "?");

	public static final SqlToken TOKEN_CONCATENATION=new SqlToken(Type.OPERATOR_CONCATENATION, "||");

	public static final SqlToken TOKEN_GREATER_GREATER=new SqlToken(Type.OPERATOR_GREATER_GREATER, ">>");

	public static final SqlToken TOKEN_LOWER_LOWER=new SqlToken(Type.OPERATOR_LOWER_LOWER, "<<");

	private final Type type;

	private final String image;

	private final String value;

	private final boolean keyword;

	public SqlToken(Type type, String image, String value)
	{
		super();
		this.type=type;
		this.image=image;
		this.value=value;
		switch (type)
		{
			case KEYWORD_SELECT:
			case KEYWORD_FROM:
			case KEYWORD_WHERE:
			case KEYWORD_GROUP:
			case KEYWORD_HAVING:
			case KEYWORD_ORDER:
			case KEYWORD_AS:
			case KEYWORD_BY:
			case KEYWORD_ALL:
			case KEYWORD_DISTINCT:
			case KEYWORD_INNER:
			case KEYWORD_JOIN:
			case KEYWORD_LEFT:
			case KEYWORD_RIGHT:
			case KEYWORD_ON:
			case KEYWORD_OUTER:
			case KEYWORD_USING:
			case KEYWORD_DISTINCTROW:
			case KEYWORD_ASC:
			case KEYWORD_DESC:
			case KEYWORD_FOR:
			case KEYWORD_UPDATE:
			case KEYWORD_WITH:
				this.keyword=true;
				break;
			default:
				this.keyword=false;
		}
	}

	public SqlToken(Type type, String image)
	{
		this(type, image, image);
	}

	public Type getType()
	{
		return this.type;
	}

	public String getImage()
	{
		return this.image;
	}

	public boolean isKeyword()
	{
		return this.keyword;
	}

	public String getValue()
	{
		return this.value;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		SqlToken other=(SqlToken)obj;
		if (this.image == null)
		{
			if (other.image != null)
			{
				return false;
			}
		}
		else if (!this.image.equals(other.image))
		{
			return false;
		}
		if (this.type != other.type)
		{
			return false;
		}
		if (this.value == null)
		{
			if (other.value != null)
			{
				return false;
			}
		}
		else if (!this.value.equals(other.value))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "SqlToken [type=" + this.type + ", image=" + this.image + ", value=" + this.value + "]";
	}
}
