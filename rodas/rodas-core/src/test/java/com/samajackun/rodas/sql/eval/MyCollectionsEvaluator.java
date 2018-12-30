package com.samajackun.rodas.sql.eval;

import java.util.List;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.model.Expression;

public class MyCollectionsEvaluator extends AbstractEvaluator implements CollectionsEvaluator
{
	public MyCollectionsEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	public boolean evaluateIn(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=expression1.evaluate(context, getEvaluatorFactory());
		Object value2=expression2.evaluate(context, getEvaluatorFactory());
		boolean x;
		if (value2.getClass().isArray())
		{
			Object[] array=(Object[])value2;
			x=false;
			for (int i=0; !x && i < array.length; i++)
			{
				Object item=array[i];
				x=(value1 == null)
					? item == null
					: (value1.equals(item));
				System.out.println("x=" + x + ", value1=" + value1 + ", item=" + item);
			}
		}
		else
		{
			x=(value1 == null)
				? value2 == null
				: (value1.equals(value2));
		}
		return x;
	}

	@Override
	public boolean evaluateExists(Context context, Expression expression1)
		throws EvaluationException
	{
		boolean x;
		Object value=expression1.evaluate(context, getEvaluatorFactory());
		if (value.getClass().isArray())
		{
			Object[] array=(Object[])value;
			x=array.length > 0;
		}
		else
		{
			x=value != null;
		}
		return x;
	}

	@Override
	public Object evaluateAsterisk(Context context, Expression expression)
		throws EvaluationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object evaluateList(Context context, List<Expression> expressions)
		throws EvaluationException
	{
		Object[] result=new Object[expressions.size()];
		int i=0;
		for (Expression expression : expressions)
		{
			result[i++]=expression.evaluate(context, getEvaluatorFactory());
		}
		return result;
	}
}
