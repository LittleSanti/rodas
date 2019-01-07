package com.samajackun.rodas.sql.engine;

import java.util.List;

import com.samajackun.rodas.core.model.Cursor;
import com.samajackun.rodas.core.model.CursorException;
import com.samajackun.rodas.core.model.RowData;

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