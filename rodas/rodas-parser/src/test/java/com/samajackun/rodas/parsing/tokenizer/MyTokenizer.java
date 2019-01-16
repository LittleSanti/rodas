package com.samajackun.rodas.parsing.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.source.PushBackSource;

class MyTokenizer extends AbstractTokenizer<String, MySettings>
{
	protected MyTokenizer(PushBackSource source, MySettings settings) throws TokenizerException, IOException
	{
		super(source, settings);
	}

	@Override
	protected String fetch(PushBackSource source)
		throws TokenizerException,
		IOException
	{
		int x=source.nextChar();
		return x < 0
			? null
			: String.valueOf((char)x);
	}

	@Override
	protected void pushBackToken(String token, PushBackSource source)
		throws IOException
	{
		source.unget(token);
	}
}