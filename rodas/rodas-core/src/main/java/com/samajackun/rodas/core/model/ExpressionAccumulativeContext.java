package com.samajackun.rodas.core.model;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.functions.FunctionEvaluationException;

public class ExpressionAccumulativeContext implements AccumulativeExpression
{
	private static final long serialVersionUID=7061689180614642323L;

	private Expression expression;

	private Object currentValue;

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.currentValue;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.expression.reduce(evaluatorFactory);
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return this.expression.getDatatype(context, evaluatorFactory);
	}

	@Override
	public String toCode()
	{
		return this.expression.toCode();
	}

	@Override
	public void evaluateAndAccumulate(Context context, EvaluatorFactory evaluatorFactory)
		throws FunctionEvaluationException
	{
		try
		{
			this.currentValue=this.expression.evaluate(context, evaluatorFactory);
		}
		catch (EvaluationException e)
		{
			throw new FunctionEvaluationException(e.getMessage(), this.expression.toCode());
		}
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return Collections.singletonList(this.expression);
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.currentValue == null)
			? 0
			: this.currentValue.hashCode());
		result=prime * result + ((this.expression == null)
			? 0
			: this.expression.hashCode());
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
		ExpressionAccumulativeContext other=(ExpressionAccumulativeContext)obj;
		if (this.currentValue == null)
		{
			if (other.currentValue != null)
			{
				return false;
			}
		}
		else if (!this.currentValue.equals(other.currentValue))
		{
			return false;
		}
		if (this.expression == null)
		{
			if (other.expression != null)
			{
				return false;
			}
		}
		else if (!this.expression.equals(other.expression))
		{
			return false;
		}
		return true;
	}

}
