package com.samajackun.rodas.core.eval.functions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.functions.AbstractFunction;

public class LenFunction extends AbstractFunction
{
	public LenFunction(EvaluatorFactory evaluatorFactory)
	{
		super("len", evaluatorFactory, 1, 1);
	}

	@Override
	protected Object evaluateFunction(Context context, Object[] values)
		throws EvaluationException
	{
		Object result;
		Object value=values[0];
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
			throw new EvaluationException("LEN parameter type must be a STRING");
		}
		return result;
	}

	@Override
	public boolean isDeterministic()
	{
		return true;
	}
}
