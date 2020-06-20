package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public abstract class UnitExpression implements Expression
{
	private static final long serialVersionUID=5894835190673624342L;

	private final String operator;

	private final List<Expression> operands=new ArrayList<>(1);

	public UnitExpression(String operator, Expression expression)
	{
		super();
		this.operator=operator;
		this.operands.add(expression);
	}

	public String getOperator()
	{
		return this.operator;
	}

	public Expression getExpression()
	{
		return this.operands.get(0);
	}

	@Override
	public String toCode()
	{
		return this.operator + this.operands.get(0).toCode();
	}

	@Override
	public String toString()
	{
		return toCode();
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return this.operands.get(0).getDatatype(context, evaluatorFactory);
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
		UnitExpression other=(UnitExpression)obj;
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
