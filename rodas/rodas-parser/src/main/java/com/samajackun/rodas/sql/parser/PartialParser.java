package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;

public interface PartialParser
{
	public abstract Expression parse(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException;

}
