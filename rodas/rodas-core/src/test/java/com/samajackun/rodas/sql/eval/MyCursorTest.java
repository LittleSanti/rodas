package com.samajackun.rodas.sql.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.Datatype;
import com.samajackun.rodas.sql.model.IterableTableData;
import com.samajackun.rodas.sql.model.MyIterableTableData;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.RowData;

public class MyCursorTest
{
	@Test
	public void getNumberOfColumns()
		throws ProviderException,
		CursorException
	{
		MyCursor cursor=createCursor1();
		assertEquals(4, cursor.getNumberOfColumns());
	}

	@Test
	public void metadata()
		throws ProviderException,
		CursorException
	{
		MyCursor cursor=createCursor1();
		List<ColumnMetadata> metadata=cursor.getMetadata();
		assertEquals(4, metadata.size());
		ColumnMetadata columnMetadata;

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
		MyCursor cursor=createCursor1();
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
		MyCursor cursor=createCursor1();
		RowData rowData;
		assertTrue(cursor.hasNext());
		cursor.next();
		rowData=cursor.getRowData();
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

	private List<ColumnMetadata> createMetadata()
	{
		List<ColumnMetadata> metadata=new ArrayList<>();
		metadata.add(new ColumnMetadata("id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetadata("name", Datatype.TEXT, false));
		metadata.add(new ColumnMetadata("days", Datatype.INTEGER_NUMBER, true));
		metadata.add(new ColumnMetadata("amount", Datatype.DECIMAL_NUMBER, true));
		return metadata;
	}

	private MyCursor createCursor1()
		throws ProviderException
	{
		List<ColumnMetadata> metadata=createMetadata();
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
		MyCursor cursor=new MyCursor(metadata, iterableTableData);
		return cursor;
	}

	private MyCursor createCursor0()
		throws ProviderException
	{
		List<ColumnMetadata> metadata=createMetadata();
		List<Object[]> data=new ArrayList<>();
		IterableTableData iterableTableData=new MyIterableTableData(data);
		MyCursor cursor=new MyCursor(metadata, iterableTableData);
		return cursor;
	}

	@Test
	public void iterateDataOnEmptyCursor()
		throws ProviderException,
		CursorException
	{
		MyCursor cursor=createCursor0();
		assertFalse(cursor.hasNext());
	}

	@Test
	public void reset()
		throws ProviderException,
		CursorException
	{
		MyCursor cursor=createCursor1();
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
		MyCursor cursor=createCursor1();
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
		MyCursor cursor=createCursor0();
		List<ColumnMetadata> metadata=cursor.getMetadata();
		assertEquals(4, metadata.size());
		ColumnMetadata columnMetadata;

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
