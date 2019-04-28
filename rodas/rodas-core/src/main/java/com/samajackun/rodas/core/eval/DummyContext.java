package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.Provider;

public final class DummyContext implements Context
{
	private static final DummyContext INSTANCE=new DummyContext();

	public static DummyContext getInstance()
	{
		return INSTANCE;
	}

	private DummyContext()
	{
	}

	@Override
	public Provider getProvider()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public MapList<String, Cursor> getCursors()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getColumnByName(String column, String prefix)
		throws NameNotBoundException,
		CursorException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getColumnByName(String column)
		throws NameNotBoundException,
		CursorException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnIndexByName(String column, String prefix)
		throws NameNotBoundException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Context fork(Context subContext)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getColumnByIndex(int index)
		throws IndexNotBoundException,
		CursorException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public VariablesManager getVariablesManager()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Runtime getRuntime()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object evaluate(Expression expression, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return expression.evaluate(this, evaluatorFactory);
	}

}
