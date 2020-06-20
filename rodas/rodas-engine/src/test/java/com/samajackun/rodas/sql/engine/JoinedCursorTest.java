package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.eval.ColumnNotFoundException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.MapList;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.eval.PrefixNotFoundException;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.BooleanExpression;
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.EqualsExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.MyIterableTableData;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableSource;

public class JoinedCursorTest
{
	@Test
	public void getNumberOfColumns()
		throws ProviderException,
		CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		JoinedCursor cursor=createJoinedCursor();
		assertEquals(6, cursor.getNumberOfColumns());
	}

	@Test
	public void metadata()
		throws ProviderException,
		CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		JoinedCursor cursor=createJoinedCursor();
		List<ColumnMetadata> metadata=cursor.getMetadata();
		assertEquals(6, metadata.size());
		ColumnMetadata columnMetadata;
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
		CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		JoinedCursor cursor=createJoinedCursor();
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
	public void iterateData()
		throws ProviderException,
		CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException

	{
		JoinedCursor cursor=createJoinedCursor();
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
		assertEquals("febrero", rowData.get(1));
		assertEquals("martes", rowData.get(5));

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("marzo", rowData.get(1));
		assertEquals("miércoles", rowData.get(5));

		assertFalse(cursor.hasNext());
	}

	private JoinedCursor createJoinedCursor()
		throws CursorException,
		ProviderException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		BooleanExpression condition=new EqualsExpression("=", new IdentifierExpression("c1", "month_id"), new IdentifierExpression("c2", "day_id"));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		Cursor cursor1=createCursor1();
		Cursor cursor2=createCursor2();
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		Map<String, Cursor> cursorMap=new HashMap<>();
		cursorMap.put("c1", cursor1);
		cursorMap.put("c2", cursor2);
		VariablesContext newVariablesContext=new CursorMapVariablesContext(context.getVariablesManager().peekLocalContext(), cursorMap);
		context.getVariablesManager().pushLocalContext(newVariablesContext);
		context.setProvider(new MyProvider());
		JoinedCursor joinedCursor=new JoinedCursor(cursor1, cursor2, condition, evaluatorFactory, context);
		return joinedCursor;
	}

	private DefaultCursor createCursor1()
		throws ProviderException
	{
		List<ColumnMetadata> metadata=new ArrayList<>();
		metadata.add(new ColumnMetadata("month_id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetadata("month_name", Datatype.TEXT, false));
		metadata.add(new ColumnMetadata("days", Datatype.INTEGER_NUMBER, true));
		metadata.add(new ColumnMetadata("amount", Datatype.DECIMAL_NUMBER, true));
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
		List<ColumnMetadata> metadata=new ArrayList<>();
		metadata.add(new ColumnMetadata("day_id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetadata("day_name", Datatype.TEXT, false));
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

	private DefaultCursor createCursor0()
		throws ProviderException
	{
		List<ColumnMetadata> metadata=new ArrayList<>();
		metadata.add(new ColumnMetadata("month_id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetadata("month_name", Datatype.TEXT, false));
		metadata.add(new ColumnMetadata("days", Datatype.INTEGER_NUMBER, true));
		metadata.add(new ColumnMetadata("amount", Datatype.DECIMAL_NUMBER, true));
		List<Object[]> data=Arrays.asList(new Object[][] {});
		IterableTableData iterableTableData=new MyIterableTableData(data);
		DefaultCursor cursor=new DefaultCursor(metadata, iterableTableData);
		return cursor;
	}

	@Test
	public void joinBetweenEmptyAndFull()
		throws ProviderException,
		CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		BooleanExpression condition=new EqualsExpression("=", new IdentifierExpression("c1", "month_id"), new IdentifierExpression("c2", "day_id"));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		Cursor cursor1=createCursor0();
		Cursor cursor2=createCursor2();
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		Map<String, Cursor> cursorMap=new HashMap<>();
		cursorMap.put("c1", cursor1);
		cursorMap.put("c2", cursor2);
		VariablesContext newVariablesContext=new CursorMapVariablesContext(context.getVariablesManager().peekLocalContext(), cursorMap);
		context.getVariablesManager().pushLocalContext(newVariablesContext);
		context.setProvider(new MyProvider());
		JoinedCursor joinedCursor=new JoinedCursor(cursor1, cursor2, condition, evaluatorFactory, context);

		// getNumberOfColumns:
		assertEquals(6, joinedCursor.getNumberOfColumns());
		// getColumnMap:
		Map<String, Integer> columnMap=joinedCursor.getColumnMap();
		assertEquals(6, columnMap.size());
		assertEquals(0, columnMap.get("month_id").intValue());
		assertEquals(1, columnMap.get("month_name").intValue());
		assertEquals(2, columnMap.get("days").intValue());
		assertEquals(3, columnMap.get("amount").intValue());
		assertEquals(4, columnMap.get("day_id").intValue());
		assertEquals(5, columnMap.get("day_name").intValue());
		assertNull(columnMap.get("wrongColumn"));
		// Metadata:
		List<ColumnMetadata> metadata=joinedCursor.getMetadata();
		assertEquals(6, metadata.size());
		ColumnMetadata columnMetadata;
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

		assertFalse(joinedCursor.hasNext());
	}

	@Test
	public void joinBetweenFullAndFull()
		throws ProviderException,
		CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		BooleanExpression condition=new EqualsExpression("=", new IdentifierExpression("c1", "month_id"), new IdentifierExpression("c2", "day_id"));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		Cursor cursor1=createCursor1();
		Cursor cursor2=createCursor2();
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		Map<String, Cursor> cursorMap=new HashMap<>();
		cursorMap.put("c1", cursor1);
		cursorMap.put("c2", cursor2);
		VariablesContext newVariablesContext=new CursorMapVariablesContext(context.getVariablesManager().peekLocalContext(), cursorMap);
		context.getVariablesManager().pushLocalContext(newVariablesContext);
		context.setProvider(new MyProvider());
		JoinedCursor joinedCursor=new JoinedCursor(cursor1, cursor2, condition, evaluatorFactory, context);
		RowData row=joinedCursor.getRowData();
		assertTrue(joinedCursor.hasNext());
		joinedCursor.next();
		assertEquals("enero", row.get(1));
		assertEquals("lunes", row.get(5));
		assertTrue(joinedCursor.hasNext());
		joinedCursor.next();
		assertEquals("febrero", row.get(1));
		assertEquals("martes", row.get(5));
		assertTrue(joinedCursor.hasNext());
		joinedCursor.next();
		assertEquals("marzo", row.get(1));
		assertEquals("miércoles", row.get(5));
		assertFalse(joinedCursor.hasNext());
	}

	@Test
	public void crossBetweenFullAndEmpty()
		throws ProviderException,
		CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		BooleanExpression condition=new EqualsExpression("=", new IdentifierExpression("month_id"), new IdentifierExpression("day_id"));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		Map<String, Cursor> cursorMap=new HashMap<>();
		Cursor cursor1=createCursor2();
		Cursor cursor2=createCursor0();
		cursorMap.put("x1", cursor1);
		cursorMap.put("x2", cursor2);

		Provider provider=new MyProvider();
		MapList<String, Source> sources=new MapList<>();
		TableSource source1=new TableSource("country");
		sources.put("country", source1);
		TableSource source2=new TableSource("month");
		sources.put("month", source2);
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.setProvider(provider);
		VariablesContext varContext=new CursorMapVariablesContext(context.getVariablesManager().peekLocalContext(), cursorMap);
		context.getVariablesManager().pushLocalContext(varContext);
		// context.bindPrivateColumn(null, "id");
		// context.bindPrivateColumn(null, "day_id");
		// Context executionContext=context.toExecutionContext(cursorMap);
		JoinedCursor joinedCursor=new JoinedCursor(cursor1, cursor2, condition, evaluatorFactory, context);

		// getNumberOfColumns:
		assertEquals(6, joinedCursor.getNumberOfColumns());
		// getColumnMap:
		Map<String, Integer> columnMap=joinedCursor.getColumnMap();
		assertEquals(6, columnMap.size());
		assertEquals(0, columnMap.get("day_id").intValue());
		assertEquals(1, columnMap.get("day_name").intValue());
		assertEquals(2, columnMap.get("month_id").intValue());
		assertEquals(3, columnMap.get("month_name").intValue());
		assertEquals(4, columnMap.get("days").intValue());
		assertEquals(5, columnMap.get("amount").intValue());
		assertNull(columnMap.get("wrongColumn"));
		// Metadata:
		List<ColumnMetadata> metadata=joinedCursor.getMetadata();
		assertEquals(6, metadata.size());
		ColumnMetadata columnMetadata;
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

		RowData rowData=joinedCursor.getRowData();
		while (joinedCursor.hasNext())
		{
			joinedCursor.next();
			for (int j=0; j < joinedCursor.getNumberOfColumns(); i++)
			{
				System.out.print(rowData.get(j) + "\t");
			}
			System.out.println();
		}
		// hasNext:
		assertFalse(joinedCursor.hasNext());
	}
}
