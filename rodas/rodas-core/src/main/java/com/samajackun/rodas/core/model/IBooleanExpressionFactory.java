package com.samajackun.rodas.core.model;

public interface IBooleanExpressionFactory extends IExpressionFactory
{
	@Override
	public BooleanExpression create(Expression e);
}
