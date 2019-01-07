package com.samajackun.rodas.core.model;

public class ScopedIdentifierExpression extends IdentifierExpression
{
	private final String scope;

	public ScopedIdentifierExpression(String scope, String identifier)
	{
		super(identifier);
		this.scope=scope;
	}

	@Override
	public String toCode()
	{
		return this.scope + "." + super.toCode();
	}
}
