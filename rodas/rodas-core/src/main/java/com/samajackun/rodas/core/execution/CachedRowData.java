package com.samajackun.rodas.core.execution;

import java.util.Arrays;

import com.samajackun.rodas.core.model.RowData;

public class CachedRowData implements RowData
{
	private final Object[] cells;

	public long myPosition;

	public CachedRowData(RowData src, int len, long myPosition)
	{
		this.cells=new Object[len];
		this.myPosition=myPosition;
		for (int i=0; i < len; i++)
		{
			this.cells[i]=src.get(i);
		}
	}

	@Override
	public Object get(int column)
	{
		return this.cells[column];
	}

	@Override
	public long position()
	{
		return this.myPosition;
	}

	@Override
	public String toString()
	{
		return "[" + Arrays.toString(this.cells) + "]";
	}

}