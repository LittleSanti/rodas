package com.samajackun.rodas.core.eval.functions;

import com.samajackun.rodas.core.eval.Context;

public interface FunctionContext
{
	public Object getResult();

	public void addArgument(Context context, Object argumentValue)
		throws FunctionEvaluationException;
}
