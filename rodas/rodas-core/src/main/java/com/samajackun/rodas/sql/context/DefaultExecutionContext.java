package com.samajackun.rodas.sql.context;

import java.util.HashMap;
import java.util.Map;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.IndexNotBoundException;
import com.samajackun.rodas.sql.eval.MapList;
import com.samajackun.rodas.sql.eval.NameNotBoundException;
import com.samajackun.rodas.sql.eval.ParameterNotFoundException;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.RowData;

public class DefaultExecutionContext implements Context
{
	private final MapList<String, IdentifierCoordinates> identifierCoordinatesMap;

	// private final List<IdentifierCoordinates> identifierCoordinatesIndex;

	// private final Map<String, Cursor> cursors;

	private final MapList<String, Cursor> cursorMap;

	private final Map<String, Object> parameters=new HashMap<>();

	public DefaultExecutionContext(MapList<String, Cursor> cursorMap, MapList<String, IdentifierCoordinates> identifierCoordinatesMap)
	{
		super();
		this.cursorMap=cursorMap;
		this.identifierCoordinatesMap=identifierCoordinatesMap;
		// this.identifierCoordinatesIndex=identifierCoordinatesIndex;
	}

	@Override
	public MapList<String, Cursor> getCursors()
	{
		return this.cursorMap;
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
		Cursor cursor=this.cursorMap.get(identifierCoordinates.getRowDataIndex());
		try
		{
			RowData rowData=cursor.getRowData();
			Object value=rowData.get(identifierCoordinates.getColumnIndex());
			return value;
		}
		catch (CursorException e)
		{
			throw new NameNotBoundException(column, prefix);
		}
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
		RowData rowData;
		try
		{
			rowData=this.cursorMap.get(identifierCoordinates.getRowDataIndex()).getRowData();
		}
		catch (CursorException e)
		{
			throw new NameNotBoundException(column);
		}
		Object value=rowData.get(identifierCoordinates.getColumnIndex());
		return value;
	}

	@Override
	public Object getColumnByIndex(int column)
		throws IndexNotBoundException
	{
		if (column < 0 || column >= this.identifierCoordinatesMap.size())
		{
			throw new IndexNotBoundException(column);
		}
		IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesMap.get(column);
		try
		{
			RowData rowData=this.cursorMap.get(identifierCoordinates.getRowDataIndex()).getRowData();
			Object value=rowData.get(identifierCoordinates.getColumnIndex());
			return value;
		}
		catch (CursorException e)
		{
			throw new IndexNotBoundException(column);
		}
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
	public int getColumnIndexByName(String column, String prefix)
		throws NameNotBoundException
	{
		return this.identifierCoordinatesMap.indexOf(prefixColumn(column, prefix));
	}

	private String prefixColumn(String column, String prefix)
	{
		return prefix == null
			? column
			: prefix + "." + column;
	}

	@Override
	public void setParameter(String name, Object value)
	{
		this.parameters.put(name, value);
	}

	@Override
	public Context fork(Context subContext)
	{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
