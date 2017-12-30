package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public abstract class AbstractExecutor implements Executor
{
	private final Context context;

	private final EvaluatorFactory evaluatorFactory;

	protected AbstractExecutor(Context context, EvaluatorFactory evaluatorFactory)
	{
		super();
		this.context=context;
		this.evaluatorFactory=evaluatorFactory;
	}

	protected Context getContext()
	{
		return this.context;
	}

	protected EvaluatorFactory getEvaluatorFactory()
	{
		return this.evaluatorFactory;
	}

}
