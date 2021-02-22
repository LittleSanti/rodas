package com.samajackun.rodas.core.execution;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

/**
 * Represents a set of rows which may be iterated.
 * Can be reset to its original iteration state.
 *
 * @author SKN
 */
public interface Cursor
{
	public void close()
		throws CursorException;

	public void next()
		throws CursorException;

	public boolean hasNext()
		throws CursorException;

	public RowData getRowData()
		throws CursorException;

	public default RowData getCachedRowData()
		throws CursorException
	{
		return new CachedRowData(getRowData(), getNumberOfColumns(), getRowData().position());
	}

	public Map<String, Integer> getColumnMap()
		throws CursorException;

	public List<ColumnMetaData> getMetadata()
		throws CursorException;

	public void reset()
		throws CursorException;

	public int getNumberOfColumns();

	public default boolean isCached()
	{
		return false;
	}

	public default CachedCursor toCachedCursor()
		throws CursorException
	{
		return new DefaultCachedCursor(this);
	}
}
