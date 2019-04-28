package com.samajackun.rodas.core.eval.functions;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class LenFunction extends AbstractFunction
{
	public LenFunction(EvaluatorFactory evaluatorFactory)
	{
		super("len", evaluatorFactory, 1, 1);
	}

	@Override
	protected Object evaluateFunction(Context context, List<Object> values)
		throws FunctionEvaluationException
	{
		Object result;
		Object value=values.get(0);
		if (value == null)
		{
			result=null;
		}
		else if (value instanceof String)
		{
			String string=(String)value;
			result=string.length();
		}
		else
		{
			throw new FunctionEvaluationException("len", "parameter type must be a STRING");
		}
		return result;
	}

	@Override
	public boolean isDeterministic()
	{
		return true;
	}
}
