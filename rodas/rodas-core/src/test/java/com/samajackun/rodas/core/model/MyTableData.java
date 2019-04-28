package com.samajackun.rodas.core.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class MyTableData implements TableData
{
	private final List<Object[]> list;

	public MyTableData(List<Object[]> list)
	{
		super();
		this.list=list;
	}

	@Override
	public int countRows()
		throws ProviderException
	{
		return this.list.size();
	}

	@Override
	public Object[] getRow(int row)
		throws ProviderException
	{
		return this.list.get(row);
	}

	@Override
	public Iterator<RowData> iterator()
	{
		return new MyIterator(this.list.iterator());
	}

	public class MyIterator implements Iterator<RowData>
	{
		private final Iterator<Object[]> src;

		private long myPosition;

		public MyIterator(Iterator<Object[]> src)
		{
			super();
			this.src=src;
		}

		@Override
		public boolean hasNext()
		{
			return this.src.hasNext();
		}

		@Override
		public RowData next()
		{
			return new MyRowData(this.src.next(), this.myPosition++);
		}

		private class MyRowData implements RowData
		{
			private final Object[] map;

			private final long myPosition;

			public MyRowData(Object[] map, long myPosition)
			{
				super();
				this.map=map;
				this.myPosition=myPosition;
			}

			@Override
			public Object get(int column)
			{
				return this.map[column];
			}

			@Override
			public long position()
			{
				return this.myPosition;
			}

			@Override
			public String toString()
			{
				return Arrays.asList(this.map).toString();
			}
		}
	}
}