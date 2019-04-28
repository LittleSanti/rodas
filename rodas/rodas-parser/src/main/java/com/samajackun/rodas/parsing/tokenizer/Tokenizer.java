package com.samajackun.rodas.parsing.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.Token;

public interface Tokenizer
{
	public abstract Token nextToken()
		throws TokenizerException,
		IOException;

	public abstract void pushBackToken(Token token)
		throws IOException;

	public PushBackSource getSource();
}
