package com.samajackun.rodas.core.eval.evaluators;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.CollectionsEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Expression;

public class DefaultCollectionsEvaluator extends AbstractEvaluator implements CollectionsEvaluator
{
	public DefaultCollectionsEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	public boolean evaluateIn(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=context.evaluate(expression1, getEvaluatorFactory());
		Object value2=context.evaluate(expression2, getEvaluatorFactory());
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
		Object value=context.evaluate(expression1, getEvaluatorFactory());
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
		List<Object> result=new ArrayList<>(expressions.size());
		for (Expression expression : expressions)
		{
			result.add(context.evaluate(expression, getEvaluatorFactory()));
		}
		return result;
	}
}
