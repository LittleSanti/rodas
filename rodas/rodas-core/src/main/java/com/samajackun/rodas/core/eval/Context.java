package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.Provider;

public interface Context
{
	public Provider getProvider();

	// Object getColumnByName(String column, String prefix)
	// throws NameNotBoundException,
	// CursorException;
	//
	// Object getColumnByName(String column)
	// throws NameNotBoundException,
	// CursorException;
	//
	// int getColumnIndexByName(String column, String prefix)
	// throws NameNotBoundException;
	//
	// Context fork(Context subContext);
	//
	// Object getColumnByIndex(int index)
	// throws IndexNotBoundException,
	// CursorException;

	public VariablesManager getVariablesManager();

	public Runtime getRuntime();

	public Object evaluate(Expression expression, EvaluatorFactory evaluatorFactory)
		throws EvaluationException;

	public EvaluatorFactory getEvaluatorFactory();
}