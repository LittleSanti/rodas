package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.core.context.TestUtils;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.OrderClause;
import com.samajackun.rodas.core.model.ProviderException;

public class OrderedCursorTest
{
	@Test
	public void orderByOneStringColumn()
		throws CursorException,
		ProviderException,
		EvaluationException
	{
		Cursor cursor=TestUtils.createCursor("month");
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<OrderClause> clauses=new ArrayList<>();
		clauses.add(new OrderClause(new IdentifierExpression("name"), true));
		cursor=new OrderedCursor(cursor, context, evaluatorFactory, clauses);
		assertListEquals(cursor, 1, Arrays.asList("abril", "agosto", "diciembre", "enero", "febrero", "julio", "junio", "marzo", "mayo", "noviembre", "octubre", "septiembre", "triciembre"));
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

	@Test
	public void orderByOneIntColumnUnique()
		throws CursorException,
		ProviderException,
		EvaluationException
	{
		Cursor cursor=TestUtils.createCursor("month");
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<OrderClause> clauses=new ArrayList<>();
		clauses.add(new OrderClause(new IdentifierExpression("id"), true));
		cursor=new OrderedCursor(cursor, context, evaluatorFactory, clauses);
		assertListEquals(cursor, 1, Arrays.asList("enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre", "triciembre"));
	}

	@Test
	public void orderByOneIntColumnNonUnique()
		throws CursorException,
		ProviderException,
		EvaluationException
	{
		Cursor cursor=TestUtils.createCursor("month");
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<OrderClause> clauses=new ArrayList<>();
		clauses.add(new OrderClause(new IdentifierExpression("days"), true));
		cursor=new OrderedCursor(cursor, context, evaluatorFactory, clauses);
		assertListEquals(cursor, 1, Arrays.asList("febrero", "abril", "junio", "septiembre", "noviembre", "enero", "marzo", "mayo", "julio", "agosto", "octubre", "diciembre", "triciembre"));
	}

	@Test
	public void orderByOneIntColumnNonUniqueAndOneReverseString()
		throws CursorException,
		ProviderException,
		EvaluationException
	{
		Cursor cursor=TestUtils.createCursor("month");
		MyOpenContext context=new MyOpenContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<OrderClause> clauses=new ArrayList<>();
		clauses.add(new OrderClause(new IdentifierExpression("days"), true));
		clauses.add(new OrderClause(new IdentifierExpression("name"), false));
		cursor=new OrderedCursor(cursor, context, evaluatorFactory, clauses);
		assertListEquals(cursor, 1, Arrays.asList("febrero", "septiembre", "noviembre", "junio", "abril", "octubre", "mayo", "marzo", "julio", "enero", "diciembre", "agosto", "triciembre"));
	}
}
