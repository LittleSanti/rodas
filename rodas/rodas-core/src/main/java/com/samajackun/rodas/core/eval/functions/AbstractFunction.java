package com.samajackun.rodas.core.eval.functions;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public abstract class AbstractFunction implements Function
{
	private final String name;

	private final EvaluatorFactory evaluatorFactory;

	private final int minArguments;

	private final int maxArguments;

	protected AbstractFunction(String name, EvaluatorFactory evaluatorFactory, int minArguments, int maxArguments)
	{
		super();
		this.name=name;
		this.evaluatorFactory=evaluatorFactory;
		this.minArguments=minArguments;
		this.maxArguments=maxArguments;
	}

	public EvaluatorFactory getEvaluatorFactory()
	{
		return this.evaluatorFactory;
	}

	protected abstract Object evaluateFunction(Context context, List<Object> values)
		throws FunctionEvaluationException;

	protected void validateNumberOfArguments(int numberOfArguments)
		throws FunctionEvaluationException
	{
		if (this.minArguments > 0)
		{
			if (this.maxArguments == this.minArguments)
			{
				if (numberOfArguments != this.minArguments)
				{
					throw new ExactNumberOfArgumentsRequiredException(this.name, this.minArguments);
				}
			}
			else
			{
				if (numberOfArguments < this.minArguments)
				{
					throw new MinimumNumberOfArgumentsRequiredException(this.name, this.minArguments);
				}
			}
		}
	}

	@Override
	public Object call(Context context, List<Object> arguments)
		throws FunctionEvaluationException
	{
		validateNumberOfArguments(arguments.size());
		return evaluateFunction(context, arguments);
	}

	protected boolean hasAnyNull(List<Object> values)
	{
		boolean x=false;
		for (Iterator<Object> iterator=values.iterator(); !x && iterator.hasNext();)
		{
			x=(iterator.next() == null);
		}
		return x;
	}

	protected Class<?> getMaxScaleType(List<Object> values)
		throws FunctionEvaluationException
	{
		int scale=0;
		for (Object value : values)
		{
			int newScale;
			if (value == null)
			{
				return null;
			}
			if (value instanceof Number)
			{
				if (value instanceof Integer)
				{
					newScale=1;
				}
				else if (value instanceof Long)
				{
					newScale=2;
				}
				else if (value instanceof Float)
				{
					newScale=(scale == 1)
						? 3
						: 4;
				}
				else if (value instanceof Double)
				{
					newScale=4;
				}
				else
				{
					newScale=1;
				}
				scale=Math.max(scale, newScale);
			}
			else if (value instanceof String)
			{
				if (scale == 0)
				{
					scale=5;
				}
				else
				{
					throw new FunctionEvaluationException(this.name, "parameter types do not match");
				}
			}
			else if (value instanceof Date)
			{
				if (scale == 0)
				{
					scale=6;
				}
				else
				{
					throw new FunctionEvaluationException(this.name, "parameter types do not match");
				}
			}
			else if (value instanceof Boolean)
			{
				if (scale == 0)
				{
					scale=7;
				}
				else
				{
					throw new FunctionEvaluationException(this.name, "parameter types do not match");
				}
			}
			else
			{
				throw new FunctionEvaluationException(this.name, "parameter types should be numeric, string, date or booleam");
			}
		}
		Class<?> result;
		switch (scale)
		{
			case 1:
				result=Integer.class;
				break;
			case 2:
				result=Long.class;
				break;
			case 3:
				result=Float.class;
				break;
			case 4:
				result=Double.class;
				break;
			case 5:
				result=String.class;
				break;
			case 6:
				result=Date.class;
				break;
			case 7:
				result=Boolean.class;
				break;
			default:
				result=Integer.class;
		}
		return result;
	}
}
