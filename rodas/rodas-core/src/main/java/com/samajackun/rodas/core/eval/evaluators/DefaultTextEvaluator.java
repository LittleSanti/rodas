package com.samajackun.rodas.core.eval.evaluators;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.TextEvaluator;
import com.samajackun.rodas.core.eval.UnsuportedDatatypeException;
import com.samajackun.rodas.core.model.Expression;

public class DefaultTextEvaluator extends AbstractEvaluator implements TextEvaluator
{

	public DefaultTextEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	public String evaluateConcat(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		String result;
		Object value1=expression1.evaluate(context, getEvaluatorFactory());
		Object value2=expression2.evaluate(context, getEvaluatorFactory());
		if (value1 == null || value2 == null)
		{
			result=null;
		}
		else if (value1 instanceof String)
		{
			if (value2 instanceof String)
			{
				String string1=(String)value1;
				String string2=(String)value2;
				result=string1 + string2;
			}
			else
			{
				throw new EvaluationException("concatenation should receive STRING types");
			}
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return result;
	}
}
