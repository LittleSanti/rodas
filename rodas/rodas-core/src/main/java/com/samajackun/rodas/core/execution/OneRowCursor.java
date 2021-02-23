package com.samajackun.rodas.core.execution;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

public class OneRowCursor implements Cursor
{
	private boolean closed;

	@Override
	public void close()
		throws CursorException
	{
		this.closed=true;
	}

	@Override
	public void next()
		throws CursorException
	{
		this.closed=true;
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		return !this.closed;
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		return EmptyRowData.getInstance();
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return Collections.emptyMap();
	}

	@Override
	public List<ColumnMetaData> getMetadata()
		throws CursorException
	{
		return Collections.emptyList();
	}

	@Override
	public void reset()
		throws CursorException
	{
	}

	@Override
	public int getNumberOfColumns()
	{
		return 0;
	}

}
