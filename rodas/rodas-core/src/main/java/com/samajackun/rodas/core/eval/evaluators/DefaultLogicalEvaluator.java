package com.samajackun.rodas.core.eval.evaluators;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.LogicalEvaluator;
import com.samajackun.rodas.core.model.Expression;

public class DefaultLogicalEvaluator extends AbstractEvaluator implements LogicalEvaluator
{
	public DefaultLogicalEvaluator(EvaluatorFactory evaluator)
	{
		super(evaluator);
	}

	private boolean toBoolean(Context context, Expression exp)
		throws EvaluationException
	{
		Object value=context.evaluate(exp, getEvaluatorFactory());
		boolean v;
		if (value != null)
		{
			if (value instanceof Boolean)
			{
				v=(Boolean)value;
			}
			else if (value instanceof Number)
			{
				v=((Number)value).intValue() != 0;
			}
			else if (value instanceof String)
			{
				v=!((String)value).isEmpty();
			}
			else
			{
				v=true;
			}
		}
		else
		{
			v=false;
		}
		return v;
	}

	@Override
	public boolean evaluateAnd(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		return toBoolean(context, expression1) && toBoolean(context, expression2);
	}

	@Override
	public boolean evaluateOr(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		return toBoolean(context, expression1) || toBoolean(context, expression2);
	}

	@Override
	public boolean evaluateNot(Context context, Expression expression1)
		throws EvaluationException
	{
		return !toBoolean(context, expression1);
	}
}
