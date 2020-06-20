package com.samajackun.rodas.core.model;

import java.util.Calendar;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class DatetimeConstantExpression extends ConstantExpression
{
	private static final long serialVersionUID=-7128805434579909759L;

	private final Calendar numericValue;

	public DatetimeConstantExpression(String value, Calendar numericValue)
	{
		super(value);
		this.numericValue=numericValue;
	}

	public Calendar getNumericValue()
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
		return Datatype.DATETIME;
	}

	@Override
	public Object evaluateAsConstant()
	{
		return this.numericValue;
	}
}
