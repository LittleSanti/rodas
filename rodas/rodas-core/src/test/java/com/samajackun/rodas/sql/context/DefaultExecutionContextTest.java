package com.samajackun.rodas.sql.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.MapList;
import com.samajackun.rodas.sql.eval.MyCursor;
import com.samajackun.rodas.sql.eval.NameNotBoundException;
import com.samajackun.rodas.sql.eval.PrefixNotFoundException;
import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.Datatype;
import com.samajackun.rodas.sql.model.IterableTableData;
import com.samajackun.rodas.sql.model.MyIterableTableData;
import com.samajackun.rodas.sql.model.ProviderException;

public class DefaultExecutionContextTest
{
	@Test
	public void bindAllColumns()
		throws ProviderException,
		PrefixNotFoundException
	{
		Cursor cursor1=createCursor();
		MapList<String, Cursor> cursors=new MapList<>();
		cursors.put("x1", cursor1);
		MapList<String, IdentifierCoordinates> identifierCoordinatesMap=new MapList<>();
		identifierCoordinatesMap.put("id", new IdentifierCoordinates(0, 0));
		identifierCoordinatesMap.put("name", new IdentifierCoordinates(0, 1));
		identifierCoordinatesMap.put("days", new IdentifierCoordinates(0, 2));
		Context context=new DefaultExecutionContext(cursors, identifierCoordinatesMap);
		try
		{
			// context.bindPublicColumn(null, "id");
			// context.bindPublicColumn(null, "name");
			// context.bindPublicColumn(null, "days");

			cursor1.next();
			assertEquals(1, context.getColumnByName("id"));
			assertEquals("enero", context.getColumnByName("name"));
			assertEquals(31, context.getColumnByName("days"));
			// assertEquals(1, context.getColumnByIndex(0));
			// assertEquals("enero", context.getColumnByIndex(1));
			// assertEquals(31, context.getColumnByIndex(2));

			cursor1.next();
			assertEquals(2, context.getColumnByName("id"));
			assertEquals("febrero", context.getColumnByName("name"));
			assertEquals(28, context.getColumnByName("days"));
			// assertEquals(2, context.getColumnByIndex(0));
			// assertEquals("febrero", context.getColumnByIndex(1));
			// assertEquals(28, context.getColumnByIndex(2));
		}
		catch (NameNotBoundException | CursorException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void bindOneColumns()
		throws ProviderException,
		PrefixNotFoundException
	{
		Cursor cursor1=createCursor();
		MapList<String, Cursor> cursors=new MapList<>();
		cursors.put("x1", cursor1);
		MapList<String, IdentifierCoordinates> identifierCoordinatesMap=new MapList<>();
		identifierCoordinatesMap.put("name", new IdentifierCoordinates(0, 1));
		Context context=new DefaultExecutionContext(cursors, identifierCoordinatesMap);
		try
		{
			cursor1.next();
			try
			{
				context.getColumnByName("id");
			}
			catch (NameNotBoundException e)
			{
				assertEquals("id", e.getName());
			}
			assertEquals("enero", context.getColumnByName("name"));
		}
		catch (NameNotBoundException | CursorException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	// @Test
	// public void bindAllPrefixedColumnsInAliasedCursor()
	// throws ProviderException,
	// PrefixNotFoundException
	// {
	// Map<String, Cursor> cursors=new HashMap<>();
	// Cursor cursor1=createCursor();
	// cursors.put("mes", cursor1);
	// ExecutionContext context=new ExecutionContext(cursors);
	// try
	// {
	// context.bindPublicColumn("mes", "id");
	// context.bindPublicColumn("mes", "name");
	// context.bindPublicColumn("mes", "days");
	//
	// cursor1.next();
	// assertEquals(1, context.getColumnByName("id"));
	// assertEquals(1, context.getColumnByName("id", "mes"));
	// assertEquals("enero", context.getColumnByName("name", "mes"));
	// assertEquals("enero", context.getColumnByName("name"));
	// try
	// {
	// context.getColumnByName("wrong");
	// fail("Expected NameNotBoundException");
	// }
	// catch (NameNotBoundException e)
	// {
	// assertEquals("wrong", e.getName());
	// }
	// assertEquals(31, context.getColumnByName("days"));
	// assertEquals(31, context.getColumnByName("days", "mes"));
	// assertEquals(1, context.getColumnByIndex(0));
	// assertEquals("enero", context.getColumnByIndex(1));
	// assertEquals(31, context.getColumnByIndex(2));
	//
	// cursor1.next();
	// assertEquals(2, context.getColumnByName("id"));
	// assertEquals(2, context.getColumnByName("id", "mes"));
	// assertEquals("febrero", context.getColumnByName("name"));
	// assertEquals("febrero", context.getColumnByName("name", "mes"));
	// assertEquals(28, context.getColumnByName("days"));
	// assertEquals(28, context.getColumnByName("days", "mes"));
	// assertEquals(2, context.getColumnByIndex(0));
	// assertEquals("febrero", context.getColumnByIndex(1));
	// assertEquals(28, context.getColumnByIndex(2));
	// }
	// catch (NameNotBoundException | CursorException | ColumnNotFoundException | IndexNotBoundException e)
	// {
	// e.printStackTrace();
	// fail(e.toString());
	// }
	// }

	private Cursor createCursor()
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
}
