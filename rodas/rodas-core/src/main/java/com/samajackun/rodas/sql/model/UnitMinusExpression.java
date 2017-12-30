package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class UnitMinusExpression extends ArithmeticUnitExpression
{
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
	protected Number compute(EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateUnitMinus(null, expression1);
	}
}
