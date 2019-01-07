package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class NotExpression extends LogicalUnitExpression implements BooleanExpression
{
	public NotExpression(String operator, Expression expression)
	{
		super(operator, expression);
	}

	@Override
	protected Boolean compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException
	{
		return evaluatorFactory.getLogicalEvaluator().evaluateNot(context, expression1);
	}
}
