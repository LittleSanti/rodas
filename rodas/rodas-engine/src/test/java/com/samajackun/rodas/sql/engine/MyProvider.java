package com.samajackun.rodas.sql.engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.Datatype;
import com.samajackun.rodas.sql.model.IterableTableData;
import com.samajackun.rodas.sql.model.Provider;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.TableData;
import com.samajackun.rodas.sql.model.TableMetadata;

public class MyProvider implements Provider
{

	@Override
	public Map<String, Integer> getColumnMapFromTable(String tableName)
		throws ProviderException
	{
		Map<String, Integer> columns;
		switch (tableName)
		{
			case "country":
				columns=map(Arrays.asList("idCountry", "name", "area"));
				break;
			case "city":
				columns=map(Arrays.asList("idCity", "idCountry", "name"));
				break;
			default:
				throw new ProviderException("Table '" + tableName + "' not found");
		}
		return columns;
	}

	private Map<String, Integer> map(List<String> list)
	{
		Map<String, Integer> map=new HashMap<>((int)(1.7d * list.size()));
		int i=0;
		for (String s : list)
		{
			map.put(s, i++);
		}
		return map;
	}

	private Object[] countryToArray(int idCountry, String name, double area)
	{
		return new Object[] {
			idCountry,
			name,
			area
		};
	}

	private Object[] cityToArray(int idCity, int idCountry, String name)
	{
		return new Object[] {
			idCity,
			idCountry,
			name,
		};
	}

	@Override
	public TableData getTableData(String table)
		throws ProviderException
	{
		TableData tableData;
		switch (table)
		{
			case "country":
				tableData=new MyTableData(Arrays.asList(countryToArray(1, "spain", 121.1), countryToArray(2, "portugal", 122.2), countryToArray(3, "italy", 123.3)));
				break;
			case "city":
				tableData=new MyTableData(Arrays.asList(cityToArray(1, 1, "madrid"), cityToArray(2, 1, "bibo"), cityToArray(3, 1, "zevilla"), cityToArray(1, 2, "lisboa"), cityToArray(2, 2, "são vicente")));
				break;
			default:
				throw new ProviderException("Table '" + table + "' not found");
		}
		return tableData;
	}

	@Override
	public Cursor openCursor(String table)
		throws ProviderException
	{
		Map<String, Integer> columnNames=getColumnMapFromTable(table);
		IterableTableData iterableData=getTableData(table);
		return new DefaultCursor(columnNames, iterableData);
	}

	@Override
	public TableMetadata getColumnsMetadataFromTable(String table)
		throws ProviderException
	{
		TableMetadata tableMetadata=new MyTableMetadata();
		return tableMetadata;
	}

	public class MyTableMetadata implements TableMetadata
	{
		private final ColumnMetadata[] columns=createColumns();

		@Override
		public ColumnMetadata getColumnMetadata(int column)
		{
			return this.columns[column];
		}

		private ColumnMetadata[] createColumns()
		{
			// @formatter:off
			ColumnMetadata[] columns= {
				new ColumnMetadata("id", Datatype.INTEGER_NUMBER, false),
				new ColumnMetadata("name", Datatype.TEXT, true),
			};
			// @formatter:oh
			return columns;
		}
	}
}
