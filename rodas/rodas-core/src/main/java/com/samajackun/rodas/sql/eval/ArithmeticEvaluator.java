package com.samajackun.rodas.sql.eval;

import com.samajackun.rodas.sql.model.Datatype;
import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.model.MetadataException;

public interface ArithmeticEvaluator
{
	public Number evaluateAdd(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public Number evaluateSubstract(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public Number evaluateMultiply(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public Number evaluateDivide(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public Number evaluateUnitMinus(Context context, Expression expression)
		throws EvaluationException;

	public Number evaluateUnitPlus(Context context, Expression expression)
		throws EvaluationException;

	public Datatype getDatatypeForAdd(Context context, Expression expression1, Expression expression2)
		throws MetadataException;

	public Datatype getDatatypeForSubstract(Context context, Expression expression1, Expression expression2)
		throws MetadataException;
}
