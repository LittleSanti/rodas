package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class UnitPlusExpression extends ArithmeticUnitExpression
{
	public UnitPlusExpression(String operator, Expression expression)
	{
		super(operator, expression);
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateUnitPlus(context, getExpression());
	}

	@Override
	protected Number compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateUnitPlus(context, expression1);
	}
}
