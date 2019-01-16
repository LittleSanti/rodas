package com.samajackun.rodas.parsing.parser;

import java.util.Arrays;

import com.samajackun.rodas.sql.tokenizer.SqlToken;

public class UnexpectedTokenException extends ParserException
{
	private static final long serialVersionUID=-7580269662415995272L;

	private final SqlToken token;

	private final SqlToken.Type[] expected;

	public UnexpectedTokenException(SqlToken token, SqlToken.Type... expected)
	{
		super("Unexpected token " + token + (expected.length > 0
			? " instead of one of these: " + Arrays.asList(expected)
			: ""));
		this.token=token;
		this.expected=expected;
	}

	public SqlToken getToken()
	{
		return this.token;
	}

	public SqlToken.Type[] getExpected()
	{
		return this.expected;
	}

}
