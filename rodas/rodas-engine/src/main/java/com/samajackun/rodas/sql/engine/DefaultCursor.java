package com.samajackun.rodas.sql.engine;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.IterableTableData;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.RowData;

public class DefaultCursor implements Cursor
{
	// public class ContextFacade implements Context
	// {
	//
	// @Override
	// public void putSubcontext(String name, Context context)
	// throws NameAlreadyBoundException
	// {
	// throw new IllegalArgumentException();
	// }
	//
	// @Override
	// public Context getSubcontext(String prefix)
	// throws ObjectNotBoundException
	// {
	// throw new ObjectNotBoundException(prefix, "?");
	// }
	//
	// @Override
	// public Object lookup(String name)
	// throws ObjectNotBoundException
	// {
	// return getRowData().get(DefaultCursor.this.columnMap.get(name));
	// }
	//
	// @Override
	// public void bind(String alias, Object value)
	// throws NameAlreadyBoundException
	// {
	// // TODO
	// }
	//
	// @Override
	// public RowResult getResult()
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Object getParameter(String name)
	// throws ParameterNotFoundException
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Object getValue(int index)
	// throws ObjectNotBoundException
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Context findIdentifier(String identifier)
	// throws ObjectNotBoundException
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Context fork()
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Context addCursor(Cursor cursor)
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Context getContextForAlias(String alias)
	// throws NameNotBoundException
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public int getColumn(String name)
	// throws NameNotBoundException
	// {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// }
	//
	// private final Context context=new ContextFacade();
	private final Map<String, Integer> columnMap;

	private final IterableTableData iterable;

	private Iterator<RowData> iterator;

	private RowData rowData;

	public DefaultCursor(Map<String, Integer> columnMap, IterableTableData iterable)
		throws ProviderException
	{
		super();
		this.columnMap=columnMap;
		this.iterable=iterable;
		this.iterator=iterable.iterator();
	}

	@Override
	public void close()
	{
		// TODO
	}

	@Override
	public void next()
	{
		this.rowData=this.iterator.next();
	}

	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}

	@Override
	public void reset()
	{
		this.iterator=this.iterable.iterator();
	}

	@Override
	public RowData getRowData()
	{
		return this.rowData;
	}

	@Override
	public Map<String, Integer> getColumnMap()
	{
		return this.columnMap;
	}

	@Override
	public List<ColumnMetadata> getMetadata()
		throws CursorException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.columnMap.size();
	}
}
