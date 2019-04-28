package com.samajackun.rodas.parsing.parser;

import java.util.Arrays;

import com.samajackun.rodas.sql.tokenizer.Token;

public class UnexpectedTokenException extends ParserException
{
	private static final long serialVersionUID=-7580269662415995272L;

	private final Token token;

	private final String[] expected;

	public UnexpectedTokenException(Token token, String... expected)
	{
		super("Unexpected token " + token + (expected.length > 0
			? " instead of one of these: " + Arrays.asList(expected)
			: ""));
		this.token=token;
		this.expected=expected;
	}

	public Token getToken()
	{
		return this.token;
	}

	public String[] getExpected()
	{
		return this.expected;
	}
}
