package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.RowData;

public class CrossCursor implements Cursor
{
	public class CombinedRowData implements RowData
	{
		private final CursorColumn[] map;

		public CombinedRowData(List<Cursor> cursors)
			throws CursorException
		{
			this.map=new CursorColumn[getNumberOfColumns()];
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

	private final List<Cursor> cursors;

	private final List<ColumnMetadata> metadata;

	private final Map<String, Integer> columnMap;

	private final RowData rowData;

	private boolean justReset=true;

	public CrossCursor(List<Cursor> cursors)
		throws CursorException
	{
		super();
		this.cursors=cursors;
		this.metadata=concatMetadata(cursors);
		this.columnMap=toColumnMap(this.metadata);
		this.rowData=combineCursors();
	}

	private static List<ColumnMetadata> concatMetadata(List<Cursor> cursors)
		throws CursorException
	{
		int numberOfColumns=0;
		for (Cursor cursor : cursors)
		{
			numberOfColumns+=cursor.getNumberOfColumns();
		}
		List<ColumnMetadata> metadata=new ArrayList<>(numberOfColumns);
		for (Cursor cursor : cursors)
		{
			metadata.addAll(cursor.getMetadata());
		}
		return metadata;
	}

	private static Map<String, Integer> toColumnMap(List<ColumnMetadata> metadata)
	{
		Map<String, Integer> columnMap=new HashMap<>((int)(1.7d * metadata.size()));
		int i=0;
		for (ColumnMetadata column : metadata)
		{
			// FIXME Machaca columnas con nombre repetido:
			columnMap.put(column.getName(), i++);
		}
		return columnMap;
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		boolean hasNext=false;
		for (int i=this.cursors.size() - 1; i >= 0; i--)
		{
			Cursor cursor=this.cursors.get(i);
			hasNext=hasNext || cursor.hasNext();
		}
		return hasNext;
		// return currentAndAllFollowingHaveNext();
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

	@Override
	public void next()
		throws CursorException
	{
		boolean iterate=true;
		if (this.justReset)
		{
			for (int i=this.cursors.size() - 1; iterate && i >= 0; i--)
			{
				Cursor cursor=this.cursors.get(i);
				if (cursor.hasNext())
				{
					cursor.next();
				}
			}
			this.justReset=false;
		}
		else
		{
			for (int i=this.cursors.size() - 1; iterate && i >= 0; i--)
			{
				Cursor cursor=this.cursors.get(i);
				if (cursor.hasNext())
				{
					iterate=false;
				}
				else
				{
					cursor.reset();
				}
				if (cursor.hasNext())
				{
					cursor.next();
				}
			}
		}
	}

	private RowData combineCursors()
		throws CursorException
	{
		return new CombinedRowData(this.cursors);
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
	public List<ColumnMetadata> getMetadata()
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
}
