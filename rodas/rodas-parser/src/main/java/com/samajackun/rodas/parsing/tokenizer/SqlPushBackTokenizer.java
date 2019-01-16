package com.samajackun.rodas.sql.parser.tokenizer;

public class PushBackTokenizer<T> implements Tokenizer<T>
{
	private final Tokenizer<T> tokenizer;

	private T pendant;

	private T last;

	public PushBackTokenizer(Tokenizer<T> tokenizer)
	{
		super();
		this.tokenizer=tokenizer;
	}

	@Override
	public boolean hasMoreTokens()
	{
		return this.pendant != null || this.tokenizer.hasMoreTokens();
	}

	@Override
	public T nextToken()
		throws TokenizerException
	{
		if (this.pendant != null)
		{
			this.last=this.pendant;
			this.pendant=null;
		}
		else
		{
			this.last=this.tokenizer.nextToken();
		}
		return this.last;
	}

	public void pushBack()
	{
		if (this.pendant != null)
		{
			throw new IllegalStateException();
		}
		this.pendant=this.last;
	}
}
