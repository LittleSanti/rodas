package com.samajackun.rodas.core.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

public class DefaultCachedCursor implements CachedCursor
{
	private final List<RowData> records;

	private final Map<String, Integer> columnMap;

	private final List<ColumnMetaData> metadata;

	private boolean closed;

	public DefaultCachedCursor(Cursor src) throws CursorException
	{
		columnMap=src.getColumnMap();
		metadata=src.getMetadata();
		records=readAll(src);
	}

	private List<RowData> readAll(Cursor src)
		throws CursorException
	{
		ArrayList<RowData> records=new ArrayList<>(4096);
		while (src.hasNext())
		{
			src.next();
			RowData record=new CachedRowData(src.getRowData(), src.getNumberOfColumns(), records.size());
			records.add(record);
		}
		records.trimToSize();
		return records;
	}

	@Override
	public int size()
		throws ExhaustedCursorException
	{
		checkClosed();
		return records.size();
	}

	@Override
	public RowData getRowData(int index)
		throws ExhaustedCursorException
	{
		checkClosed();
		return records.get(index);
	}

	private void checkClosed()
		throws ExhaustedCursorException
	{
		if (closed)
		{
			throw new ExhaustedCursorException();
		}
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return columnMap;
	}

	@Override
	public List<ColumnMetaData> getMetadata()
		throws CursorException
	{
		return metadata;
	}

	@Override
	public int getNumberOfColumns()
	{
		return metadata.size();
	}
}
