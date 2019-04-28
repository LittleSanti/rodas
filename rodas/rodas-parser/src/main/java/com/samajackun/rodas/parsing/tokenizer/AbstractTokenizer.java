package com.samajackun.rodas.parsing.tokenizer;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings;

/**
 *
 * @author Santi
 * @version 2017.00
 */
public abstract class AbstractTokenizer implements Tokenizer
{
	private final TokenizerSettings settings;

	// private final PushBackSource source;
	private final PushBackSource source;

	private final Deque<Token> pushbackBuffer=new ArrayDeque<Token>();

	private Token fetched;

	protected AbstractTokenizer(PushBackSource source, TokenizerSettings settings) throws TokenizerException, IOException
	{
		super();
		this.source=source;
		this.settings=settings;
		this.fetched=fetch(this.source);
	}

	public TokenizerSettings getSettings()
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
	public Token nextToken()
		throws TokenizerException,
		IOException
	{
		Token x;
		if (this.pushbackBuffer.isEmpty())
		{
			x=this.fetched;
			this.fetched=fetch(this.source);
		}
		else
		{
			x=this.pushbackBuffer.pop();
		}
		return x;
	}

	protected abstract Token fetch(PushBackSource source)
		throws TokenizerException,
		IOException;

	@Override
	public void pushBackToken(Token token)
		throws IOException
	{
		pushBackToken(token, this.source);
	}

	protected void pushBackToken(Token token, PushBackSource source)
		throws IOException
	{
		this.pushbackBuffer.push(token);
	}

	//
	// protected PushBackSource getSource()
	// {
	// return this.source;
	// }

	@Override
	public PushBackSource getSource()
	{
		return this.source;
	}
}
