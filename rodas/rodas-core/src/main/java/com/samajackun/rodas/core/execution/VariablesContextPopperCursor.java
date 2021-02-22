package com.samajackun.rodas.core.execution;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

public class VariablesContextPopperCursor implements Cursor
{

	@Override
	public void close()
		throws CursorException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void next()
		throws CursorException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColumnMetaData> getMetadata()
		throws CursorException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset()
		throws CursorException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getNumberOfColumns()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
