package com.samajackun.rodas.sql.model;

import java.util.Map;

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

	public Map<String, Integer> getColumnMap()
		throws CursorException;

	public void reset()
		throws CursorException;
}
