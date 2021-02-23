package com.samajackun.rodas.sql.tokenizer;

public final class SqlTokens
{
	public static final Token TOKEN_ASTERISK=new Token(SqlTokenTypes.ASTERISK, "*");

	public static final Token TOKEN_COMMA=new Token(SqlTokenTypes.COMMA, ",");

	public static final Token TOKEN_CONCATENATION=new Token(SqlTokenTypes.OPERATOR_CONCATENATION, "||");

	public static final Token TOKEN_DISTINCT1=new Token(SqlTokenTypes.OPERATOR_DISTINCT, "<>");

	public static final Token TOKEN_DISTINCT2=new Token(SqlTokenTypes.OPERATOR_DISTINCT, "!=");

	public static final Token TOKEN_DIV=new Token(SqlTokenTypes.OPERATOR_DIV, "/");

	public static final Token TOKEN_EQUALS=new Token(SqlTokenTypes.OPERATOR_EQUALS, "=");

	public static final Token TOKEN_GREATER=new Token(SqlTokenTypes.OPERATOR_GREATER, ">");

	public static final Token TOKEN_GREATER_GREATER=new Token(SqlTokenTypes.OPERATOR_GREATER_GREATER, ">>");

	public static final Token TOKEN_GREATER_OR_EQUALS=new Token(SqlTokenTypes.OPERATOR_GREATER_OR_EQUALS, ">=");

	public static final Token TOKEN_LOWER=new Token(SqlTokenTypes.OPERATOR_LOWER, "<");

	public static final Token TOKEN_LOWER_LOWER=new Token(SqlTokenTypes.OPERATOR_LOWER_LOWER, "<<");

	public static final Token TOKEN_LOWER_OR_EQUALS=new Token(SqlTokenTypes.OPERATOR_LOWER_OR_EQUALS, "<=");

	public static final Token TOKEN_MINUS=new Token(SqlTokenTypes.OPERATOR_MINUS, "-");

	public static final Token TOKEN_PAREHENTESIS_END=new Token(SqlTokenTypes.PARENTHESIS_END, ")");

	public static final Token TOKEN_PAREHENTESIS_START=new Token(SqlTokenTypes.PARENTHESIS_START, "(");

	public static final Token TOKEN_PERIOD=new Token(SqlTokenTypes.PERIOD, ".");

	public static final Token TOKEN_PLUS=new Token(SqlTokenTypes.OPERATOR_PLUS, "+");

	public static final Token TOKEN_SEMICOLON=new Token(SqlTokenTypes.SEMICOLON, ";");

	public static final Token TOKEN_UNNAMED_PARAMETER=new Token(SqlTokenTypes.UNNAMED_PARAMETER, "?");

	private SqlTokens()
	{
	}

}
