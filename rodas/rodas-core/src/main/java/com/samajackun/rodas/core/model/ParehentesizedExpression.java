package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ParehentesizedExpression implements Expression
{
	private final Expression expression;

	public ParehentesizedExpression(Expression expression)
	{
		super();
		this.expression=expression;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

	@Override
	public String toCode()
	{
		return "(" + this.expression.toCode() + ")";
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.expression.evaluate(context, evaluatorFactory);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.expression.reduce(evaluatorFactory);
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory) throws MetadataException
	{
		return this.expression.getDatatype(context, evaluatorFactory);
	}
}
