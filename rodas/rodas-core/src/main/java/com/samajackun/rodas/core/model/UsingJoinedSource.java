package com.samajackun.rodas.core.model;

public class UsingJoinedSource extends OnJoinedSource
{
	private static final long serialVersionUID=-8763733749543080956L;

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
