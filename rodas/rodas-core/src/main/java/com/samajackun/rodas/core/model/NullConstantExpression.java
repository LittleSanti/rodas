package com.samajackun.rodas.core.model;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class NullConstantExpression extends ConstantExpression implements Expression
{
	private static final long serialVersionUID=-2053605489408328631L;

	public NullConstantExpression(String value)
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
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.NULL;
	}

	@Override
	public Object evaluateAsConstant()
	{
		return null;
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj.getClass() == getClass();
	}
}
