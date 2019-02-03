package com.samajackun.rodas.sql.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.EndOfStreamException;
import com.samajackun.rodas.parsing.tokenizer.Tokenizer;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;

public abstract class AbstractMatchingTokenizer<T>
{
	private final Tokenizer<T> tokenizer;

	private T lastToken;

	private boolean lastTokenIsNotNull=true;

	public AbstractMatchingTokenizer(Tokenizer<T> tokenizer)
	{
		this.tokenizer=tokenizer;
	}

	public T nextOptionalUsefulToken()
		throws TokenizerException,
		IOException
	{
		T found=null;
		T token;
		do
		{
			token=nextOptionalToken();
			if (token != null && isUseful(token))
			{
				found=token;
			}
		}
		while (token != null && found == null);
		return found;
	}

	protected abstract boolean isUseful(T token);

	public T nextToken()
		throws TokenizerException,
		IOException
	{
		T token=nextOptionalToken();
		if (token == null)
		{
			throw new EndOfStreamException(this.tokenizer.getSource());
		}
		return token;
	}

	public T nextOptionalToken()
		throws TokenizerException,
		IOException
	{
		this.lastToken=this.tokenizer.nextToken();
		this.lastTokenIsNotNull=(this.lastToken != null);
		return this.lastToken;
	}

	public T nextUsefulToken()
		throws TokenizerException,
		IOException
	{
		T token=nextOptionalUsefulToken();
		if (token == null)
		{
			throw new EndOfStreamException(this.tokenizer.getSource());
		}
		return token;
	}

	public void pushBack(T token)
		throws IOException
	{
		this.tokenizer.pushBackToken(token);
	}

	public boolean tokenWasRead()
	{
		return this.lastTokenIsNotNull;
	}

	public PushBackSource getSource()
	{
		return this.tokenizer.getSource();
	}
}
