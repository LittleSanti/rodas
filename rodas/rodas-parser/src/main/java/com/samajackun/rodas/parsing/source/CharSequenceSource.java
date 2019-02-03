package com.samajackun.rodas.parsing.source;

import java.io.IOException;

public class CharSequenceSource extends AbstractSource
{
	private final CharSequence src;

	private int index;

	private int recordFrom;

	public CharSequenceSource(CharSequence src)
	{
		super();
		this.src=src;
	}

	@Override
	protected int getNextChar()
		throws IOException
	{
		int x=this.index < this.src.length()
			? this.src.charAt(this.index++)
			: -1;
		return x;
	}

	@Override
	public void startRecord()
	{
		this.recordFrom=this.index;
	}

	@Override
	public CharSequence endRecord()
	{
		return this.src.subSequence(this.recordFrom, this.index);
	}

}
