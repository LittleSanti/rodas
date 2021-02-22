package com.samajackun.rodas.sql.engine;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.execution.CachedCursor;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.execution.ExhaustedCursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

public class MemoryCursor implements Cursor
{
	private final CachedCursor src;

	private int currentIndex=-1;

	private boolean closed;

	public MemoryCursor(CachedCursor src)
	{
		super();
		this.src=src;
	}

	private void checkClosed()
		throws ClosedCursorException
	{
		if (this.closed)
		{
			throw new ClosedCursorException();
		}
	}

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
		checkClosed();
		if (this.currentIndex < this.src.size())
		{
			this.currentIndex++;
		}
		else
		{
			throw new ExhaustedCursorException();
		}
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		checkClosed();
		return this.currentIndex < this.src.size() - 1;
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		checkClosed();
		return this.src.getRowData(this.currentIndex);
	}

	@Override
	public RowData getCachedRowData()
		throws CursorException
	{
		return getRowData();
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return this.src.getColumnMap();
	}

	@Override
	public List<ColumnMetaData> getMetadata()
		throws CursorException
	{
		return this.src.getMetadata();
	}

	@Override
	public void reset()
		throws CursorException
	{
		checkClosed();
		this.currentIndex=0;
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.src.getNumberOfColumns();
	}

	@Override
	public boolean isCached()
	{
		return true;
	}

	@Override
	public CachedCursor toCachedCursor()
		throws CursorException
	{
		return this.src;
	}
}
