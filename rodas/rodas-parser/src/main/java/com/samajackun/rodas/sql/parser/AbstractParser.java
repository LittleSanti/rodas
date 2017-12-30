package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;

public abstract class AbstractParser<T>
{
	public abstract T parse(ParserTokenizer tokenizer)
		throws ParserException;
}
