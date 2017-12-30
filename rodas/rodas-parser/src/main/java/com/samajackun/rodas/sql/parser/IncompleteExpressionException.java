package com.samajackun.rodas.sql.parser;

public class IncompleteExpressionException extends ParserException
{
	private static final long serialVersionUID=-203593152536876795L;

	public IncompleteExpressionException()
	{
		super("Incomplete expression");
	}
}
