package com.samajackun.rodas.sql.model;

import java.util.Iterator;
import java.util.List;

public class MyIterableTableData implements IterableTableData
{
	private final List<Object[]> data;

	public MyIterableTableData(List<Object[]> data)
	{
		super();
		this.data=data;
	}

	@Override
	public Iterator<RowData> iterator()
	{
		return new MyIterator();
	}

	private class MyIterator implements Iterator<RowData>
	{
		private int index=0;

		@Override
		public boolean hasNext()
		{
			return this.index < MyIterableTableData.this.data.size();
		}

		@Override
		public RowData next()
		{
			return new MyRowData(MyIterableTableData.this.data.get(this.index++));
		}
	}

	private class MyRowData implements RowData
	{
		private final Object[] data;

		public MyRowData(Object[] data)
		{
			super();
			this.data=data;
		}

		@Override
		public Object get(int column)
		{
			return this.data[column];
		}
	}
}
