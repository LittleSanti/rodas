package com.samajackun.rodas.sql.parser.tokenizer;

public interface Tokenizer<T>
{
	public abstract boolean hasMoreTokens();

	public abstract T nextToken()
		throws TokenizerException;
}