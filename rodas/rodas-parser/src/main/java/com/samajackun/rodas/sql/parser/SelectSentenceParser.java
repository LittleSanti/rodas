package com.samajackun.rodas.sql.parser;

public final class SelectSentenceParser extends GenericSelectSentenceParser
{
	private static final SelectSentenceParser INSTANCE=new SelectSentenceParser();

	public static SelectSentenceParser getInstance()
	{
		return SelectSentenceParser.INSTANCE;
	}

	private SelectSentenceParser()
	{
		super(DefaultParserFactory.getInstance());
	}
}
