package com.samajackun.rodas.sql.engine;

import com.samajackun.rodas.core.execution.ExhaustedCursorException;

public class IndexOutOfCursorBoundsException extends ExhaustedCursorException
{
	private static final long serialVersionUID=2273398347280887952L;

	private final int index;

	public IndexOutOfCursorBoundsException(int index)
	{
		super("Index " + index + " is out of cursor bounds");
		this.index=index;
	}

	public int getIndex()
	{
		return this.index;
	}
}
