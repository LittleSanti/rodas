package com.samajackun.rodas.core.execution;

import com.samajackun.rodas.core.model.RowData;

public class EmptyRowData implements RowData
{
	private static final EmptyRowData INSTANCE=new EmptyRowData();

	public static EmptyRowData getInstance()
	{
		return INSTANCE;
	}

	private EmptyRowData()
	{
	}

	@Override
	public Object get(int column)
	{
		return null;
	}

	@Override
	public long position()
	{
		return 0;
	}

}
