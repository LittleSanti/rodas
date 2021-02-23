package com.samajackun.rodas.core.model;

public class OrderClause implements Codeable
{
	private static final long serialVersionUID=-1359852623125066497L;

	private final Expression expression;

	public enum Direction {
		ASCENDING, DESCENDING
	};

	private final Direction direction;

	public OrderClause(Expression expression, Direction direction)
	{
		super();
		this.expression=expression;
		this.direction=direction;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

	public boolean isAscending()
	{
		return this.direction == Direction.ASCENDING;
	}

	@Override
	public String toCode()
	{
		String s=this.expression.toCode();
		if (this.direction == Direction.DESCENDING)
		{
			s+=" DESC";
		}
		return s;
	}
}
