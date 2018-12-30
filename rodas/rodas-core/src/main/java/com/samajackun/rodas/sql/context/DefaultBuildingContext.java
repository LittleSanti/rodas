package com.samajackun.rodas.sql.context;

import java.util.Iterator;
import java.util.Map;

import com.samajackun.rodas.sql.eval.ColumnNotFoundException;
import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.MapList;
import com.samajackun.rodas.sql.eval.NameAlreadyBoundException;
import com.samajackun.rodas.sql.eval.PrefixNotFoundException;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.Provider;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.Source;

public class DefaultBuildingContext
{
	private final MapList<String, IdentifierCoordinates> identifierCoordinatesMap=new MapList<>(57);

	// private final List<IdentifierCoordinates> identifierCoordinatesIndex=new ArrayList<>();

	private final MapList<String, Source> sources;

	private final Provider provider;

	// private final Map<String, Object> parameters=new HashMap<>();

	// private RowData[] rowDatas;

	public DefaultBuildingContext(Provider provider, MapList<String, Source> sources)
	{
		super();
		this.provider=provider;
		this.sources=sources;
	}

	// public Provider getProvider()
	// {
	// return this.provider;
	// }

	public void addSource(String name, Source source)
		throws NameAlreadyBoundException
	{
		Source old=this.sources.put(name, source);
		if (old != null)
		{
			throw new NameAlreadyBoundException(name);
		}

	}

	public void bindPublicColumn(String prefix, String column)
		throws ProviderException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		bindColumn(prefix, column);
		// this.identifierCoordinatesIndex.add(identifierCoordinates);
	}

	public void bindPrivateColumn(String prefix, String column)
		throws ProviderException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		bindColumn(prefix, column);
	}

	private Source lookupColumn(String column)
		throws ColumnNotFoundException,
		ProviderException
	{
		Source found=null;
		for (Iterator<Source> iterator=this.sources.values().iterator(); iterator.hasNext() && found == null;)
		{
			Source source=iterator.next();
			if (source.getColumnNamesMap(this.provider).containsKey(column))
			{
				found=source;
			}
		}
		if (found == null)
		{
			throw new ColumnNotFoundException(column);
		}
		return found;
	}

	private String prefixColumn(String column, String prefix)
	{
		return prefix == null
			? column
			: prefix + "." + column;
	}

	private IdentifierCoordinates bindColumn(String prefix, String column)
		throws ColumnNotFoundException,
		PrefixNotFoundException,
		ProviderException
	{
		String prefixAndColumn=prefixColumn(column, prefix);
		IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesMap.get(prefixAndColumn);
		if (identifierCoordinates == null)
		{
			Source source=(prefix != null)
				? this.sources.get(prefix)
				: lookupColumn(column);
			if (source == null)
			{
				throw new PrefixNotFoundException(prefix);
			}
			Map<String, Integer> columnNamesMap=source.getColumnNamesMap(this.provider);
			Integer columnIndex=columnNamesMap.get(column);
			if (columnIndex == null)
			{
				throw new ColumnNotFoundException(column, prefix);
			}
			int sourceIndex=this.sources.indexOf(prefix);
			identifierCoordinates=new IdentifierCoordinates(sourceIndex, columnIndex);
			this.identifierCoordinatesMap.put(prefixAndColumn, identifierCoordinates);
		}

		return identifierCoordinates;
	}

	//
	// public Object getColumnByName(String column, String prefix)
	// throws NameNotBoundException
	// {
	// String prefixAndColumn=prefixColumn(column, prefix);
	// IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesMap.get(prefixAndColumn);
	// if (identifierCoordinates == null)
	// {
	// throw new NameNotBoundException(column);
	// }
	// RowData rowData=this.rowDatas[identifierCoordinates.getRowDataIndex()];
	// Object value=rowData.get(identifierCoordinates.getColumnIndex());
	// return value;
	// }

	//
	// public Object getColumnByName(String column)
	// throws NameNotBoundException
	// {
	// return getColumnByName(column, null);
	// }

	// public int getColumnIndexByName(String column, String prefix)
	// throws NameNotBoundException
	// {
	// String prefixAndColumn=prefixColumn(column, prefix);
	// IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesMap.get(prefixAndColumn);
	// if (identifierCoordinates == null)
	// {
	// throw new NameNotBoundException(column);
	// }
	// return identifierCoordinates.getColumnIndex();
	// }

	//
	// public Object getColumnByIndex(int column)
	// throws IndexNotBoundException
	// {
	// if (column < 0 || column >= this.identifierCoordinatesIndex.size())
	// {
	// throw new IndexNotBoundException(column);
	// }
	// IdentifierCoordinates identifierCoordinates=this.identifierCoordinatesIndex.get(column);
	// RowData rowData=this.rowDatas[identifierCoordinates.getRowDataIndex()];
	// Object value=rowData.get(identifierCoordinates.getColumnIndex());
	// return value;
	// }

	//
	// public Object getParameter(String name)
	// throws ParameterNotFoundException
	// {
	// if (this.parameters.containsKey(name))
	// {
	// return this.parameters.get(name);
	// }
	// else
	// {
	// throw new ParameterNotFoundException(name);
	// }
	// }
	//
	//
	// public void setParameter(String name, Object value)
	// {
	// this.parameters.put(name, value);
	// }

	public DefaultBuildingContext fork(MapList<String, Source> sources)
	{
		MapList<String, Source> compoundSources=new MapList<>(sources);
		return new DefaultBuildingContext(this.provider, compoundSources);
	}

	public Context toExecutionContext(MapList<String, Cursor> cursorMap)
	{
		return new DefaultExecutionContext(cursorMap, this.identifierCoordinatesMap);
	}
}
