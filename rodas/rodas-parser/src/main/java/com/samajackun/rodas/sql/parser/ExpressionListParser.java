package com.samajackun.rodas.sql.parser;

public final class ExpressionListParser extends GenericExpressionListParser
{
	private static final ExpressionListParser INSTANCE=new ExpressionListParser();

	public static ExpressionListParser getInstance()
	{
		return ExpressionListParser.INSTANCE;
	}

	private ExpressionListParser()
	{
		super(DefaultParserFactory.getInstance());
	}
}
