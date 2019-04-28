package com.samajackun.rodas.parsing.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings;

class MyTokenizer extends AbstractTokenizer
{
	private int t=0;

	protected MyTokenizer(PushBackSource source, TokenizerSettings settings) throws TokenizerException, IOException
	{
		super(source, settings);
	}

	@Override
	protected Token fetch(PushBackSource source)
		throws TokenizerException,
		IOException
	{
		int x=source.nextChar();
		this.t=(++this.t) % 2;
		return x < 0
			? null
			: new Token("type" + this.t, String.valueOf((char)x));
	}
}