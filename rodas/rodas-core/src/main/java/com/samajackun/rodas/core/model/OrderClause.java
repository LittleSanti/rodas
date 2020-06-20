package com.samajackun.rodas.core.model;

public class OrderClause implements Codeable
{
	private static final long serialVersionUID=-1359852623125066497L;

	private final Expression expression;

	private final boolean ascending;

	public OrderClause(Expression expression, boolean ascending)
	{
		super();
		this.expression=expression;
		this.ascending=ascending;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

	public boolean isAscending()
	{
		return this.ascending;
	}

	@Override
	public String toCode()
	{
		String s=this.expression.toCode();
		if (!this.ascending)
		{
			s+=" DESC";
		}
		return s;
	}
}
