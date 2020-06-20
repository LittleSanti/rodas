package com.samajackun.rodas.core.eval.functions;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;

public interface Function
{
	public Object call(Context context, List<Object> argumentValues)
		throws FunctionEvaluationException;

	public default boolean isDeterministic()
	{
		return false;
	}
}
