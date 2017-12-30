package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public abstract class ArithmeticUnitExpression extends UnitExpression
{
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
			Number result=compute(evaluatorFactory, reduced1);
			reduced=new NumericConstantExpression(result.toString(), result);
		}
		else
		{
			reduced=this;
		}
		return reduced;
	}

	protected abstract Number compute(EvaluatorFactory evaluatorFactory, Expression expression1)
		throws EvaluationException;
}
