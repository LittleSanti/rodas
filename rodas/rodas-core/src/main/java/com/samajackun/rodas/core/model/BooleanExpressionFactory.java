package com.samajackun.rodas.core.model;

public interface BooleanExpressionFactory
{
	public BooleanExpression createBinaryExpression(String operator, Expression expression1, Expression expression2);
}
