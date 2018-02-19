package com.samajackun.rodas.sql.engine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.model.BooleanExpression;
import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.RowData;

public class JoinedCursor implements Cursor
{
	private final BooleanExpression condition;

	private Context context;

	private final EvaluatorFactory evaluatorFactory;

	private boolean closed;

	private final CrossCursor crossCursor;

	private boolean canIterate;

	private final MemoryRowData rowData;

	private final Object[] fetched;

	private final Object[] currentRow;

	public JoinedCursor(Cursor left, Cursor right, BooleanExpression condition, EvaluatorFactory evaluatorFactory)
		throws CursorException
	{
		this.condition=condition;
		this.evaluatorFactory=evaluatorFactory;
		this.crossCursor=new CrossCursor(Arrays.asList(left, right));
		this.fetched=new Object[this.crossCursor.getNumberOfColumns()];
		this.currentRow=new Object[this.crossCursor.getNumberOfColumns()];
		this.rowData=new MemoryRowData();
	}

	public void initContext(Context context)
		throws CursorException
	{
		this.context=context;
		fetch();
	}

	private void fetch()
		throws CursorException
	{
		try
		{
			boolean x=false;
			do
			{
				fetchRawRow();
				if (this.canIterate)
				{
					x=this.condition.evaluate(this.context, this.evaluatorFactory);
				}
			}
			while (!x && this.canIterate);
			if (x)
			{
				load(this.crossCursor.getRowData());
			}
			else
			{
				this.canIterate=false;
			}
		}
		catch (EvaluationException e)
		{
			throw new CursorException(e);
		}
	}

	private void fetchRawRow()
		throws CursorException
	{
		if (this.canIterate=this.crossCursor.hasNext())
		{
			this.crossCursor.next();
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
		if (!this.crossCursor.hasNext())
		{
			throw new CursorException("Ended");
		}
		else
		{
			System.arraycopy(this.fetched, 0, this.currentRow, 0, this.fetched.length);
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
		return this.rowData;
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return this.crossCursor.getColumnMap();
	}

	@Override
	public void reset()
		throws CursorException
	{
		checkOpen();
		this.crossCursor.reset();
	}

	@Override
	public List<ColumnMetadata> getMetadata()
		throws CursorException
	{
		return this.crossCursor.getMetadata();
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.crossCursor.getNumberOfColumns();
	}

	private void load(RowData rowData)
	{
		for (int i=0; i < JoinedCursor.this.fetched.length; i++)
		{
			this.fetched[i]=rowData.get(i);
		}
	}

	private class MemoryRowData implements RowData
	{
		@Override
		public Object get(int column)
		{
			return JoinedCursor.this.currentRow[column];
		}
	}

	public Cursor getInnerCursor()
	{
		return this.crossCursor;
	}

}
