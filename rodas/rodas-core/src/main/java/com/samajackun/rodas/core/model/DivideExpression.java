package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class DivideExpression extends ArithmeticBinaryExpression
{
	private static final long serialVersionUID=2734758515715376732L;

	public DivideExpression(String operator, Expression expression1, Expression expression2)
	{
		super(operator, expression1, expression2);
	}

	@Override
	protected Number compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateDivide(context, getExpression1(), getExpression2());
	}
}
