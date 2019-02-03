package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.source.Source;

public class UnexpectedSymbolException extends TokenizerException
{
	private static final long serialVersionUID=-2680277882433158456L;

	private final char symbol;

	public UnexpectedSymbolException(Source source, char symbol)
	{
		super(source, "Unexpected symbol '" + symbol + "'");
		this.symbol=symbol;
	}

	public char getSymbol()
	{
		return this.symbol;
	}
}
