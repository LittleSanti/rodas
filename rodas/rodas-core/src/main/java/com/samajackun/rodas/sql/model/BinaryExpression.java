package com.samajackun.rodas.sql.model;

public abstract class BinaryExpression implements Expression
{
	private final String operator;

	private final Expression expression1;

	private final Expression expression2;

	public BinaryExpression(String operator, Expression expression1, Expression expression2)
	{
		super();
		this.operator=operator;
		this.expression1=expression1;
		this.expression2=expression2;
	}

	public String getOperator()
	{
		return this.operator;
	}

	public Expression getExpression1()
	{
		return this.expression1;
	}

	public Expression getExpression2()
	{
		return this.expression2;
	}

	@Override
	public String toCode()
	{
		return "((" + this.expression1.toCode() + ")" + this.operator + "(" + this.expression2.toCode() + "))";
	}

	@Override
	public String toString()
	{
		return toCode();
	}

}
