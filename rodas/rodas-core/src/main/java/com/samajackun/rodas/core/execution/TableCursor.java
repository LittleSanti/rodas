package com.samajackun.rodas.core.execution;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;

public class TableCursor implements Cursor
{
	private final Map<String, Integer> columnMap;

	private final IterableTableData iterable;

	private Iterator<RowData> iterator;

	private RowData iteratorRowData;

	private final RowData rowData=new RowDataProxy();

	private class RowDataProxy implements RowData
	{
		@Override
		public Object get(int column)
		{
			return TableCursor.this.iteratorRowData.get(column);
		}

		@Override
		public long position()
		{
			return TableCursor.this.iteratorRowData.position();
		}
	}

	private final List<ColumnMetaData> metadata;

	public TableCursor(List<ColumnMetaData> metadata, IterableTableData iterable)
		throws ProviderException
	{
		super();
		this.metadata=metadata;
		this.columnMap=toColumnMap(metadata);
		this.iterable=iterable;
		this.iterator=iterable.iterator();
	}

	private static Map<String, Integer> toColumnMap(List<ColumnMetaData> metadata)
	{
		Map<String, Integer> map=new HashMap<>((int)(1.7d * metadata.size()));
		int i=0;
		for (ColumnMetaData column : metadata)
		{
			map.put(column.getName(), i++);
		}
		return map;
	}

	@Override
	public void close()
	{
		// TODO
	}

	@Override
	public void next()
	{
		this.iteratorRowData=this.iterator.next();
	}

	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}

	@Override
	public void reset()
	{
		this.iterator=this.iterable.iterator();
	}

	@Override
	public RowData getRowData()
	{
		return this.rowData;
	}

	@Override
	public Map<String, Integer> getColumnMap()
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
		return this.columnMap.size();
	}
}
