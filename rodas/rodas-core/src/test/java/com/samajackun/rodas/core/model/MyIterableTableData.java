package com.samajackun.rodas.core.model;

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
			long n=this.index;
			return new MyRowData(MyIterableTableData.this.data.get(this.index++), n);
		}
	}

	private class MyRowData implements RowData
	{
		private final Object[] data;

		private final long myPosition;

		public MyRowData(Object[] data, long myPosition)
		{
			super();
			this.data=data;
			this.myPosition=myPosition;
		}

		@Override
		public long position()
		{
			return this.myPosition;
		}

		@Override
		public Object get(int column)
		{
			return this.data[column];
		}
	}
}
