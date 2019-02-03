package com.samajackun.rodas.parsing.source;

import java.io.IOException;

public abstract class AbstractSource implements Source
{
	private int currentIndex;

	private int currentLine;

	private int currentColumn;

	@Override
	public int nextChar()
		throws IOException
	{
		int next=getNextChar();
		if (next >= 0)
		{
			this.currentIndex++;
			if (next == '\n')
			{
				this.currentLine++;
				this.currentColumn=0;
			}
			else
			{
				this.currentColumn++;
			}
		}
		return next;
	}

	protected abstract int getNextChar()
		throws IOException;

	@Override
	public abstract void startRecord();

	@Override
	public abstract CharSequence endRecord();

	@Override
	public int getCurrentIndex()
	{
		return this.currentIndex;
	}

	@Override
	public int getCurrentLine()
	{
		return this.currentLine;
	}

	@Override
	public int getCurrentColumn()
	{
		return this.currentColumn;
	}
}
