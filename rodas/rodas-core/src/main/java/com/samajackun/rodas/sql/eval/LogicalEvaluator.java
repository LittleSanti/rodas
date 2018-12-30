package com.samajackun.rodas.sql.eval;


import com.samajackun.rodas.sql.model.Expression;

public interface LogicalEvaluator
{
	public boolean evaluateAnd(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateOr(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateNot(Context context, Expression expression1)
		throws EvaluationException;
}
