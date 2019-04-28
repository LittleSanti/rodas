package com.samajackun.rodas.core.eval.evaluators;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.TextEvaluator;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.TextUtils;

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
		Object value1=context.evaluate(expression1, getEvaluatorFactory());
		Object value2=context.evaluate(expression2, getEvaluatorFactory());
		if (value1 == null || value2 == null)
		{
			result=null;
		}
		else
		{
			String string1=TextUtils.toString(value1);
			String string2=TextUtils.toString(value1);
			result=string1 + string2;
			// throw new UnsuportedDatatypeException(value1.getClass());
		}
		return result;
	}
}
