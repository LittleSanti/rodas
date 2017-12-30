package com.samajackun.rodas.sql.model;

public class UsingJoinedSource extends OnJoinedSource
{
	private final Expression using;

	public UsingJoinedSource(Source sourceLeft, Source sourceRight, Type type, IdentifierExpression using)
	{
		// FIXME Falta especificar que using se refiere a source1 y a source2:
		super(sourceLeft, sourceRight, type, new EqualsExpression("=", using, using));
		this.using=using;
	}

	public Expression getUsing()
	{
		return this.using;
	}

	@Override
	public String toCode()
	{
		return getLeftSource().toCode() + " " + getType() + " " + " USING " + this.using.toCode();
	}
}
