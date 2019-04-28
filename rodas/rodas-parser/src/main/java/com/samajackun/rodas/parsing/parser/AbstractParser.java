package com.samajackun.rodas.parsing.parser;

import java.io.IOException;

import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.parser.ParserFactory;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;

public abstract class AbstractParser<T>
{
	private final ParserFactory parserFactory;

	protected AbstractParser(ParserFactory parserFactory)
	{
		super();
		this.parserFactory=parserFactory;
	}

	public abstract T parse(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException;

	protected ParserFactory getParserFactory()
	{
		return this.parserFactory;
	}
}
