package com.samajackun.rodas.sql.model;

public interface BooleanExpressionFactory
{
	public BooleanExpression createBinaryExpression(String operator, Expression expression1, Expression expression2);
}
