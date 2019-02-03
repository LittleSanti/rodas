package com.samajackun.rodas.sql.parser;

public final class ExpressionParser extends GenericExpressionParser
{
	private static final ExpressionParser INSTANCE=new ExpressionParser();

	public static ExpressionParser getInstance()
	{
		return ExpressionParser.INSTANCE;
	}

	private ExpressionParser()
	{
		super(DefaultParserFactory.getInstance());
	}
}
