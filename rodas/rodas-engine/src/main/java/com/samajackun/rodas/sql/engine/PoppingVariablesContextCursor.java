package com.samajackun.rodas.sql.engine;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

public class PoppingVariablesContextCursor implements Cursor
{
	private final Cursor src;

	private final Context context;

	public PoppingVariablesContextCursor(Cursor src, Context context)
	{
		super();
		this.src=src;
		this.context=context;
	}

	@Override
	public void close()
		throws CursorException
	{
		this.context.getVariablesManager().popLocalContext();
		this.src.close();
	}

	@Override
	public void next()
		throws CursorException
	{
		this.src.next();
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
}
