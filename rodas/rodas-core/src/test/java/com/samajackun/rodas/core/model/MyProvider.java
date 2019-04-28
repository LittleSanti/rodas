package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.DefaultCursor;

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
			case "month":
				columns=map(Arrays.asList("id", "name", "days"));
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
			case "month":
				tableData=new MyTableData(Arrays.asList(
				// @formatter:off
					new Object[] {1,"enero",31},
					new Object[] {2,"febrero",28},
					new Object[] {3,"marzo",31},
					new Object[] {4,"abril",30},
					new Object[] {5,"mayo",31},
					new Object[] {6,"junio",30},
					new Object[] {7,"julio",31},
					new Object[] {8,"agosto",31},
					new Object[] {9,"septiembre",30},
					new Object[] {10,"octubre",31},
					new Object[] {11,"noviembre",30},
					new Object[] {12,"diciembre",31},
					new Object[] {13,"triciembre",41}));
				// @formatter:on
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
		IterableTableData iterableData=getTableData(table);
		return new DefaultCursor(getColumnsMetadataFromTable(table).getListOfColumnMetadata(), iterableData);
	}

	@Override
	public TableMetadata getColumnsMetadataFromTable(String table)
		throws ProviderException
	{
		TableMetadata tableMetadata=new MyTableMetadata(table);
		List<ColumnMetadata> metadata=tableMetadata.getListOfColumnMetadata();
		switch (table)
		{
			case "country":
				metadata.add(new ColumnMetadata("idCountry", Datatype.INTEGER_NUMBER, false));
				metadata.add(new ColumnMetadata("name", Datatype.TEXT, false));
				metadata.add(new ColumnMetadata("area", Datatype.DECIMAL_NUMBER, true));
				break;
			case "city":
				metadata.add(new ColumnMetadata("idCity", Datatype.INTEGER_NUMBER, false));
				metadata.add(new ColumnMetadata("idCountry", Datatype.INTEGER_NUMBER, false));
				metadata.add(new ColumnMetadata("name", Datatype.TEXT, false));
				break;
			case "month":
				metadata.add(new ColumnMetadata("id", Datatype.INTEGER_NUMBER, false));
				metadata.add(new ColumnMetadata("name", Datatype.TEXT, false));
				metadata.add(new ColumnMetadata("days", Datatype.INTEGER_NUMBER, true));
				break;
		}
		return tableMetadata;
	}

	public class MyTableMetadata implements TableMetadata
	{
		private final String name;

		private final List<ColumnMetadata> columns=new ArrayList<>();

		public MyTableMetadata(String name)
		{
			super();
			this.name=name;
		}

		@Override
		public ColumnMetadata getColumnMetadata(int column)
		{
			return this.columns.get(column);
		}

		@Override
		public String getName()
		{
			return this.name;
		}

		@Override
		public List<ColumnMetadata> getListOfColumnMetadata()
		{
			return this.columns;
		}
	}
}
