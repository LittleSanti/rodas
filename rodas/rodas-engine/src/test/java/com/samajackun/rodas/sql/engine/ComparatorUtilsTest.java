package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.core.context.TestUtils;
import com.samajackun.rodas.core.eval.DefaultContext;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.CachedCursor;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.OrderClause;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;

public class ComparatorUtilsTest
{

	@Test
	public void createComparatorFromOneClauseAscending()
		throws ProviderException,
		CursorException
	{
		Cursor cursor=TestUtils.createCursor("month");
		DefaultContext context=new DefaultContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		RowDataContext rowDataContext=new RowDataContext(context.getVariablesManager().peekLocalContext(), cursor.getColumnMap());
		context.getVariablesManager().pushLocalContext(rowDataContext);
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<OrderClause> clauses=new ArrayList<>();
		clauses.add(new OrderClause(new IdentifierExpression("days"), OrderClause.Direction.ASCENDING));
		CachedCursor cachedCursor=cursor.toCachedCursor();
		RowData row0=cachedCursor.getRowData(0);
		// System.out.println("row0=" + row0);
		RowData row1=cachedCursor.getRowData(1);
		// System.out.println("row1=" + row1);
		Comparator<RowData> comparator=ComparatorUtils.createComparator(context, rowDataContext, evaluatorFactory, clauses);
		assertEquals(0, comparator.compare(row0, row0));
		assertTrue(comparator.compare(row0, row1) > 0);
		assertTrue(comparator.compare(row1, row0) < 0);
		assertEquals(0, comparator.compare(row1, row1));
	}

	@Test
	public void createComparatorFromOneClauseDescending()
		throws ProviderException,
		CursorException
	{
		Cursor cursor=TestUtils.createCursor("month");
		DefaultContext context=new DefaultContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		RowDataContext rowDataContext=new RowDataContext(context.getVariablesManager().peekLocalContext(), cursor.getColumnMap());
		context.getVariablesManager().pushLocalContext(rowDataContext);
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<OrderClause> clauses=new ArrayList<>();
		clauses.add(new OrderClause(new IdentifierExpression("days"), OrderClause.Direction.DESCENDING));
		CachedCursor cachedCursor=cursor.toCachedCursor();
		RowData row0=cachedCursor.getRowData(0);
		// System.out.println("row0=" + row0);
		RowData row1=cachedCursor.getRowData(1);
		// System.out.println("row1=" + row1);
		Comparator<RowData> comparator=ComparatorUtils.createComparator(context, rowDataContext, evaluatorFactory, clauses);
		assertEquals(0, comparator.compare(row0, row0));
		assertTrue(comparator.compare(row0, row1) < 0);
		assertTrue(comparator.compare(row1, row0) > 0);
		assertEquals(0, comparator.compare(row1, row1));
	}

	@Test
	public void createComparatorFromTwoClauses()
		throws ProviderException,
		CursorException
	{
		Cursor cursor=TestUtils.createCursor("month");
		DefaultContext context=new DefaultContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		RowDataContext rowDataContext=new RowDataContext(context.getVariablesManager().peekLocalContext(), cursor.getColumnMap());
		context.getVariablesManager().pushLocalContext(rowDataContext);
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<OrderClause> clauses=new ArrayList<>();
		clauses.add(new OrderClause(new IdentifierExpression("days"), OrderClause.Direction.ASCENDING));
		clauses.add(new OrderClause(new IdentifierExpression("name"), OrderClause.Direction.ASCENDING));
		CachedCursor cachedCursor=cursor.toCachedCursor();
		RowData row0=cachedCursor.getRowData(5);// junio
		RowData row1=cachedCursor.getRowData(3);// abril
		Comparator<RowData> comparator=ComparatorUtils.createComparator(context, rowDataContext, evaluatorFactory, clauses);
		assertEquals(0, comparator.compare(row0, row0));
		assertTrue(comparator.compare(row0, row1) > 0);
		assertTrue(comparator.compare(row1, row0) < 0);
		assertEquals(0, comparator.compare(row1, row1));
	}

	@Test
	public void indexFromTwoClauses()
		throws ProviderException,
		CursorException
	{
		Cursor cursor=TestUtils.createCursor("month");
		DefaultContext context=new DefaultContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		RowDataContext rowDataContext=new RowDataContext(context.getVariablesManager().peekLocalContext(), cursor.getColumnMap());
		context.getVariablesManager().pushLocalContext(rowDataContext);
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		List<OrderClause> clauses=new ArrayList<>();
		clauses.add(new OrderClause(new IdentifierExpression("days"), OrderClause.Direction.ASCENDING));
		clauses.add(new OrderClause(new IdentifierExpression("name"), OrderClause.Direction.ASCENDING));
		CachedCursor cachedCursor=cursor.toCachedCursor();
		List<Integer> index=ComparatorUtils.index(cachedCursor, context, rowDataContext, evaluatorFactory, clauses);
		// 28 febrero
		// 30 abril
		// 30 junio
		// 30 nov
		// 30 sept
		// 31 agosto
		// 31 dic
		// 31 enero
		// 31 julio
		// 31 marzo
		// 31 mayo
		// 31 oct
		List<Integer> expected=Arrays.asList(new Integer[] {
			1,
			3,
			5,
			10,
			8,
			7,
			11,
			0,
			6,
			2,
			4,
			9,
			12
		});
		assertEquals(expected, index);
	}
}
