package com.samajackun.rodas.sql.engine;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.LogicalUtils;
import com.samajackun.rodas.core.execution.CachedRowData;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.RowData;

public class FilteredCursor extends AbstractNestedCursor
{
	private final EvaluatorFactory evaluatorFactory;

	private final Expression filterExpression;

	private RowData fetched;

	private RowData current;

	private long myPosition;

	public FilteredCursor(Cursor src, Context context, EvaluatorFactory evaluatorFactory, Expression filterExpression)
		throws CursorException,
		EvaluationException
	{
		super(src, context);
		this.evaluatorFactory=evaluatorFactory;
		this.filterExpression=filterExpression;
		fetch();
	}

	private void fetch()
		throws CursorException
	{
		try
		{
			boolean looping=true;
			while (looping)
			{
				if (super.hasNext())
				{
					super.next();
					Object value=this.filterExpression.evaluate(getContext(), this.evaluatorFactory);
					boolean match=LogicalUtils.toBoolean(value);
					if (match)
					{
						this.fetched=new CachedRowData(super.getRowData(), getNumberOfColumns(), this.myPosition);
						looping=false;
					}
					else
					{
						this.fetched=null;
					}
				}
				else
				{
					this.fetched=null;
					looping=false;
				}
			}
			this.myPosition++;
		}
		catch (EvaluationException e)
		{
			throw new CursorException(e);
		}
	}

	@Override
	public void next()
		throws CursorException
	{
		if (this.fetched == null)
		{
			throw new CursorException("Exhausted cursor");
		}
		this.current=this.fetched;
		fetch();
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		return this.fetched != null;
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		return this.current;
	}

	@Override
	public void reset()
		throws CursorException
	{
		super.reset();
		fetch();
	}
}
