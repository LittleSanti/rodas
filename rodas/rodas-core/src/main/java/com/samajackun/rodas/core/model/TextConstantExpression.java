package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class TextConstantExpression extends ConstantExpression
{
	private static final long serialVersionUID=8553911440861992794L;

	public TextConstantExpression(String value)
	{
		super(value);
	}

	@Override
	public Object evaluateOnce(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getBaseEvaluator().evaluate(context, this);
	}

	@Override
	public String toCode()
	{
		return "'" + super.toCode() + "'";
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.TEXT;
	}

	@Override
	public Object evaluateAsConstant()
	{
		return getValue();
	}
}
