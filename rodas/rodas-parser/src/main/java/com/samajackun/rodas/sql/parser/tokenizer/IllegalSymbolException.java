package com.samajackun.rodas.sql.parser.tokenizer;

public class IllegalSymbolException extends TokenizerException
{
	private static final long serialVersionUID=4889554688583417210L;

	private final char symbol;

	public IllegalSymbolException(char symbol)
	{
		super("Illegal symbol '" + symbol + "'");
		this.symbol=symbol;
	}

	public char getSymbol()
	{
		return this.symbol;
	}
}
