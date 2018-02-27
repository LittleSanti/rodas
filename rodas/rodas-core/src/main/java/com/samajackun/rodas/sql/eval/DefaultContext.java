package com.samajackun.rodas.sql.eval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.RowData;

public class DefaultContext implements Context
{
	private final Map<String, IdentifierCoordinates> identifierCoordinatesMap=new HashMap<>();

	private final List<IdentifierCoordinates> identifierCoordinatesIndex=new ArrayList<>();

	private final Map<String, Cursor> cursors;

	private final Map<String, Object> parameters=new HashMap<>();

	public DefaultContext(Map<String, Cursor> cursors)
	{
		super();
		this.cursors=cursors;
	}

	public void addCursor(String name, Cursor cursor)
		throws NameAlreadyBoundException
	{
		Cursor old=this.cursors.put(name, cursor);
		if (old != null)
		{
			throw new NameAlreadyBoundException(name);
		}

	}

	@Override
	public void bindPublicColumn(String prefix, String column)
		throws CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		IdentifierCoordinates identifierCoordinates=bindColumn(prefix, column);
		this.identifierCoordinatesIndex.add(identifierCoordinates);
	}

	@Override
	public void bindPrivateColumn(String prefix, String column)
		throws CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		bindColumn(prefix, column);
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
		CursorException,
		PrefixNotFoundException
	{
		Cursor cursor=(prefix != null)
			? this.cursors.get(prefix)
			: lookupColumn(column);
		if (cursor == null)
		{
			throw new PrefixNotFoundException(prefix);
		}
		Integer index=cursor.getColumnMap().get(column);
		if (index == null)
		{
			throw new ColumnNotFoundException(column, prefix);
		}
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
	public int getColumnIndexByName(String column, String prefix)
		throws NameNotBoundException
	{
		IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesMap.get(column);
		if (identifierCoordinates == null)
		{
			throw new NameNotBoundException(column);
		}
		return identifierCoordinates.getColumnIndex();
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

	@Override
	public void setParameter(String name, Object value)
	{
		this.parameters.put(name, value);
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

	@Override
	public Context fork(Map<String, Cursor> cursors)
	{
		return new CompoundContext(new DefaultContext(cursors), this);
	}
}
