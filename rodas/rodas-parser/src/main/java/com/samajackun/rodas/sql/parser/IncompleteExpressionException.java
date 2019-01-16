package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.parsing.parser.ParserException;

public class IncompleteExpressionException extends ParserException
{
	private static final long serialVersionUID=-203593152536876795L;

	public IncompleteExpressionException()
	{
		super("Incomplete expression");
	}
}
