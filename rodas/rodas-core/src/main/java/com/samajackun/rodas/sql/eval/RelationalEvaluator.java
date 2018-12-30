package com.samajackun.rodas.sql.eval;


import com.samajackun.rodas.sql.model.Expression;

public interface RelationalEvaluator
{
	public boolean evaluateLowerThan(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateLowerThanOrEquals(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateGreaterThan(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateGreaterThanOrEquals(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateEquals(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateNotEquals(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateLike(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateBetween(Context context, Expression expression1, Expression lower, Expression upper)
		throws EvaluationException;

	public boolean evaluateIs(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateIsOfType(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;
}
