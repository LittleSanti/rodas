package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public abstract class UnitExpression implements Expression
{
	private final String operator;

	private final Expression expression;

	public UnitExpression(String operator, Expression expression)
	{
		super();
		this.operator=operator;
		this.expression=expression;
	}

	public String getOperator()
	{
		return this.operator;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

	@Override
	public String toCode()
	{
		return this.operator + this.expression.toCode();
	}

	@Override
	public String toString()
	{
		return toCode();
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory) throws MetadataException
	{
		return this.expression.getDatatype(context, evaluatorFactory);
	}
}
