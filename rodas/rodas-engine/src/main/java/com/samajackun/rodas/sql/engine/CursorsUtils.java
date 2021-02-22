package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;

final class CursorsUtils
{
	private CursorsUtils()
	{
	}

	public static List<ColumnMetaData> concatMetadata(List<Cursor> cursors)
		throws CursorException
	{
		int numberOfColumns=0;
		for (Cursor cursor : cursors)
		{
			numberOfColumns+=cursor.getNumberOfColumns();
		}
		List<ColumnMetaData> metadata=new ArrayList<>(numberOfColumns);
		for (Cursor cursor : cursors)
		{
			metadata.addAll(cursor.getMetadata());
		}
		return metadata;
	}

	public static Map<String, Integer> toColumnMap(List<ColumnMetaData> metadata)
	{
		Map<String, Integer> columnMap=new HashMap<>((int)(1.7d * metadata.size()));
		int i=0;
		for (ColumnMetaData column : metadata)
		{
			// FIXME Machaca columnas con nombre repetido:
			columnMap.put(column.getName(), i++);
		}
		return columnMap;
	}

}
