package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.samajackun.rodas.core.context.TestUtils;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.DefaultContext;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.BooleanConstantExpression;
import com.samajackun.rodas.core.model.EqualsExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.TextConstantExpression;

public class FilteredCursorTest
{
	@Test
	public void filterTrue()
		throws CursorException,
		ProviderException,
		EvaluationException
	{
		Cursor cursor=TestUtils.createCursor("month");
		Expression expression=new BooleanConstantExpression("true", true);
		DefaultContext context=new DefaultContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		cursor=new FilteredCursor(cursor, context, evaluatorFactory, expression);

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("enero", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("febrero", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("marzo", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("abril", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("mayo", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("junio", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("julio", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("agosto", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("septiembre", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("octubre", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("noviembre", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("diciembre", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("triciembre", cursor.getRowData().get(1));

		assertFalse(cursor.hasNext());
	}

	@Test
	public void filterFalse()
		throws CursorException,
		ProviderException,
		EvaluationException
	{
		Cursor cursor=TestUtils.createCursor("month");
		Expression expression=new BooleanConstantExpression("false", false);
		DefaultContext context=new DefaultContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		cursor=new FilteredCursor(cursor, context, evaluatorFactory, expression);

		assertFalse(cursor.hasNext());
	}

	@Test
	public void filterByName()
		throws CursorException,
		ProviderException,
		EvaluationException
	{
		Cursor cursor=TestUtils.createCursor("month");
		Expression expression=new EqualsExpression("=", new IdentifierExpression("name"), new TextConstantExpression("marzo"));
		DefaultContext context=new DefaultContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		cursor=new FilteredCursor(cursor, context, evaluatorFactory, expression);

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("marzo", cursor.getRowData().get(1));
		assertFalse(cursor.hasNext());

		assertFalse(cursor.hasNext());
	}

	@Test
	public void filterAndReturnSome()
		throws CursorException,
		ProviderException,
		EvaluationException
	{
		Cursor cursor=TestUtils.createCursor("month");
		Expression expression=new EqualsExpression("=", new IdentifierExpression("days"), new NumericConstantExpression("31", 31));
		DefaultContext context=new DefaultContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		cursor=new FilteredCursor(cursor, context, evaluatorFactory, expression);

		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("enero", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("marzo", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("mayo", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("julio", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("agosto", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("octubre", cursor.getRowData().get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("diciembre", cursor.getRowData().get(1));
		assertFalse(cursor.hasNext());
	}
}
