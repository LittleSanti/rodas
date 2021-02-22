package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.MyIterableTableData;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;

public class CrossCursorTest
{
	@Test
	public void getNumberOfColumns()
		throws ProviderException,
		CursorException
	{
		CrossCursor cursor=createCrossCursor();
		assertEquals(6, cursor.getNumberOfColumns());
	}

	@Test
	public void metadata()
		throws ProviderException,
		CursorException
	{
		CrossCursor cursor=createCrossCursor();
		List<ColumnMetaData> metadata=cursor.getMetadata();
		assertEquals(6, metadata.size());
		ColumnMetaData columnMetadata;
		int i=0;

		columnMetadata=metadata.get(i++);
		assertEquals("month_id", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("month_name", columnMetadata.getName());
		assertEquals(Datatype.TEXT, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("days", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("amount", columnMetadata.getName());
		assertEquals(Datatype.DECIMAL_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("day_id", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("day_name", columnMetadata.getName());
		assertEquals(Datatype.TEXT, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());
	}

	@Test
	public void getColumnMap()
		throws ProviderException,
		CursorException
	{
		CrossCursor cursor=createCrossCursor();
		Map<String, Integer> columnMap=cursor.getColumnMap();
		assertEquals(6, columnMap.size());
		assertEquals(0, columnMap.get("month_id").intValue());
		assertEquals(1, columnMap.get("month_name").intValue());
		assertEquals(2, columnMap.get("days").intValue());
		assertEquals(3, columnMap.get("amount").intValue());
		assertEquals(4, columnMap.get("day_id").intValue());
		assertEquals(5, columnMap.get("day_name").intValue());
		assertNull(columnMap.get("wrongColumn"));
	}

	@Test
	public void crossBetweenTwoFull()
		throws ProviderException,
		CursorException
	{
		CrossCursor cursor=createCrossCursor();
		RowData rowData=cursor.getRowData();
		// while (cursor.hasNext())
		// {
		// cursor.next();
		// for (int i=0; i < cursor.getNumberOfColumns(); i++)
		// {
		// System.out.print(rowData.get(i) + "\t");
		// }
		// System.out.println();
		// }
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("enero", rowData.get(1));
		assertEquals("lunes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("enero", rowData.get(1));
		assertEquals("martes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("enero", rowData.get(1));
		assertEquals("miércoles", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("febrero", rowData.get(1));
		assertEquals("lunes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("febrero", rowData.get(1));
		assertEquals("martes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("febrero", rowData.get(1));
		assertEquals("miércoles", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("marzo", rowData.get(1));
		assertEquals("lunes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("marzo", rowData.get(1));
		assertEquals("martes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("marzo", rowData.get(1));
		assertEquals("miércoles", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("abril", rowData.get(1));
		assertEquals("lunes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("abril", rowData.get(1));
		assertEquals("martes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("abril", rowData.get(1));
		assertEquals("miércoles", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("mayo", rowData.get(1));
		assertEquals("lunes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("mayo", rowData.get(1));
		assertEquals("martes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("mayo", rowData.get(1));
		assertEquals("miércoles", rowData.get(5));

		assertFalse(cursor.hasNext());
	}

	private CrossCursor createCrossCursor()
		throws CursorException,
		ProviderException
	{
		CrossCursor crossCursor=new CrossCursor(Arrays.asList(createCursor1(), createCursor2()));
		return crossCursor;
	}

	private DefaultCursor createCursor0()
		throws ProviderException
	{
		List<ColumnMetaData> metadata=new ArrayList<>();
		metadata.add(new ColumnMetaData("month_id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetaData("month_name", Datatype.TEXT, false));
		metadata.add(new ColumnMetaData("days", Datatype.INTEGER_NUMBER, true));
		metadata.add(new ColumnMetaData("amount", Datatype.DECIMAL_NUMBER, true));
		List<Object[]> data=Arrays.asList(new Object[][] {});
		IterableTableData iterableTableData=new MyIterableTableData(data);
		DefaultCursor cursor=new DefaultCursor(metadata, iterableTableData);
		return cursor;
	}

	private DefaultCursor createCursor1()
		throws ProviderException
	{
		List<ColumnMetaData> metadata=new ArrayList<>();
		metadata.add(new ColumnMetaData("month_id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetaData("month_name", Datatype.TEXT, false));
		metadata.add(new ColumnMetaData("days", Datatype.INTEGER_NUMBER, true));
		metadata.add(new ColumnMetaData("amount", Datatype.DECIMAL_NUMBER, true));
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

	private DefaultCursor createCursor2()
		throws ProviderException
	{
		List<ColumnMetaData> metadata=new ArrayList<>();
		metadata.add(new ColumnMetaData("day_id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetaData("day_name", Datatype.TEXT, false));
		List<Object[]> data=Arrays.asList(new Object[][] {
			// @formatter:off
			new Object[] {1, "lunes"},
			new Object[] {2, "martes"},
			new Object[] {3, "miércoles"},
			// @formatter:on
		});
		IterableTableData iterableTableData=new MyIterableTableData(data);
		DefaultCursor cursor=new DefaultCursor(metadata, iterableTableData);
		return cursor;
	}

	@Test
	public void crossBetweenEmptyAndFull()
		throws ProviderException,
		CursorException
	{
		CrossCursor cursor=new CrossCursor(Arrays.asList(createCursor0(), createCursor2()));
		// getNumberOfColumns:
		assertEquals(6, cursor.getNumberOfColumns());
		// getColumnMap:
		Map<String, Integer> columnMap=cursor.getColumnMap();
		assertEquals(6, columnMap.size());
		assertEquals(0, columnMap.get("month_id").intValue());
		assertEquals(1, columnMap.get("month_name").intValue());
		assertEquals(2, columnMap.get("days").intValue());
		assertEquals(3, columnMap.get("amount").intValue());
		assertEquals(4, columnMap.get("day_id").intValue());
		assertEquals(5, columnMap.get("day_name").intValue());
		assertNull(columnMap.get("wrongColumn"));
		// Metadata:
		List<ColumnMetaData> metadata=cursor.getMetadata();
		assertEquals(6, metadata.size());
		ColumnMetaData columnMetadata;
		int i=0;

		columnMetadata=metadata.get(i++);
		assertEquals("month_id", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("month_name", columnMetadata.getName());
		assertEquals(Datatype.TEXT, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("days", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("amount", columnMetadata.getName());
		assertEquals(Datatype.DECIMAL_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("day_id", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("day_name", columnMetadata.getName());
		assertEquals(Datatype.TEXT, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		// RowData rowData=cursor.getRowData();
		// while (cursor.hasNext())
		// {
		// cursor.next();
		// for (int j=0; j < cursor.getNumberOfColumns(); i++)
		// {
		// System.out.print(rowData.get(j) + "\t");
		// }
		// System.out.println();
		// }
		assertFalse(cursor.hasNext());
	}

	@Test
	public void crossBetweenFullAndEmpty()
		throws ProviderException,
		CursorException
	{
		CrossCursor cursor=new CrossCursor(Arrays.asList(createCursor2(), createCursor0()));
		// getNumberOfColumns:
		assertEquals(6, cursor.getNumberOfColumns());
		// getColumnMap:
		Map<String, Integer> columnMap=cursor.getColumnMap();
		assertEquals(6, columnMap.size());
		assertEquals(0, columnMap.get("day_id").intValue());
		assertEquals(1, columnMap.get("day_name").intValue());
		assertEquals(2, columnMap.get("month_id").intValue());
		assertEquals(3, columnMap.get("month_name").intValue());
		assertEquals(4, columnMap.get("days").intValue());
		assertEquals(5, columnMap.get("amount").intValue());
		assertNull(columnMap.get("wrongColumn"));
		// Metadata:
		List<ColumnMetaData> metadata=cursor.getMetadata();
		assertEquals(6, metadata.size());
		ColumnMetaData columnMetadata;
		int i=0;

		columnMetadata=metadata.get(i++);
		assertEquals("day_id", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("day_name", columnMetadata.getName());
		assertEquals(Datatype.TEXT, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("month_id", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("month_name", columnMetadata.getName());
		assertEquals(Datatype.TEXT, columnMetadata.getDatatype());
		assertFalse(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("days", columnMetadata.getName());
		assertEquals(Datatype.INTEGER_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());

		columnMetadata=metadata.get(i++);
		assertEquals("amount", columnMetadata.getName());
		assertEquals(Datatype.DECIMAL_NUMBER, columnMetadata.getDatatype());
		assertTrue(columnMetadata.isNullable());

		// RowData rowData=cursor.getRowData();
		// while (cursor.hasNext())
		// {
		// cursor.next();
		// for (int j=0; j < cursor.getNumberOfColumns(); i++)
		// {
		// System.out.print(rowData.get(j) + "\t");
		// }
		// System.out.println();
		// }
		assertFalse(cursor.hasNext());
	}
}
