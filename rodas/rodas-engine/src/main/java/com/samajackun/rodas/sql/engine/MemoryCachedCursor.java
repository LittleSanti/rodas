package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.execution.CachedCursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.execution.ExhaustedCursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

public class MemoryCachedCursor implements CachedCursor
{
	private final List<RowData> rows=new ArrayList<>(1024);

	private final List<ColumnMetaData> metadata;

	private final Map<String, Integer> columnMap;

	public MemoryCachedCursor(List<ColumnMetaData> metadata)
	{
		super();
		this.metadata=metadata;
		this.columnMap=CursorsUtils.toColumnMap(metadata);
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return this.columnMap;
	}

	@Override
	public List<ColumnMetaData> getMetadata()
		throws CursorException
	{
		return this.metadata;
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.metadata.size();
	}

	@Override
	public int size()
		throws ExhaustedCursorException
	{
		return this.rows.size();
	}

	@Override
	public RowData getRowData(int index)
		throws CursorException
	{
		if (index >= this.rows.size())
		{
			throw new IndexOutOfCursorBoundsException(index);
		}
		return this.rows.get(index);
	}

	public void addRow(RowData row)
	{
		this.rows.add(row);
	}
}
