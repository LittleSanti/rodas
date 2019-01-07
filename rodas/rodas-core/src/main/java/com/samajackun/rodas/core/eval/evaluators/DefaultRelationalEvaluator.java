package com.samajackun.rodas.core.eval.evaluators;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.RelationalEvaluator;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ReationalUtils;

public class DefaultRelationalEvaluator extends AbstractEvaluator implements RelationalEvaluator
{
	public DefaultRelationalEvaluator(DefaultEvaluatorFactory myEvaluatorFactory)
	{
		super(myEvaluatorFactory);
	}

	@Override
	public boolean evaluateLowerThan(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=expression1.evaluate(context, getEvaluatorFactory());
		Object value2=expression2.evaluate(context, getEvaluatorFactory());
		return ReationalUtils.computeLowerThan(value1, value2);
	}

	@Override
	public boolean evaluateLowerThanOrEquals(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=expression1.evaluate(context, getEvaluatorFactory());
		Object value2=expression2.evaluate(context, getEvaluatorFactory());
		return ReationalUtils.computeLowerThanOrEquals(value1, value2);
	}

	@Override
	public boolean evaluateGreaterThan(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=expression1.evaluate(context, getEvaluatorFactory());
		Object value2=expression2.evaluate(context, getEvaluatorFactory());
		return ReationalUtils.computeGreaterThan(value1, value2);
	}

	@Override
	public boolean evaluateGreaterThanOrEquals(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=expression1.evaluate(context, getEvaluatorFactory());
		Object value2=expression2.evaluate(context, getEvaluatorFactory());
		return ReationalUtils.computeGreaterThanOrEquals(value1, value2);
	}

	@Override
	public boolean evaluateEquals(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=expression1.evaluate(context, getEvaluatorFactory());
		Object value2=expression2.evaluate(context, getEvaluatorFactory());
		return ReationalUtils.computeEquals(value1, value2);
	}

	@Override
	public boolean evaluateNotEquals(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=expression1.evaluate(context, getEvaluatorFactory());
		Object value2=expression2.evaluate(context, getEvaluatorFactory());
		return ReationalUtils.computeNotEquals(value1, value2);
	}

	@Override
	public boolean evaluateLike(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean evaluateBetween(Context context, Expression expression1, Expression lower, Expression upper)
		throws EvaluationException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean evaluateIs(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean evaluateIsOfType(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		// TODO Auto-generated method stub
		return false;
	}

}
