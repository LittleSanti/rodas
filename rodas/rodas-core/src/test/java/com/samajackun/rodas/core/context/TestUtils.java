package com.samajackun.rodas.core.context;

import java.util.ServiceConfigurationError;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.MapList;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.ProviderException;

public class TestUtils
{
	// public static Cursor createCursor0()
	// throws ProviderException
	// {
	// List<ColumnMetadata> metadata=new ArrayList<>();
	// metadata.add(new ColumnMetadata("id", Datatype.INTEGER_NUMBER, false));
	// metadata.add(new ColumnMetadata("name", Datatype.TEXT, false));
	// metadata.add(new ColumnMetadata("days", Datatype.INTEGER_NUMBER, true));
	// IterableTableData iterable=new MyIterableTableData(Arrays.asList(new Object[][] {
//			// @formatter:off
//			new Object[] {1,"enero",31},
//			new Object[] {2,"febrero",28},
//			new Object[] {3,"marzo",31},
//			new Object[] {4,"abril",30},
//			new Object[] {5,"mayo",31},
//			new Object[] {6,"junio",30},
//			new Object[] {7,"julio",31},
//			new Object[] {8,"agosto",31},
//			new Object[] {9,"septiembre",30},
//			new Object[] {10,"octubre",31},
//			new Object[] {11,"noviembre",30},
//			new Object[] {12,"diciembre",31},
//			new Object[] {13,"triciembre",41},
//			// @formatter:on
	// }));
	// Cursor cursor=new MyCursor(metadata, iterable);
	// return cursor;
	// }

	public static Cursor createCursor(String table)
		throws ProviderException
	{
		MyProvider provider=new MyProvider();
		MyCursor cursor=new MyCursor(provider.getColumnsMetadataFromTable(table).getListOfColumnMetadata(), provider.getTableData(table));
		return cursor;
	}

	public static Context createContext()
	{
		try
		{
			Cursor cursor1=createCursor("month");
			MapList<String, Cursor> cursors=new MapList<>();
			cursors.put("mes", cursor1);
			MapList<String, IdentifierCoordinates> identifierCoordinatesMap=new MapList<>();
			identifierCoordinatesMap.put("id", new IdentifierCoordinates(0, 0));
			identifierCoordinatesMap.put("name", new IdentifierCoordinates(0, 1));
			identifierCoordinatesMap.put("days", new IdentifierCoordinates(0, 2));
			Context context=new DefaultExecutionContext(cursors, identifierCoordinatesMap);

			// cursor1.next();
			return context;
		}
		catch (ProviderException e)
		{
			throw new ServiceConfigurationError(e.toString(), e);
		}
	}
}
