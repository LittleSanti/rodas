package com.samajackun.rodas.parsing.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.source.PushBackSource;

public interface Tokenizer<T>
{
	// public abstract boolean hasMoreTokens();

	public abstract T nextToken()
		throws TokenizerException,
		IOException;

	public abstract void pushBackToken(T token)
		throws IOException;

	public PushBackSource getSource();
}
