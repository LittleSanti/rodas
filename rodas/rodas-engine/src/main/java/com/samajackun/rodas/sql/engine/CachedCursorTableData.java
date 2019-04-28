package com.samajackun.rodas.sql.engine;

import java.util.Iterator;

import com.samajackun.rodas.core.execution.CachedCursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.execution.ExhaustedCursorException;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.RowData;

public class CachedCursorTableData implements IterableTableData
{
	private final CachedCursor src;

	public CachedCursorTableData(CachedCursor src)
	{
		super();
		this.src=src;
	}

	@Override
	public Iterator<RowData> iterator()
	{
		return new MyIterator();
	}

	private class MyIterator implements Iterator<RowData>
	{
		private int currentIndex;

		@Override
		public boolean hasNext()
		{
			try
			{
				return currentIndex < src.size();
			}
			catch (ExhaustedCursorException e)
			{
				throw new java.lang.RuntimeException(e);
			}
		}

		@Override
		public RowData next()
		{
			try
			{
				RowData rowData=src.getRowData(currentIndex);
				currentIndex++;
				return rowData;
			}
			catch (CursorException e)
			{
				throw new java.lang.RuntimeException(e);
			}
		}
	}
}
