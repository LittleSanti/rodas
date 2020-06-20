package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class MultiplyExpression extends ArithmeticBinaryExpression
{
	private static final long serialVersionUID=-1368024663996211177L;

	public MultiplyExpression(String operator, Expression expression1, Expression expression2)
	{
		super(operator, expression1, expression2);
	}

	@Override
	protected Number compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateMultiply(context, expression1, expression2);
	}

}
