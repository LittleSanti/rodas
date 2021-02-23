package com.samajackun.rodas.sql.engine;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

public abstract class AbstractComposedCursor implements Cursor
{
	private final Cursor src;

	private long currentPosition;

	public AbstractComposedCursor(Cursor src)
	{
		super();
		this.src=src;
	}

	@Override
	public void close()
		throws CursorException
	{
		this.src.close();
	}

	@Override
	public void next()
		throws CursorException
	{
		this.src.next();
		this.currentPosition++;
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		return this.src.hasNext();
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		return this.src.getRowData();
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
		this.src.reset();
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.src.getNumberOfColumns();
	}

	protected long getCurrentPosition()
	{
		return this.currentPosition;
	}

	protected Cursor getSrc()
	{
		return this.src;
	}
}
