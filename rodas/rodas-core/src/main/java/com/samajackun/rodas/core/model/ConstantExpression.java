package com.samajackun.rodas.core.model;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public abstract class ConstantExpression implements Expression
{
	private static final long serialVersionUID=-7660064617396789851L;

	private final String value;

	private boolean notEvaluated=true;

	private Object evaluatedValue;

	private EvaluationException evaluationException;

	protected ConstantExpression(String value)
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

	public abstract Object evaluateAsConstant()
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

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.evaluatedValue == null)
			? 0
			: this.evaluatedValue.hashCode());
		result=prime * result + ((this.evaluationException == null)
			? 0
			: this.evaluationException.hashCode());
		result=prime * result + (this.notEvaluated
			? 1231
			: 1237);
		result=prime * result + ((this.value == null)
			? 0
			: this.value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		ConstantExpression other=(ConstantExpression)obj;
		if (this.evaluatedValue == null)
		{
			if (other.evaluatedValue != null)
			{
				return false;
			}
		}
		else if (!this.evaluatedValue.equals(other.evaluatedValue))
		{
			return false;
		}
		if (this.evaluationException == null)
		{
			if (other.evaluationException != null)
			{
				return false;
			}
		}
		else if (!this.evaluationException.equals(other.evaluationException))
		{
			return false;
		}
		if (this.notEvaluated != other.notEvaluated)
		{
			return false;
		}
		if (this.value == null)
		{
			if (other.value != null)
			{
				return false;
			}
		}
		else if (!this.value.equals(other.value))
		{
			return false;
		}
		return true;
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return Collections.emptyList();
	}
}
