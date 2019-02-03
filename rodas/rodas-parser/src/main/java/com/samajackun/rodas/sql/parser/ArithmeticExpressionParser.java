package com.samajackun.rodas.sql.parser;

public final class ArithmeticExpressionParser extends GenericArithmeticExpressionParser
{
	private static final ArithmeticExpressionParser INSTANCE=new ArithmeticExpressionParser();

	public static ArithmeticExpressionParser getInstance()
	{
		return ArithmeticExpressionParser.INSTANCE;
	}

	private ArithmeticExpressionParser()
	{
		super(DefaultParserFactory.getInstance());
	}
}
