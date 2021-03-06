package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public abstract class ConstantExpression implements Expression
{
	private final String value;

	private boolean notEvaluated=true;

	private Object evaluatedValue;

	private EvaluationException evaluationException;

	public ConstantExpression(String value)
	{
		super();
		this.value=value;
	}

	public String getValue()
	{
		return this.value;
	}

	@Override
	public String toCode()
	{
		return this.value;
	}

	@Override
	public String toString()
	{
		return toCode();
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		if (this.notEvaluated)
		{
			try
			{
				this.evaluatedValue=evaluateOnce(context, evaluatorFactory);
			}
			catch (EvaluationException e)
			{
				this.evaluationException=e;
			}
			this.notEvaluated=false;
		}
		if (this.evaluationException != null)
		{
			throw this.evaluationException;
		}
		else
		{
			return this.evaluatedValue;
		}
	}

	protected abstract Object evaluateOnce(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException;

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this;
	}

	@Override
	public Executor createExecutor(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return new ConstantExecutor(context, evaluatorFactory, evaluatorFactory);
	}
}
