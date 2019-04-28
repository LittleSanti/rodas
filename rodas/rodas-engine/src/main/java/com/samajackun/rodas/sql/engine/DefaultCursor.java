package com.samajackun.rodas.sql.engine;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;

public class DefaultCursor implements Cursor
{
	public class PrivateRowData implements RowData
	{
		@Override
		public Object get(int column)
		{
			return getSrcRowData().get(column);
		}

		@Override
		public long position()
		{
			return DefaultCursor.this.myPosition;
		}
	}

	private final List<ColumnMetadata> metadata;

	private final Map<String, Integer> columnMap;

	private final IterableTableData iterable;

	private Iterator<RowData> iterator;

	private long myPosition;

	private final RowData rowData=new PrivateRowData();

	private RowData srcRowData;

	private final RowData getSrcRowData()
	{
		if (this.srcRowData == null)
		{
			throw new IllegalStateException("A previous call to next is required");
		}
		return this.srcRowData;
	}

	public DefaultCursor(List<ColumnMetadata> metadata, IterableTableData iterable)
		throws ProviderException
	{
		super();
		this.metadata=metadata;
		this.columnMap=CursorsUtils.toColumnMap(metadata);
		this.iterable=iterable;
		reset();
	}

	@Override
	public void close()
	{
	}

	@Override
	public void next()
	{
		this.srcRowData=this.iterator.next();
	}

	@Override
	public boolean hasNext()
	{
		this.myPosition++;
		return this.iterator.hasNext();
	}

	@Override
	public void reset()
	{
		this.iterator=this.iterable.iterator();
		// this.iterationState=IterationState.RESET;
		this.srcRowData=null;
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
	public List<ColumnMetadata> getMetadata()
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
