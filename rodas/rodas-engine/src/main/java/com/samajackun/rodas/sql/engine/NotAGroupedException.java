package com.samajackun.rodas.sql.engine;

import com.samajackun.rodas.core.model.Expression;

public class NotAGroupedException extends Exception
{
	private static final long serialVersionUID=8462947484117682578L;

	private final Expression expression;

	public NotAGroupedException(Expression expression)
	{
		this.expression=expression;
	}

	public Expression getExpression()
	{
		return this.expression;
	}
}
