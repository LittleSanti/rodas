package com.samajackun.rodas.parsing.parser;

import java.io.IOException;

import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;

public abstract class AbstractParser<T>
{
	public abstract T parse(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException;
}
