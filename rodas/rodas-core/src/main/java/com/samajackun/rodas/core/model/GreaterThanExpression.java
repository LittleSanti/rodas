package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class GreaterThanExpression extends RelationalBinaryExpression
{
	private static final long serialVersionUID=-2726795869055694208L;

	public GreaterThanExpression(String operator, Expression expression1, Expression expression2)
	{
		super(operator, expression1, expression2);
	}

	@Override
	protected Boolean compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		return evaluatorFactory.getRelationalEvaluator().evaluateGreaterThan(context, expression1, expression2);
	}
}
