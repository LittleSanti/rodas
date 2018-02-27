package com.samajackun.rodas.sql.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;

import com.samajackun.rodas.sql.eval.ColumnNotFoundException;
import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.DefaultContext;
import com.samajackun.rodas.sql.eval.MyCursor;
import com.samajackun.rodas.sql.eval.PrefixNotFoundException;

public class TestUtils
{
	public static Cursor createCursor()
		throws ProviderException
	{
		List<ColumnMetadata> metadata=new ArrayList<>();
		metadata.add(new ColumnMetadata("id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetadata("name", Datatype.TEXT, false));
		metadata.add(new ColumnMetadata("days", Datatype.INTEGER_NUMBER, true));
		IterableTableData iterable=new MyIterableTableData(Arrays.asList(new Object[][] {
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
			new Object[] {13,"triciembre",41},
			// @formatter:on
		}));
		Cursor cursor=new MyCursor(metadata, iterable);
		return cursor;
	}

	public static Context createContext()
	{
		try
		{
			Map<String, Cursor> cursors=new HashMap<>();
			Cursor cursor1=createCursor();
			cursors.put("mes", cursor1);
			DefaultContext context=new DefaultContext(cursors);
			context.bindPublicColumn(null, "id");
			context.bindPublicColumn(null, "name");
			context.bindPublicColumn(null, "days");

			cursor1.next();
			return context;
		}
		catch (CursorException | ColumnNotFoundException | ProviderException | PrefixNotFoundException e)
		{
			throw new ServiceConfigurationError(e.toString(), e);
		}
	}

}
