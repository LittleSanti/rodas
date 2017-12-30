package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class BetweenExpression extends RelationalBinaryExpression
{
	private final Expression lower;

	private final Expression upper;

	public BetweenExpression(String operator, Expression expression1, AndExpression expression2)
	{
		super(operator, expression1, expression2);
		this.lower=expression2.getExpression1();
		this.upper=expression2.getExpression2();
	}

	@Override
	protected Boolean compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		return evaluatorFactory.getRelationalEvaluator().evaluateBetween(context, getExpression1(), this.lower, this.upper);
	}
}
