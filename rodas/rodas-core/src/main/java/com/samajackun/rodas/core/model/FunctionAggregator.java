package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.functions.FunctionEvaluationException;

public interface FunctionAggregator
{
	public Object getResult();

	public void addArgument(Context context, Object argumentValue)
		throws FunctionEvaluationException;
}
