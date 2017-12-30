package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class ExistsExpression extends CollectionsUnitExpression implements BooleanExpression
{
	public ExistsExpression(String operator, Expression expression)
	{
		super(operator, expression);
	}

	@Override
	protected Boolean compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException
	{
		return evaluatorFactory.getCollectionsEvaluator().evaluateExists(context, expression1);
	}
}
