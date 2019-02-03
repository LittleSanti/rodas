package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class BooleanConstantExpression extends ConstantExpression implements BooleanExpression
{
	private final boolean booleanValue;

	public BooleanConstantExpression(String value, boolean booleanValue)
	{
		super(value);
		this.booleanValue=booleanValue;
	}

	@Override
	public Boolean evaluateOnce(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getBaseEvaluator().evaluate(context, this);
	}

	public static BooleanConstantExpression createTrueConstrantExpression(String value)
	{
		return new BooleanConstantExpression(value, true);
	}

	public static BooleanConstantExpression createFalseConstrantExpression(String value)
	{
		return new BooleanConstantExpression(value, false);
	}

	public boolean getBooleanValue()
	{
		return this.booleanValue;
	}

	@Override
	public Boolean evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return Boolean.class.cast(super.evaluate(context, evaluatorFactory));
	}

	@Override
	public Object evaluateAsConstant()
	{
		return this.booleanValue;
	}
}
