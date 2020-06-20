package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.List;

public abstract class BinaryExpression implements Expression
{
	private static final long serialVersionUID=-5762981021220426108L;

	private final String operator;

	private final List<Expression> operands=new ArrayList<>(2);

	public BinaryExpression(String operator, Expression expression1, Expression expression2)
	{
		super();
		this.operator=operator;
		this.operands.add(expression1);
		this.operands.add(expression2);
	}

	public String getOperator()
	{
		return this.operator;
	}

	public Expression getExpression1()
	{
		return this.operands.get(0);
	}

	public Expression getExpression2()
	{
		return this.operands.get(1);
	}

	@Override
	public String toCode()
	{
		return "((" + this.operands.get(0).toCode() + ")" + this.operator + "(" + this.operands.get(1).toCode() + "))";
	}

	@Override
	public String toString()
	{
		return toCode();
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return this.operands;
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.operands == null)
			? 0
			: this.operands.hashCode());
		result=prime * result + ((this.operator == null)
			? 0
			: this.operator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		BinaryExpression other=(BinaryExpression)obj;
		if (this.operands == null)
		{
			if (other.operands != null)
			{
				return false;
			}
		}
		else if (!this.operands.equals(other.operands))
		{
			return false;
		}
		if (this.operator == null)
		{
			if (other.operator != null)
			{
				return false;
			}
		}
		else if (!this.operator.equals(other.operator))
		{
			return false;
		}
		return true;
	}
}
