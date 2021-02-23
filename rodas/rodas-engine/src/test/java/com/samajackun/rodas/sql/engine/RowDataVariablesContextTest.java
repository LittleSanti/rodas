package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.DefaultContext;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.MyIterableTableData;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.ProviderException;

public class RowDataVariablesContextTest
{
	private DefaultCursor createCursor()
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

	private Context createContext(Cursor cursor)
	{
		DefaultContext context=new DefaultContext();
		context.setProvider(new MyProvider());
		Map<String, Integer> columnMap=new HashMap<>();
		columnMap.put("month_id", 0);
		columnMap.put("month_name", 1);
		columnMap.put("days", 2);
		columnMap.put("amount", 3);
		VariablesContext parent=new StrictVariablesContext();
		RowDataVariablesContext rowDataVariablesContext=new RowDataVariablesContext(parent, columnMap);
		rowDataVariablesContext.setCurrentCursor(cursor);
		context.setVariablesManager(new StrictVariablesManager(parent));
		context.getVariablesManager().pushLocalContext(rowDataVariablesContext);
		context.setEvaluatorFactory(new DefaultEvaluatorFactory());
		return context;
	}

	@Test
	public void test()
		throws ProviderException,
		VariableNotFoundException,
		CursorException
	{
		Cursor cursor=createCursor();
		Context context=createContext(cursor);

		cursor.next();
		assertEquals(1, context.getVariablesManager().getNearestVariable(Name.instanceOf("month_id")));
		assertEquals("enero", context.getVariablesManager().getNearestVariable(Name.instanceOf("month_name")));

		cursor.next();
		assertEquals(2, context.getVariablesManager().getNearestVariable(Name.instanceOf("month_id")));
		assertEquals("febrero", context.getVariablesManager().getNearestVariable(Name.instanceOf("month_name")));
	}
}
