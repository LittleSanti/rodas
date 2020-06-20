package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.RodasRuntimeException;

public class MyOpenContext implements Context
{
	// private MapList<String, Cursor> cursors;

	private VariablesManager variablesManager;

	private Runtime runtime;

	private Provider provider;

	// @Override
	// public Object getColumnByName(String column, String prefix)
	// throws NameNotBoundException,
	// CursorException
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Object getColumnByName(String column)
	// throws NameNotBoundException,
	// CursorException
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public int getColumnIndexByName(String column, String prefix)
	// throws NameNotBoundException
	// {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public Context fork(Context subContext)
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public Object getColumnByIndex(int index)
	// throws IndexNotBoundException,
	// CursorException
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }

	// @Override
	// public MapList<String, Cursor> getCursors()
	// {
	// return this.cursors;
	// }
	//
	// public void setCursors(MapList<String, Cursor> cursors)
	// {
	// this.cursors=cursors;
	// }

	@Override
	public VariablesManager getVariablesManager()
	{
		return this.variablesManager;
	}

	public void setVariablesManager(VariablesManager variablesManager)
	{
		this.variablesManager=variablesManager;
	}

	@Override
	public Runtime getRuntime()
	{
		return this.runtime;
	}

	public void setRuntime(Runtime runtime)
	{
		this.runtime=runtime;
	}

	@Override
	public Provider getProvider()
	{
		return this.provider;
	}

	public void setProvider(Provider provider)
	{
		this.provider=provider;
	}

	@Override
	public Object evaluate(Expression expression, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		VariablesContext variablesContext=this.variablesManager.peekLocalContext();
		// System.out.println("variablesContext.get(" + expression.getName() +
		// ")=" + variablesContext.get(expression.getName()));
		Object value=variablesContext.setIfAbsent(expression.getCodeAsName(), () -> {
			try
			{
				return expression.evaluate(this, evaluatorFactory);
			}
			catch (EvaluationException e)
			{
				throw new RodasRuntimeException(e);
			}
		});
		return value;
	}

	public Context createSubcontext(VariablesContext newVariablesContext)
	{
		return new ProxyContext(this, newVariablesContext);
	}

}
