package com.samajackun.rodas.parsing.source;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class PushBackSource implements Source
{
	private final Source source;

	private int fetchedChar;

	private int fetchedIndex;

	private int fetchedLine;

	private int fetchedColumn;

	private final Deque<Character> pushbackBuffer=new ArrayDeque<Character>();

	private boolean recording;

	private final StringBuilder record=new StringBuilder(2048);

	public PushBackSource(Source source) throws IOException
	{
		super();
		this.source=source;
		fetch();
	}

	private void fetch()
		throws IOException
	{
		this.fetchedColumn=this.source.getCurrentColumn();
		this.fetchedLine=this.source.getCurrentLine();
		this.fetchedIndex=this.source.getCurrentIndex();
		this.fetchedChar=this.source.nextChar();
	}

	@Override
	public int nextChar()
		throws IOException
	{
		int x=readNextChar();
		if (this.recording && x >= 0)
		{
			this.record.append((char)x);
		}
		return x;
	}

	public int discardChar()
		throws IOException
	{
		int x=readNextChar();
		return x;
	}

	private int readNextChar()
		throws IOException
	{
		int x;
		if (this.pushbackBuffer.isEmpty())
		{
			x=this.fetchedChar;
			fetch();
		}
		else
		{
			x=this.pushbackBuffer.pop();
		}
		return x;
	}

	public int lookahead()
		throws IOException
	{
		return (this.pushbackBuffer.isEmpty())
			? this.fetchedChar
			: this.pushbackBuffer.peek();
	}

	public boolean hasMoreChars()
	{
		return (!this.pushbackBuffer.isEmpty()) || this.fetchedChar >= 0;
	}

	public void unget(char c)
	{
		this.pushbackBuffer.push(c);
		if (this.record.length() > 0 && this.recording)
		{
			this.record.setLength(this.record.length() - 1);
		}
		decIndexes(c);
	}

	public void unget(String s)
	{
		for (int i=s.length() - 1; i >= 0; i--)
		{
			char c=s.charAt(i);
			this.pushbackBuffer.push(c);
			decIndexes(c);
		}
		if (this.record.length() > 0 && this.recording)
		{
			this.record.setLength(Math.max(0, this.record.length() - s.length()));
		}
	}

	private void decIndexes(char c)
	{
		this.fetchedIndex--;
		this.fetchedColumn--;
		if (c == '\n')
		{
			this.fetchedLine--;
			this.fetchedColumn=-1;// Valor desconocido
		}
	}

	@Override
	public int getCurrentIndex()
	{
		return this.fetchedIndex;
	}

	@Override
	public int getCurrentLine()
	{
		return this.fetchedLine;
	}

	@Override
	public int getCurrentColumn()
	{
		return this.fetchedColumn;
	}

	@Override
	public void startRecord()
	{
		this.recording=true;
		this.record.setLength(0);
	}

	@Override
	public CharSequence endRecord()
	{
		this.recording=false;
		String s=this.record.toString();
		return s;
	}
}
