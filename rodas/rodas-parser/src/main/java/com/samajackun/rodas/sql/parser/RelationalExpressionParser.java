package com.samajackun.rodas.sql.parser;

public final class RelationalExpressionParser extends GenericRelationalExpressionParser
{
	private static final RelationalExpressionParser INSTANCE=new RelationalExpressionParser();

	public static RelationalExpressionParser getInstance()
	{
		return RelationalExpressionParser.INSTANCE;
	}

	private RelationalExpressionParser()
	{
		super(DefaultParserFactory.getInstance());
	}
}
