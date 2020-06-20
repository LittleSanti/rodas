package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.DummyContext;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public abstract class RelationalBinaryExpression extends BinaryExpression implements BooleanExpression
{
	private static final long serialVersionUID=5832978806302648865L;

	public RelationalBinaryExpression(String operator, Expression expression1, Expression expression2)
	{
		super(operator, expression1, expression2);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reduced;
		Expression reduced1=getExpression1().reduceAndReport(evaluatorFactory);
		Expression reduced2=getExpression2().reduceAndReport(evaluatorFactory);
		// @formatter:off
		if ((reduced1 instanceof BooleanConstantExpression && reduced2 instanceof BooleanConstantExpression)
			|| ((reduced1 instanceof NumericConstantExpression && reduced2 instanceof NumericConstantExpression))
            || ((reduced1 instanceof TextConstantExpression && reduced2 instanceof TextConstantExpression)))
		// @formatter:on
		{
			Boolean result=compute(DummyContext.getInstance(), evaluatorFactory, reduced1, reduced2);
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
		return compute(context, evaluatorFactory, getExpression1(), getExpression2());
	}

	protected abstract Boolean compute(Context context, EvaluatorFactory evaluatorFactory, Expression expression1, Expression expression2)
		throws EvaluationException;
}
