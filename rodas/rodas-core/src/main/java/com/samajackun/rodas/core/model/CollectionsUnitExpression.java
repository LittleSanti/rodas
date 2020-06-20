package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public abstract class CollectionsUnitExpression extends UnitExpression
{
	private static final long serialVersionUID=2368982869036179697L;

	public CollectionsUnitExpression(String operator, Expression expression)
	{
		super(operator, expression);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		// TODO
		Expression reduced=null;
		return reduced;
	}

	@Override
	public Boolean evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return compute(context, evaluatorFactory, getExpression());
	}

	protected abstract Boolean compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException;
}
