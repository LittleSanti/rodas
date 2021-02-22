package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.eval.VariablesManager;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.MyIterableTableData;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;

public class DefaultCursorTest
{
	@Test
	public void getNumberOfColumns()
		throws ProviderException,
		CursorException
	{
		DefaultCursor cursor=createCursor1();
		assertEquals(4, cursor.getNumberOfColumns());
	}

	@Test
	public void metadata()
		throws ProviderException,
		CursorException
	{
		DefaultCursor cursor=createCursor1();
		List<ColumnMetaData> metadata=cursor.getMetadata();
		assertEquals(4, metadata.size());
		ColumnMetaData columnMetadata;

		columnMetadata=metadata.get(0);
		assertEquals("id", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(1);
		assertEquals("name", columnMetadata.getName());
		assertEquals(Datatype.TEXT, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(2);
		assertEquals("days", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());

		columnMetadata=metadata.get(3);
		assertEquals("amount", columnMetadata.getName());
		assertEquals(Datatype.DECIMAL_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());
	}

	@Test
	public void getColumnMap()
		throws ProviderException,
		CursorException
	{
		DefaultCursor cursor=createCursor1();
		Map<String, Integer> columnMap=cursor.getColumnMap();
		assertEquals(4, columnMap.size());
		assertEquals(0, columnMap.get("id").intValue());
		assertEquals(1, columnMap.get("name").intValue());
		assertEquals(2, columnMap.get("days").intValue());
		assertEquals(3, columnMap.get("amount").intValue());
		assertNull(columnMap.get("wrongColumn"));
	}

	@Test
	public void iterateData()
		throws ProviderException,
		CursorException
	{
		MyOpenContext context=new MyOpenContext();
		DefaultCursor cursor=createCursor1();
		VariablesContext globalVariablesContext=new StrictVariablesContext();
		globalVariablesContext.set(Name.instanceOf("year"), 2019);
		VariablesManager variablesManager=new StrictVariablesManager(globalVariablesContext);
		context.setVariablesManager(variablesManager);
		variablesManager.pushLocalContext(new CursorVariablesContext(null, cursor));
		try
		{
			RowData rowData;
			assertTrue(cursor.hasNext());
			cursor.next();
			rowData=cursor.getRowData();
			assertEquals("enero", rowData.get(1));
			assertEquals(1, variablesManager.getLocalVariable(Name.instanceOf("id")));
			assertEquals("enero", variablesManager.getLocalVariable(Name.instanceOf("name")));
			assertEquals(31, variablesManager.getLocalVariable(Name.instanceOf("days")));
			assertEquals(0.1d, variablesManager.getLocalVariable(Name.instanceOf("amount")));

			assertTrue(cursor.hasNext());
			cursor.next();
			assertEquals("febrero", rowData.get(1));
			assertEquals(2, variablesManager.getLocalVariable(Name.instanceOf("id")));
			assertEquals("febrero", variablesManager.getLocalVariable(Name.instanceOf("name")));
			assertEquals(28, variablesManager.getLocalVariable(Name.instanceOf("days")));
			assertEquals(0.2d, variablesManager.getLocalVariable(Name.instanceOf("amount")));

			assertTrue(cursor.hasNext());
			cursor.next();
			assertEquals("marzo", rowData.get(1));
			assertEquals(3, variablesManager.getLocalVariable(Name.instanceOf("id")));
			assertEquals("marzo", variablesManager.getLocalVariable(Name.instanceOf("name")));
			assertEquals(31, variablesManager.getLocalVariable(Name.instanceOf("days")));
			assertEquals(0.3d, variablesManager.getLocalVariable(Name.instanceOf("amount")));

			assertTrue(cursor.hasNext());
			cursor.next();
			assertEquals("abril", rowData.get(1));
			assertEquals(4, variablesManager.getLocalVariable(Name.instanceOf("id")));
			assertEquals("abril", variablesManager.getLocalVariable(Name.instanceOf("name")));
			assertEquals(30, variablesManager.getLocalVariable(Name.instanceOf("days")));
			assertEquals(0.4d, variablesManager.getLocalVariable(Name.instanceOf("amount")));

			assertTrue(cursor.hasNext());
			cursor.next();
			assertEquals("mayo", rowData.get(1));
			assertEquals(5, variablesManager.getLocalVariable(Name.instanceOf("id")));
			assertEquals("mayo", variablesManager.getLocalVariable(Name.instanceOf("name")));
			assertEquals(31, variablesManager.getLocalVariable(Name.instanceOf("days")));
			assertEquals(0.5d, variablesManager.getLocalVariable(Name.instanceOf("amount")));

			assertFalse(cursor.hasNext());

			// Comprobemos ahora qué pasa si pido un parámetro global:
			assertEquals(2019, variablesManager.getGlobalVariable(Name.instanceOf("year")));
		}
		catch (VariableNotFoundException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			variablesManager.popLocalContext();
		}
	}

	private List<ColumnMetaData> createMetadata()
	{
		List<ColumnMetaData> metadata=new ArrayList<>();
		metadata.add(new ColumnMetaData("id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetaData("name", Datatype.TEXT, false));
		metadata.add(new ColumnMetaData("days", Datatype.INTEGER_NUMBER, true));
		metadata.add(new ColumnMetaData("amount", Datatype.DECIMAL_NUMBER, true));
		return metadata;
	}

	private DefaultCursor createCursor1()
		throws ProviderException
	{
		List<ColumnMetaData> metadata=createMetadata();
		List<Object[]> data=Arrays.asList(new Object[][] {
			// @formatter:off
			new Object[] {1, "enero", 31, 0.1d},
			new Object[] {2, "febrero", 28, 0.2d},
			new Object[] {3, "marzo", 31, 0.3d},
			new Object[] {4, "abril", 30, 0.4d},
			new Object[] {5, "mayo", 31, 0.5d},
			// @formatter:on
		});
		IterableTableData iterableTableData=new MyIterableTableData(data);
		DefaultCursor cursor=new DefaultCursor(metadata, iterableTableData);
		return cursor;
	}

	private DefaultCursor createCursor0()
		throws ProviderException
	{
		List<ColumnMetaData> metadata=createMetadata();
		List<Object[]> data=new ArrayList<>();
		IterableTableData iterableTableData=new MyIterableTableData(data);
		DefaultCursor cursor=new DefaultCursor(metadata, iterableTableData);
		return cursor;
	}

	@Test
	public void iterateDataOnEmptyCursor()
		throws ProviderException,
		CursorException
	{
		DefaultCursor cursor=createCursor0();
		assertFalse(cursor.hasNext());
	}

	@Test
	public void reset()
		throws ProviderException,
		CursorException
	{
		DefaultCursor cursor=createCursor1();
		RowData rowData;
		assertTrue(cursor.hasNext());
		cursor.next();
		rowData=cursor.getRowData();
		assertEquals("enero", rowData.get(1));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("febrero", rowData.get(1));
		cursor.reset();

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("enero", rowData.get(1));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("febrero", rowData.get(1));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("marzo", rowData.get(1));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("abril", rowData.get(1));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("mayo", rowData.get(1));

		assertFalse(cursor.hasNext());
	}

	@Test
	public void close()
		throws ProviderException,
		CursorException
	{
		DefaultCursor cursor=createCursor1();
		RowData rowData;
		assertTrue(cursor.hasNext());
		rowData=cursor.getRowData();
		cursor.next();
		assertEquals("enero", rowData.get(1));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("febrero", rowData.get(1));
		cursor.close();
	}

	@Test
	public void metadataFromEmptyCursor()
		throws ProviderException,
		CursorException
	{
		DefaultCursor cursor=createCursor0();
		List<ColumnMetaData> metadata=cursor.getMetadata();
		assertEquals(4, metadata.size());
		ColumnMetaData columnMetadata;

		columnMetadata=metadata.get(0);
		assertEquals("id", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(1);
		assertEquals("name", columnMetadata.getName());
		assertEquals(Datatype.TEXT, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(2);
		assertEquals("days", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());

		columnMetadata=metadata.get(3);
		assertEquals("amount", columnMetadata.getName());
		assertEquals(Datatype.DECIMAL_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());
	}
}
