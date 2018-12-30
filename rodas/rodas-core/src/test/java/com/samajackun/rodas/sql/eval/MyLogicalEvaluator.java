package com.samajackun.rodas.sql.eval;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.model.Expression;

public class MyLogicalEvaluator extends AbstractEvaluator implements LogicalEvaluator
{
	public MyLogicalEvaluator(EvaluatorFactory evaluator)
	{
		super(evaluator);
	}

	private boolean toBoolean(Context context, Expression exp)
		throws EvaluationException
	{
		Object value=exp.evaluate(context, getEvaluatorFactory());
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
