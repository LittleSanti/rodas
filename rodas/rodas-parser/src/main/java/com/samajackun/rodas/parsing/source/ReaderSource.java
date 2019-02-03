package com.samajackun.rodas.parsing.source;

import java.io.IOException;
import java.io.Reader;

public class ReaderSource extends AbstractSource
{
	private final Reader src;

	private final StringBuilder stb=new StringBuilder(2048);

	private boolean recording;

	public ReaderSource(Reader src)
	{
		super();
		this.src=src;
	}

	@Override
	protected int getNextChar()
		throws IOException
	{
		int x=this.src.read();
		if (this.recording)
		{
			if (x >= 0)
			{
				this.stb.append((char)x);
			}
		}
		return x;
	}

	@Override
	public void startRecord()
	{
		this.recording=true;
	}

	@Override
	public CharSequence endRecord()
	{
		this.recording=false;
		return this.stb.toString();
	}
}
