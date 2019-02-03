package com.samajackun.rodas.sql.parser;

public final class ComparisonExpressionParser extends GenericComparisonExpressionParser
{
	private static final ComparisonExpressionParser INSTANCE=new ComparisonExpressionParser();

	public static ComparisonExpressionParser getInstance()
	{
		return ComparisonExpressionParser.INSTANCE;
	}

	private ComparisonExpressionParser()
	{
		super(DefaultParserFactory.getInstance());
	}
}
