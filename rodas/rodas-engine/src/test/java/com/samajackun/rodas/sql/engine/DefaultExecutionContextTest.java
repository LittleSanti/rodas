package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.sql.eval.ColumnNotFoundException;
import com.samajackun.rodas.sql.eval.IndexNotBoundException;
import com.samajackun.rodas.sql.eval.MyCursor;
import com.samajackun.rodas.sql.eval.NameNotBoundException;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.IterableTableData;
import com.samajackun.rodas.sql.model.MyIterableTableData;
import com.samajackun.rodas.sql.model.ProviderException;

public class DefaultExecutionContextTest
{
	@Test
	public void bindAllColumns()
		throws ProviderException
	{
		Map<String, Cursor> cursors=new HashMap<>();
		Cursor cursor1=createCursor();
		cursors.put("mes", cursor1);
		DefaultExecutionContext context=new DefaultExecutionContext(cursors);
		try
		{
			context.bindPublicColumn(null, "id");
			context.bindPublicColumn(null, "name");
			context.bindPublicColumn(null, "days");

			cursor1.next();
			assertEquals(1, context.getColumnByName("id"));
			assertEquals("enero", context.getColumnByName("name"));
			assertEquals(31, context.getColumnByName("days"));
			assertEquals(1, context.getColumnByIndex(0));
			assertEquals("enero", context.getColumnByIndex(1));
			assertEquals(31, context.getColumnByIndex(2));

			cursor1.next();
			assertEquals(2, context.getColumnByName("id"));
			assertEquals("febrero", context.getColumnByName("name"));
			assertEquals(28, context.getColumnByName("days"));
			assertEquals(2, context.getColumnByIndex(0));
			assertEquals("febrero", context.getColumnByIndex(1));
			assertEquals(28, context.getColumnByIndex(2));
		}
		catch (NameNotBoundException | CursorException | ColumnNotFoundException | IndexNotBoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void bindOneColumns()
		throws ProviderException
	{
		Map<String, Cursor> cursors=new HashMap<>();
		Cursor cursor1=createCursor();
		cursors.put("mes", cursor1);
		DefaultExecutionContext context=new DefaultExecutionContext(cursors);
		try
		{
			context.bindPublicColumn(null, "name");

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
			assertEquals("enero", context.getColumnByIndex(0));
		}
		catch (NameNotBoundException | CursorException | ColumnNotFoundException | IndexNotBoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void bindAllPrefixedColumnsInAliasedCursor()
		throws ProviderException
	{
		Map<String, Cursor> cursors=new HashMap<>();
		Cursor cursor1=createCursor();
		cursors.put("mes", cursor1);
		DefaultExecutionContext context=new DefaultExecutionContext(cursors);
		try
		{
			context.bindPublicColumn("mes", "id");
			context.bindPublicColumn("mes", "name");
			context.bindPublicColumn("mes", "days");

			cursor1.next();
			assertEquals(1, context.getColumnByName("id"));
			assertEquals("enero", context.getColumnByName("name"));
			assertEquals(31, context.getColumnByName("days"));
			assertEquals(1, context.getColumnByIndex(0));
			assertEquals("enero", context.getColumnByIndex(1));
			assertEquals(31, context.getColumnByIndex(2));

			cursor1.next();
			assertEquals(2, context.getColumnByName("id"));
			assertEquals("febrero", context.getColumnByName("name"));
			assertEquals(28, context.getColumnByName("days"));
			assertEquals(2, context.getColumnByIndex(0));
			assertEquals("febrero", context.getColumnByIndex(1));
			assertEquals(28, context.getColumnByIndex(2));
		}
		catch (NameNotBoundException | CursorException | ColumnNotFoundException | IndexNotBoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	private Cursor createCursor()
		throws ProviderException
	{
		Map<String, Integer> columnMap=new HashMap<>();
		columnMap.put("id", 0);
		columnMap.put("name", 1);
		columnMap.put("days", 2);
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
		Cursor cursor=new MyCursor(columnMap, iterable);
		return cursor;
	}
}
