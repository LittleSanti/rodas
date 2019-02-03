package com.samajackun.rodas.sql.parser;

public final class LogicalExpressionParser extends GenericLogicalExpressionParser
{
	private static final LogicalExpressionParser INSTANCE=new LogicalExpressionParser();

	public static LogicalExpressionParser getInstance()
	{
		return LogicalExpressionParser.INSTANCE;
	}

	private LogicalExpressionParser()
	{
		super(DefaultParserFactory.getInstance());
	}
}
