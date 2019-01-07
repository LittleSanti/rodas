package com.samajackun.rodas.core.eval;


import com.samajackun.rodas.core.model.Expression;

public interface TextEvaluator
{
	public String evaluateConcat(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;
}
