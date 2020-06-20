package com.samajackun.rodas.core.model;

public abstract class AbstractSource implements Source
{
	private static final long serialVersionUID=8682854865396694720L;

	private final Provider provider;

	protected AbstractSource(Provider provider)
	{
		super();
		this.provider=provider;
	}

	protected Provider getProvider()
	{
		return this.provider;
	}
}
