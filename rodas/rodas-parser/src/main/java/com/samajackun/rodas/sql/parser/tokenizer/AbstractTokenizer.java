package com.samajackun.rodas.sql.parser.tokenizer;

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

	private final Source source;

	private T fetched;

	protected AbstractTokenizer(CharSequence src, S settings) throws TokenizerException
	{
		super();
		this.source=new Source(src);
		this.settings=settings;
		this.fetched=fetch(this.source);
	}

	public S getSettings()
	{
		return this.settings;
	}

	/*
	 * (non-Javadoc)
	 * @see dev.parser.Tokenizer#hasMoreTokens()
	 */
	@Override
	public boolean hasMoreTokens()
	{
		return this.fetched != null;
	}

	/*
	 * (non-Javadoc)
	 * @see dev.parser.Tokenizer#nextToken()
	 */
	@Override
	public T nextToken()
		throws TokenizerException
	{
		if (this.fetched == null)
		{
			throw new TokenizerException("End of stream");
		}
		T lastFetched=this.fetched;
		this.fetched=fetch(this.source);
		return lastFetched;
	}

	protected abstract T fetch(Source source)
		throws TokenizerException;

	public class Source
	{
		private final CharSequence charSequence;

		private int currentIndex;

		public Source(CharSequence charSequence)
		{
			super();
			this.charSequence=charSequence;
		}

		public CharSequence getCharSequence()
		{
			return this.charSequence;
		}

		public int getCurrentIndex()
		{
			return this.currentIndex;
		}

		public void incCurrentIndex()
		{
			this.currentIndex++;
		}

		public void decCurrentIndex()
		{
			this.currentIndex--;
		}
	}
}
