package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.DummyContext;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class AddExpression extends BinaryExpression
{
	public AddExpression(String operator, Expression expression1, Expression expression2)
	{
		super(operator, expression1, expression2);
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getArithmeticEvaluator().evaluateAdd(context, getExpression1(), getExpression2());
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reduced;
		Expression reduced1=getExpression1().reduceAndReport(evaluatorFactory);
		Expression reduced2=getExpression2().reduceAndReport(evaluatorFactory);
		if (reduced1 instanceof NumericConstantExpression && reduced2 instanceof NumericConstantExpression)
		{
			Number result=evaluatorFactory.getArithmeticEvaluator().evaluateAdd(DummyContext.getInstance(), reduced1, reduced2);
			reduced=new NumericConstantExpression(result.toString(), result);
		}
		else if (reduced1 instanceof TextConstantExpression || reduced2 instanceof TextConstantExpression)
		{
			String result=evaluatorFactory.getTextEvaluator().evaluateConcat(null, reduced1, reduced2);
			reduced=new TextConstantExpression(result);
		}
		else
		{
			reduced=this;
		}
		return reduced;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return evaluatorFactory.getArithmeticEvaluator().getDatatypeForAdd(context, getExpression1(), getExpression2());
	}
}
