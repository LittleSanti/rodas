package com.samajackun.rodas.sql.model;

public class ExpressionWithTooManyColumnsException extends MetadataException
{
	private static final long serialVersionUID=695043488113353921L;

	private final Expression src;

	public ExpressionWithTooManyColumnsException(Expression src)
	{
		super("Expression has more than one column: " + src.toCode());
		this.src=src;
	}

	public Expression getSrc()
	{
		return this.src;
	}
}
