package com.samajackun.rodas.core.eval;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Cursor;
import com.samajackun.rodas.core.model.CursorException;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;

public class MyCursor implements Cursor
{
	// private enum IterationState {
	// RESET, ITERATING, EXAHUSTED
	// };

	// private IterationState iterationState;

	public class PrivateRowData implements RowData
	{
		@Override
		public Object get(int column)
		{
			return getSrcRowData().get(column);
		}
	}

	private final List<ColumnMetadata> metadata;

	private final Map<String, Integer> columnMap;

	private final IterableTableData iterable;

	private Iterator<RowData> iterator;

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

	public MyCursor(List<ColumnMetadata> metadata, IterableTableData iterable)
		throws ProviderException
	{
		super();
		this.metadata=metadata;
		this.columnMap=toColumnMap(metadata);
		this.iterable=iterable;
		reset();
	}

	private static Map<String, Integer> toColumnMap(List<ColumnMetadata> metadata)
	{
		Map<String, Integer> map=new HashMap<>((int)(1.7 * metadata.size()));
		int i=0;
		for (ColumnMetadata column : metadata)
		{
			map.put(column.getName(), i++);
		}
		return map;
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
