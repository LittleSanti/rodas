package com.samajackun.rodas.sql.parser;

public class ExpressionCollectionParser extends GenericExpressionCollectionParser
{
	private static final ExpressionCollectionParser INSTANCE=new ExpressionCollectionParser();

	public static ExpressionCollectionParser getInstance()
	{
		return INSTANCE;
	}

	private ExpressionCollectionParser()
	{
		super(DefaultParserFactory.getInstance());
	}

}
