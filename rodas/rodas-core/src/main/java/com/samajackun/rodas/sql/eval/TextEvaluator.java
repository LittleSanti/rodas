package com.samajackun.rodas.sql.eval;

import com.samajackun.rodas.sql.model.Expression;

public interface TextEvaluator
{
	public String evaluateConcat(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;
}
