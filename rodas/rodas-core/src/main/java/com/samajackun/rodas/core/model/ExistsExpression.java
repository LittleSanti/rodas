package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ExistsExpression extends CollectionsUnitExpression implements BooleanExpression
{
	private static final long serialVersionUID=363013587318120719L;

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
