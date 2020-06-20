package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.DummyContext;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public abstract class ArithmeticUnitExpression extends UnitExpression
{
	private static final long serialVersionUID=-3098444121087250115L;

	public ArithmeticUnitExpression(String operator, Expression expression)
	{
		super(operator, expression);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reduced;
		Expression reduced1=getExpression().reduceAndReport(evaluatorFactory);
		if (reduced1 instanceof NumericConstantExpression)
		{
			Number result=compute(DummyContext.getInstance(), evaluatorFactory, reduced1);
			reduced=new NumericConstantExpression(result.toString(), result);
		}
		else
		{
			reduced=this;
		}
		return reduced;
	}

	protected abstract Number compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException;
}
