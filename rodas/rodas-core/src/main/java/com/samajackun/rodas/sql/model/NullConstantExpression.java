package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class NullConstantExpression extends ConstantExpression implements Expression
{
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
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory) throws MetadataException
	{
		return Datatype.NULL;
	}
}
