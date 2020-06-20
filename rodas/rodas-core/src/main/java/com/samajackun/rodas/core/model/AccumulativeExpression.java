package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.functions.FunctionEvaluationException;

public interface AccumulativeExpression extends Expression
{
	public void evaluateAndAccumulate(Context context, EvaluatorFactory evaluatorFactory)
		throws FunctionEvaluationException;
}
