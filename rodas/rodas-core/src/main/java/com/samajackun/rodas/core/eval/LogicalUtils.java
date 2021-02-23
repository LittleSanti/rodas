package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.Expression;

public class LogicalUtils
{

	private LogicalUtils()
	{
	}

	public static Boolean computeAnd(Object value1, Object value2, Expression expression)
		throws EvaluationException
	{
		return toBoolean(value1) && toBoolean(value2);
	}

	public static Boolean computeOr(Object value1, Object value2, Expression expression)
		throws EvaluationException
	{
		return toBoolean(value1) || toBoolean(value2);
	}

	public static Boolean computeNot(Object value1, Expression expression)
		throws EvaluationException
	{
		return !toBoolean(value1);
	}

	public static boolean toBoolean(Object value)
		throws EvaluationException
	{
		boolean v;
		if (value != null)
		{
			if (value instanceof Boolean)
			{
				v=(Boolean)value;
			}
			else if (value instanceof Number)
			{
				v=((Number)value).longValue() != 0;
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
}
