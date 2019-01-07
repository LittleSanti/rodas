package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public abstract class LogicalUnitExpression extends UnitExpression
{
	public LogicalUnitExpression(String operator, Expression expression1)
	{
		super(operator, expression1);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reduced;
		Expression reduced1=getExpression().reduceAndReport(evaluatorFactory);
		if (reduced1 instanceof BooleanConstantExpression)
		{
			Boolean result=compute(null, evaluatorFactory, reduced1);
			reduced=new BooleanConstantExpression(result.toString(), result);
		}
		else
		{
			reduced=this;
		}
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
