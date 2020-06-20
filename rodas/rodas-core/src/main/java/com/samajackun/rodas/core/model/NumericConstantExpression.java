package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class NumericConstantExpression extends ConstantExpression
{
	private static final long serialVersionUID=-1435205172218413065L;

	private final Number numericValue;

	public NumericConstantExpression(String value, Number numericValue)
	{
		super(value);
		this.numericValue=numericValue;
	}

	public Number getNumericValue()
	{
		return this.numericValue;
	}

	@Override
	public Object evaluateOnce(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getBaseEvaluator().evaluate(context, this);
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return evaluatorFactory.getBaseEvaluator().getDatatype(context, this);
	}

	@Override
	public Object evaluateAsConstant()
	{
		return this.numericValue;
	}
}
