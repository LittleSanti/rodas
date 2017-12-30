package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

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
	protected Number compute(EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateUnitPlus(null, expression1);
	}
}
