package com.samajackun.rodas.sql.engine;

import static com.samajackun.rodas.sql.engine.CursorsUtils.concatMetadata;
import static com.samajackun.rodas.sql.engine.CursorsUtils.toColumnMap;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.RowData;

public class CrossCursor implements Cursor
{
	private final List<Cursor> cursors;

	private final Deque<Cursor> cursorsInReverse;

	private final List<ColumnMetaData> metadata;

	private final Map<String, Integer> columnMap;

	private final RowData rowData;

	private long myPosition;

	private boolean justReset=true;

	public CrossCursor(List<Cursor> cursors)
		throws CursorException
	{
		super();
		this.cursors=cursors;
		this.cursorsInReverse=new ArrayDeque<>(cursors.size());
		for (Cursor cursor : cursors)
		{
			this.cursorsInReverse.push(cursor);
		}
		this.metadata=concatMetadata(cursors);
		this.columnMap=toColumnMap(this.metadata);
		this.rowData=new CombinedRowData(this.cursors);
	}

	// private boolean currentAndAllFollowingHaveNext()
	// throws CursorException
	// {
	// boolean x=false;
	// for (int i=this.currentIndex; i < this.cursors.size(); i++)
	// {
	// Cursor cursor=this.cursors.get(i);
	// x=cursor.hasNext();
	// }
	// return x;
	// }

	// @Override
	// public void next()
	// throws CursorException
	// {
	// if (!currentAndAllFollowingHaveNext())
	// {
	// throw new NoSuchElementException();
	// }
	// Cursor cursor=this.cursors.get(this.currentIndex);
	// if (cursor.hasNext())
	// {
	// cursor.next();
	// }
	// else
	// {
	// for (int i=0; i <= this.currentIndex; i++)
	// {
	// this.cursors.get(i).reset();
	// }
	// this.currentIndex++;
	// this.cursors.get(this.currentIndex).next();
	// }
	// }

	// @Override
	// public void next()
	// throws CursorException
	// {
	// boolean iterate=true;
	// if (this.justReset)
	// {
	// for (int i=this.cursors.size() - 1; i >= 0; i--)
	// {
	// Cursor cursor=this.cursors.get(i);
	// if (cursor.hasNext())
	// {
	// cursor.next();
	// }
	// }
	// this.justReset=false;
	// }
	// else
	// {
	// for (int i=this.cursors.size() - 1; iterate && i >= 0; i--)
	// {
	// Cursor cursor=this.cursors.get(i);
	// if (cursor.hasNext())
	// {
	// iterate=false;
	// }
	// else
	// {
	// cursor.reset();
	// }
	// if (cursor.hasNext())
	// {
	// cursor.next();
	// }
	// }
	// }
	// }

	@Override
	public boolean hasNext()
		throws CursorException
	{
		boolean hasNext;
		if (this.justReset)
		{
			hasNext=true;
			for (Iterator<Cursor> iterator=this.cursorsInReverse.iterator(); iterator.hasNext();)
			{
				Cursor cursor=iterator.next();
				hasNext=hasNext && cursor.hasNext();
			}
		}
		else
		{
			hasNext=false;
			for (Iterator<Cursor> iterator=this.cursorsInReverse.iterator(); !hasNext && iterator.hasNext();)
			{
				Cursor cursor=iterator.next();
				hasNext=cursor.hasNext();
			}
		}
		return hasNext;
	}

	@Override
	public void next()
		throws CursorException
	{
		if (this.justReset)
		{
			for (Iterator<Cursor> iterator=this.cursorsInReverse.iterator(); iterator.hasNext();)
			{
				Cursor cursor=iterator.next();
				if (cursor.hasNext())
				{
					cursor.next();
				}
			}
			this.justReset=false;
		}
		else
		{
			boolean hasNext=false;
			for (Iterator<Cursor> iterator=this.cursorsInReverse.iterator(); !hasNext && iterator.hasNext();)
			{
				Cursor cursor=iterator.next();
				if (hasNext=cursor.hasNext())
				{
					cursor.next();
				}
				else
				{
					cursor.reset();
					if (cursor.hasNext())
					{
						cursor.next();
					}
				}
			}
		}
		this.myPosition++;
	}

	@Override
	public void close()
		throws CursorException
	{
		for (Cursor cursor : this.cursors)
		{
			cursor.close();
		}
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		return this.rowData;
	}

	@Override
	public void reset()
		throws CursorException
	{
		for (Cursor cursor : this.cursors)
		{
			cursor.close();
		}
		this.justReset=true;
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return this.columnMap;
	}

	@Override
	public List<ColumnMetaData> getMetadata()
		throws CursorException
	{
		return this.metadata;
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.metadata.size();
	}

	// private class Wrapper implements Comparable<Wrapper>
	// {
	// private final int pos;
	//
	// private final Cursor cursor;
	//
	// private final int rows;
	//
	// public Wrapper(int pos, Cursor cursor, int rows)
	// {
	// super();
	// this.pos=pos;
	// this.cursor=cursor;
	// this.rows=rows;
	// }
	//
	// @Override
	// public int hashCode()
	// {
	// return this.pos;
	// }
	//
	// @Override
	// public boolean equals(Object o)
	// {
	// return o == this;
	// }
	//
	// @Override
	// public int compareTo(Wrapper wrapper)
	// {
	// int x;
	// if (this.pos < wrapper.pos)
	// {
	// x=-1;
	// }
	// else if (this.pos == wrapper.pos)
	// {
	// if (this.rows < wrapper.rows)
	// {
	// x=-1;
	// }
	// else if (this.rows == wrapper.rows)
	// {
	// x=0;
	// }
	// else
	// {
	// x=1;
	// }
	// }
	// else
	// {
	// x=1;
	// }
	// return x;
	// }
	// }
	class CombinedRowData implements RowData
	{
		private final CursorColumn[] map;

		public CombinedRowData(List<Cursor> cursors)
			throws CursorException
		{
			int numberOfColumns=0;
			for (Cursor cursor : cursors)
			{
				numberOfColumns+=cursor.getNumberOfColumns();
			}
			this.map=new CursorColumn[numberOfColumns];
			int i=0;
			for (Cursor cursor : cursors)
			{
				for (int j=0; j < cursor.getNumberOfColumns(); j++)
				{
					this.map[i++]=new CursorColumn(cursor.getRowData(), j);
				}
			}
		}

		@Override
		public long position()
		{
			return CrossCursor.this.myPosition;
		}

		@Override
		public Object get(int column)
		{
			CursorColumn cursorColumn=this.map[column];
			return cursorColumn.getRowData().get(cursorColumn.getColumn());
		}

		private class CursorColumn
		{
			private final RowData rowData;

			private final int column;

			public CursorColumn(RowData rowData, int column)
			{
				super();
				this.rowData=rowData;
				this.column=column;
			}

			public RowData getRowData()
			{
				return this.rowData;
			}

			public int getColumn()
			{
				return this.column;
			}
		}
	}
}
