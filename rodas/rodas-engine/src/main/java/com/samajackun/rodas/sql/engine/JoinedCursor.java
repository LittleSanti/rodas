package com.samajackun.rodas.sql.engine;

import java.util.Map;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.model.BooleanExpression;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.RowData;

public class JoinedCursor implements Cursor
{
	private final Cursor left;

	private final Cursor right;

	private final BooleanExpression condition;

	private final Context context;

	private boolean canIterate;

	private final EvaluatorFactory evaluatorFactory;

	private boolean closed;

	public JoinedCursor(Cursor left, Cursor right, BooleanExpression condition, Context context, EvaluatorFactory evaluatorFactory) throws CursorException
	{
		this.left=left;
		this.right=right;
		this.condition=condition;
		this.evaluatorFactory=evaluatorFactory;
		this.context=context.fork().addCursor(this.left).addCursor(this.right);
		reset();
	}

	private void fetch()
		throws CursorException
	{
		try
		{
			boolean x=false;
			while (!x && this.canIterate)
			{
				this.right.next();
				x=this.condition.evaluate(this.context, this.evaluatorFactory);
				if (!this.right.hasNext())
				{
					if (this.left.hasNext())
					{
						this.left.next();
						this.right.reset();
					}
					else
					{
						this.canIterate=false;
					}
				}
			}
		}
		catch (EvaluationException e)
		{
			throw new CursorException(e);
		}
	}

	@Override
	public void close()
		throws CursorException
	{
		this.closed=true;
	}

	private void checkOpen()
		throws CursorException
	{
		if (this.closed)
		{
			throw new CursorException("Cursor closed");
		}
	}

	@Override
	public void next()
		throws CursorException
	{
		checkOpen();
		if (!this.canIterate)
		{
			throw new CursorException("Ended");
		}
		else
		{
			fetch();
		}
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		checkOpen();
		return this.canIterate;
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		checkOpen();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset()
		throws CursorException
	{
		checkOpen();
		this.canIterate=this.left.hasNext() && this.right.hasNext();
		if (this.canIterate)
		{
			this.left.next();
			this.right.reset();
		}
	}

}
