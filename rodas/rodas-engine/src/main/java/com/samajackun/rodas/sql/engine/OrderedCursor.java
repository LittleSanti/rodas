package com.samajackun.rodas.sql.engine;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.execution.CachedCursor;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.execution.ExhaustedCursorException;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.OrderClause;
import com.samajackun.rodas.core.model.RowData;

public class OrderedCursor implements Cursor // extends AbstractNestedCursor
{
	private final Context producerContext;

	private final CachedCursor src;

	private final List<Integer> index;

	private int currentRow=-1;

	private boolean closed;

	public OrderedCursor(Cursor src, Context producerContext, EvaluatorFactory evaluatorFactory, List<OrderClause> orderClauses)
		throws CursorException
	{
		// super(src, context);
		this.producerContext=producerContext;
		this.src=src.toCachedCursor();
		RowDataContext rowDataContext=new RowDataContext(this.producerContext.getVariablesManager().peekLocalContext(), src.getColumnMap());
		this.producerContext.getVariablesManager().pushLocalContext(rowDataContext);
		this.index=ComparatorUtils.index(this.src, this.producerContext, rowDataContext, evaluatorFactory, orderClauses);
	}

	// private List<Integer> index(Cursor src, Context context, EvaluatorFactory evaluatorFactory, List<OrderClause> orderClauses)
	// throws CursorException
	// {
	// try
	// {
	// Comparator<Collection<Object>> composedComparator=null;
	// for (OrderClause clause : orderClauses)
	// {
	// Comparator<Collection<Object>> comparator1=ObjectComparator.getInstance();
	// if (!clause.isAscending())
	// {
	// comparator1=comparator1.reversed();
	// }
	// composedComparator=(composedComparator == null)
	// ? comparator1
	// : composedComparator.thenComparing(comparator1);
	// }
	// Map<Collection<Object>, Collection<RowDataIndex>> map=new TreeMap<>(composedComparator);
	// // Map<Collection<Object>, Collection<RowDataIndex>> map=new HashMap<>();
	// int n=0;
	// context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), src));
	// while (src.hasNext())
	// {
	// src.next();
	// Collection<Object> key=new ArrayList<>(orderClauses.size());
	// for (OrderClause orderClause : orderClauses)
	// {
	// Object value=orderClause.getExpression().evaluate(context, evaluatorFactory);
	// key.add(value);
	// }
	// RowData rowData=src.getRowData();
	// map.computeIfAbsent(key, t -> new ArrayList<>());
	// map.get(key).add(new RowDataIndex(rowData, n++));
	// }
	// System.out.println("map=" + map);
	// context.getVariablesManager().popLocalContext();
	// List<Integer> index=new ArrayList<>(n);
	// map.values().forEach(t -> t.forEach(x -> index.add(x.getIndex())));
	// System.out.println("index=" + index);
	// return index;
	// }
	// catch (EvaluationException e)
	// {
	// // TODO PRovisional, para probar el algoritmo.
	// throw new CursorException(e);
	// }
	//
	// }
	// private List<Integer> index(Cursor src, Context context, EvaluatorFactory evaluatorFactory, List<Expression> orderClauses)
	// throws CursorException
	// {
	// try
	// {
	// Comparator<Collection<Object>> comparator=AggregatedComparator.getInstance();
	// TreeMap<Collection<Object>, Integer> map=new TreeMap<>(comparator);
	// int n=0;
	// context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), src));
	// while (src.hasNext())
	// {
	// src.next();
	// Collection<Object> key=new ArrayList<>(orderClauses.size());
	// for (Expression orderClause : orderClauses)
	// {
	// Object value=orderClause.evaluate(context, evaluatorFactory);
	// key.add(value);
	// }
	// map.put(key, n++);
	// }
	// context.getVariablesManager().popLocalContext();
	// List<Integer> index=new ArrayList<>(n);
	// index.addAll(map.values());
	// System.out.println("map=" + map);
	// System.out.println("index=" + index);
	// return index;
	// }
	// catch (EvaluationException e)
	// {
	// throw new CursorException(e);
	// }
	// }

	private void checkClosed()
		throws ExhaustedCursorException
	{
		if (this.closed)
		{
			throw new ExhaustedCursorException();
		}
	}

	@Override
	public void close()
		throws CursorException
	{
		this.producerContext.getVariablesManager().popLocalContext();
		this.closed=true;
	}

	@Override
	public void next()
		throws CursorException
	{
		checkClosed();
		if (hasNext())
		{
			this.currentRow++;
		}
		else
		{
			throw new ExhaustedCursorException();
		}
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		checkClosed();
		return this.currentRow < this.index.size() - 1;
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		checkClosed();
		return this.src.getRowData(this.index.get(this.currentRow));
	}

	@Override
	public RowData getCachedRowData()
		throws CursorException
	{
		return getRowData();
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return this.src.getColumnMap();
	}

	@Override
	public List<ColumnMetaData> getMetadata()
		throws CursorException
	{
		return this.src.getMetadata();
	}

	@Override
	public void reset()
		throws CursorException
	{
		checkClosed();
		this.currentRow=0;
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.src.getNumberOfColumns();
	}

}
