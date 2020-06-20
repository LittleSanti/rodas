package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class UnitMinusExpression extends ArithmeticUnitExpression
{
	private static final long serialVersionUID=-2293700955075362824L;

	public UnitMinusExpression(String operator, Expression expression)
	{
		super(operator, expression);
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateUnitMinus(context, getExpression());
	}

	@Override
	protected Number compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateUnitMinus(context, expression1);
	}
}
