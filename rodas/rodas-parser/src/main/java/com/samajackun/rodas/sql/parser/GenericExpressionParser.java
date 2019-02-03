package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;

public class GenericExpressionParser extends AbstractParser<Expression> implements PartialParser
{
	public GenericExpressionParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	@Override
	public Expression parse(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		// return SelectSentenceParser.getInstance().parse(tokenizer);
		return getParserFactory().getLogicalExpressionParser().parse(tokenizer);
	};

}