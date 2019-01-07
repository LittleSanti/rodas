package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Cursor;
import com.samajackun.rodas.core.model.CursorException;
import com.samajackun.rodas.core.model.RowData;

final class CursorsUtils
{
	private CursorsUtils()
	{
	}

	public static List<ColumnMetadata> concatMetadata(List<Cursor> cursors)
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

	public static RowData combineCursors(List<Cursor> cursors)
		throws CursorException
	{
		return new CombinedRowData(cursors);
	}

	public static Map<String, Integer> toColumnMap(List<ColumnMetadata> metadata)
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

}
