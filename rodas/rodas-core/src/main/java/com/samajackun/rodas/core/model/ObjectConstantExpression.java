package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ObjectConstantExpression extends ConstantExpression
{
	private static final long serialVersionUID=-4366912034961228657L;

	private final Object value;

	public ObjectConstantExpression(Object value)
	{
		super(value == null
			? "null"
			: value.toString());
		this.value=value;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.OBJECT;
	}

	@Override
	protected Object evaluateOnce(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.value;
	}

	@Override
	public Object evaluateAsConstant()
		throws EvaluationException
	{
		return this.value;
	}

}
