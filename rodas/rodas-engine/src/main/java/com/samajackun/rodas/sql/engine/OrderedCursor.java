package com.samajackun.rodas.sql.engine;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.DefaultContext;
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
	private final CachedCursor src;

	private final List<Integer> index;

	private int currentRow=-1;

	private boolean closed;

	public OrderedCursor(Cursor src, Context producerContext, EvaluatorFactory evaluatorFactory, List<OrderClause> orderClauses)
		throws CursorException
	{
		this.src=src.toCachedCursor();
		RowDataContext rowDataContext=new RowDataContext(producerContext.getVariablesManager().peekLocalContext(), src.getColumnMap());
		Context newContext=DefaultContext.createNestedContext(producerContext, rowDataContext);
		this.index=ComparatorUtils.index(this.src, newContext, rowDataContext, evaluatorFactory, orderClauses);
	}

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
