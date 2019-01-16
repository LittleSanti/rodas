package com.samajackun.rodas.parsing.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.source.PushBackSource;

/**
 *
 * @author Santi
 * @version 2017.00
 * @param <T>
 * @param <S>
 */
public abstract class AbstractTokenizer<T, S> implements Tokenizer<T>
{
	private final S settings;

	private final PushBackSource source;

	// private T fetched;

	protected AbstractTokenizer(PushBackSource source, S settings) throws TokenizerException, IOException
	{
		super();
		this.source=source;
		this.settings=settings;
		// this.fetched=fetch(this.source);
	}

	public S getSettings()
	{
		return this.settings;
	}

	// @Override
	// public boolean hasMoreTokens()
	// {
	// return this.fetched != null;
	// }

	/*
	 * (non-Javadoc)
	 * @see dev.parser.Tokenizer#nextToken()
	 */
	@Override
	public T nextToken()
		throws TokenizerException,
		IOException
	{
		T fetched=fetch(this.source);
		return fetched;
	}

	protected abstract T fetch(PushBackSource source)
		throws TokenizerException,
		IOException;

	@Override
	public void pushBackToken(T token)
		throws IOException
	{
		pushBackToken(token, this.source);
	}

	protected abstract void pushBackToken(T token, PushBackSource source)
		throws IOException;
	//
	// protected PushBackSource getSource()
	// {
	// return this.source;
	// }
}
