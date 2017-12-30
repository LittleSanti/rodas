package com.samajackun.rodas.sql.model;

public interface IBooleanExpressionFactory extends IExpressionFactory
{
	@Override
	public BooleanExpression create(Expression e);
}
