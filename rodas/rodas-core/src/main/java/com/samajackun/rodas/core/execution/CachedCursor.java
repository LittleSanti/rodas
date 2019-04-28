package com.samajackun.rodas.core.execution;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.RowData;

public interface CachedCursor
{
	public Map<String, Integer> getColumnMap()
		throws CursorException;

	public List<ColumnMetadata> getMetadata()
		throws CursorException;

	public int getNumberOfColumns();

	public int size()
		throws ExhaustedCursorException;

	public RowData getRowData(int index)
		throws CursorException;

}
