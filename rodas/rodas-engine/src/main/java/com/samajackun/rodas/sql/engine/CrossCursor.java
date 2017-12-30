package com.samajackun.rodas.sql.engine;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.RowData;

public class CrossCursor implements Cursor
{
	private final List<Cursor> cursors;

	private int currentIndex;

	private RowData rowData;

	public CrossCursor(List<Cursor> cursors)
	{
		super();
		this.cursors=cursors;
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		return currentAndAllFollowingHaveNext();
	}

	private boolean currentAndAllFollowingHaveNext()
		throws CursorException
	{
		boolean x=false;
		for (int i=this.currentIndex; i < this.cursors.size(); i++)
		{
			Cursor cursor=this.cursors.get(i);
			x=cursor.hasNext();
		}
		return x;
	}

	@Override
	public void next()
		throws CursorException
	{
		if (!currentAndAllFollowingHaveNext())
		{
			throw new NoSuchElementException();
		}
		Cursor cursor=this.cursors.get(this.currentIndex);
		if (cursor.hasNext())
		{
			cursor.next();
		}
		else
		{
			for (int i=0; i <= this.currentIndex; i++)
			{
				this.cursors.get(i).reset();
			}
			this.currentIndex++;
			this.cursors.get(this.currentIndex).next();
		}
		this.rowData=combineCursors();
	}

	private RowData combineCursors()
	{
		return this.rowData;
	}

	@Override
	public void close()
		throws CursorException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public RowData getRowData()
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
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		// TODO Auto-generated method stub
		return null;
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
