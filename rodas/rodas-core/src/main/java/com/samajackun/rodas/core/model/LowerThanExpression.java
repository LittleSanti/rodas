package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class LowerThanExpression extends RelationalBinaryExpression
{
	public LowerThanExpression(String operator, Expression expression1, Expression expression2)
	{
		super(operator, expression1, expression2);
	}

	@Override
	protected Boolean compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		return evaluatorFactory.getRelationalEvaluator().evaluateLowerThan(context, expression1, expression2);
	}
}
