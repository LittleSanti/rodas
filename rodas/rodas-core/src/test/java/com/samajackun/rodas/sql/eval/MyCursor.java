package com.samajackun.rodas.sql.eval;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.IterableTableData;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.RowData;

public class MyCursor implements Cursor
{
	private enum IterationState {
		RESET, ITERATING, EXAHUSTED
	};

	private IterationState iterationState;

	public class PrivateRowData implements RowData
	{
		@Override
		public Object get(int column)
		{
			return MyCursor.this.srcRowData.get(column);
		}
	}

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
	private final List<ColumnMetadata> metadata;

	private final Map<String, Integer> columnMap;

	private final IterableTableData iterable;

	private Iterator<RowData> iterator;

	private final RowData rowData=new PrivateRowData();

	private RowData srcRowData;

	public MyCursor(List<ColumnMetadata> metadata, IterableTableData iterable)
		throws ProviderException
	{
		super();
		this.metadata=metadata;
		this.columnMap=toColumnMap(metadata);
		this.iterable=iterable;
		reset();
	}

	private static Map<String, Integer> toColumnMap(List<ColumnMetadata> metadata)
	{
		Map<String, Integer> map=new HashMap<>((int)(1.7 * metadata.size()));
		int i=0;
		for (ColumnMetadata column : metadata)
		{
			map.put(column.getName(), i++);
		}
		return map;
	}

	@Override
	public void close()
	{
	}

	@Override
	public void next()
	{
		this.srcRowData=this.iterator.next();
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
		this.iterationState=IterationState.RESET;
		this.srcRowData=null;
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
		return this.metadata;
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.columnMap.size();
	}
}
