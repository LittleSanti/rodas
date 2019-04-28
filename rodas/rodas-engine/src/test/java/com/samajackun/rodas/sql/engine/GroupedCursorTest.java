package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.context.TestUtils;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;

public class GroupedCursorTest
{
	@Test
	public void groupByOneIntColumn()
		throws CursorException,
		ProviderException
	{
		Cursor cursor=TestUtils.createCursor("month");
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<Expression> groups=new ArrayList<>();
		groups.add(new IdentifierExpression("days"));
		Map<String, AliasedExpression> selectExpressionMap=new HashMap<>();
		cursor=new GroupedCursor(cursor, context, evaluatorFactory, groups, selectExpressionMap);
		while (cursor.hasNext())
		{
			cursor.next();
			RowData row=cursor.getRowData();
			System.out.println(row.get(0));
		}
	}

	private static void assertListEquals(Cursor cursor, int column, List<Object> expectedValues)
		throws CursorException
	{
		for (Object expectedValue : expectedValues)
		{
			assertTrue(cursor.hasNext());
			cursor.next();
			assertEquals(expectedValue, cursor.getRowData().get(column));
		}
	}

}
