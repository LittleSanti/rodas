package com.samajackun.rodas.sql.eval;

public abstract class AbstractEvaluator
{
	private final EvaluatorFactory evaluatorFactory;

	protected AbstractEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super();
		this.evaluatorFactory=evaluatorFactory;
	}

	protected EvaluatorFactory getEvaluatorFactory()
	{
		return this.evaluatorFactory;
	}
}
