package com.samajackun.rodas.sql.engine;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetadata;
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
		context.getVariablesManager().popLocalContext();
		src.close();
	}

	@Override
	public void next()
		throws CursorException
	{
		src.next();
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		return src.hasNext();
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		return src.getRowData();
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return src.getColumnMap();
	}

	@Override
	public List<ColumnMetadata> getMetadata()
		throws CursorException
	{
		return src.getMetadata();
	}

	@Override
	public void reset()
		throws CursorException
	{
		src.reset();
	}

	@Override
	public int getNumberOfColumns()
	{
		return src.getNumberOfColumns();
	}
}
