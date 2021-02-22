package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.parsing.parser.ParserException;

public class RepeatedAliasException extends ParserException
{
	private static final long serialVersionUID=-3180761962474329213L;

	private final String alias;

	public RepeatedAliasException(String alias)
	{
		super("Repeated alias '" + alias + "'");
		this.alias=alias;
	}

	public String getAlias()
	{
		return this.alias;
	}
}
