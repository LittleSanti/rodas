package com.samajackun.rodas.sql.model;

import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.RowData;
import com.samajackun.rodas.sql.model.TableData;

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
			return new MyRowData(this.src.next());
		}

		private class MyRowData implements RowData
		{
			private final Object[] map;

			public MyRowData(Object[] map)
			{
				super();
				this.map=map;
			}

			@Override
			public Object get(int column)
			{
				return this.map[column];
			}
		}
	}
}