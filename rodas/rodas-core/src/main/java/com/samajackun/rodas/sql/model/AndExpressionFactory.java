package com.samajackun.rodas.sql.model;

public class AndExpressionFactory implements BooleanExpressionFactory
{
	@Override
	public BooleanExpression createBinaryExpression(String operator, Expression expression1, Expression expression2)
	{
		return new AndExpression(operator, expression1, expression2);
	}
}
