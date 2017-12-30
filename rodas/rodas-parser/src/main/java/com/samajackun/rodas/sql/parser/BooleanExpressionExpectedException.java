package com.samajackun.rodas.sql.parser;

public class BooleanExpressionExpectedException extends ParserException
{
	private static final long serialVersionUID=-5723831627103444264L;

	public BooleanExpressionExpectedException(String location)
	{
		super("A boolean expression was expected " + location);
	}
}
