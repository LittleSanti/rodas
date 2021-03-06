package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.eval.ColumnNotFoundException;
import com.samajackun.rodas.sql.eval.IndexNotBoundException;
import com.samajackun.rodas.sql.eval.NameNotBoundException;
import com.samajackun.rodas.sql.eval.ParameterNotFoundException;
import com.samajackun.rodas.sql.eval.PrefixedColumn;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.RowData;

public class DefaultExecutionContext implements ExecutionContext
{
	private final Map<String, IdentifierCoordinates> identifierCoordinatesMap=new HashMap<>();

	private final List<IdentifierCoordinates> identifierCoordinatesIndex=new ArrayList<>();

	private final Map<String, Cursor> cursors;

	private final Map<String, Object> parameters=new HashMap<>();

	public DefaultExecutionContext(Map<String, Cursor> cursors, List<PrefixedColumn> publicColumns, List<PrefixedColumn> privateColumns)
		throws ColumnNotFoundException,
		CursorException
	{
		super();
		this.cursors=cursors;
		for (PrefixedColumn prefixedColumn : publicColumns)
		{
			IdentifierCoordinates identifierCoordinates=bindColumn(prefixedColumn.getPrefix(), prefixedColumn.getColumn());
			this.identifierCoordinatesIndex.add(identifierCoordinates);
		}
		for (PrefixedColumn prefixedColumn : privateColumns)
		{
			bindColumn(prefixedColumn.getPrefix(), prefixedColumn.getColumn());
		}
	}

	private Cursor lookupColumn(String column)
		throws ColumnNotFoundException,
		CursorException
	{
		Cursor found=null;
		for (Iterator<Cursor> iterator=this.cursors.values().iterator(); iterator.hasNext() && found == null;)
		{
			Cursor cursor=iterator.next();
			if (cursor.getColumnMap().containsKey(column))
			{
				found=cursor;
			}
		}
		if (found == null)
		{
			throw new ColumnNotFoundException(column);
		}
		return found;
	}

	private IdentifierCoordinates bindColumn(String prefix, String column)
		throws ColumnNotFoundException,
		CursorException
	{
		Cursor cursor=(prefix != null)
			? this.cursors.get(prefix)
			: lookupColumn(column);
		Integer index=cursor.getColumnMap().get(column);
		IdentifierCoordinates identifierCoordinates=new IdentifierCoordinates(cursor.getRowData(), index);
		this.identifierCoordinatesMap.put(column, identifierCoordinates);
		return identifierCoordinates;
	}

	@Override
	public Object getColumnByName(String column, String prefix)
		throws NameNotBoundException
	{
		IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesMap.get(column);
		if (identifierCoordinates == null)
		{
			throw new NameNotBoundException(column);
		}
		RowData rowData=identifierCoordinates.getRowData();
		Object value=rowData.get(identifierCoordinates.getColumnIndex());
		return value;
	}

	@Override
	public Object getColumnByName(String column)
		throws NameNotBoundException
	{
		IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesMap.get(column);
		if (identifierCoordinates == null)
		{
			throw new NameNotBoundException(column);
		}
		RowData rowData=identifierCoordinates.getRowData();
		Object value=rowData.get(identifierCoordinates.getColumnIndex());
		return value;
	}

	@Override
	public Object getColumnByIndex(int column)
		throws IndexNotBoundException
	{
		if (column < 0 || column >= this.identifierCoordinatesIndex.size())
		{
			throw new IndexNotBoundException(column);
		}
		IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesIndex.get(column);
		RowData rowData=identifierCoordinates.getRowData();
		Object value=rowData.get(identifierCoordinates.getColumnIndex());
		return value;
	}

	@Override
	public Object getParameter(String name)
		throws ParameterNotFoundException
	{
		if (this.parameters.containsKey(name))
		{
			return this.parameters.get(name);
		}
		else
		{
			throw new ParameterNotFoundException(name);
		}
	}

	private class IdentifierCoordinates
	{
		private final RowData rowData;

		private final int columnIndex;

		public IdentifierCoordinates(RowData rowData, int columnIndex)
		{
			super();
			this.rowData=rowData;
			this.columnIndex=columnIndex;
		}

		public RowData getRowData()
		{
			return this.rowData;
		}

		public int getColumnIndex()
		{
			return this.columnIndex;
		}
	}
}
