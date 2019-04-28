package com.samajackun.rodas.core.model;

public abstract class AbstractSource implements Source
{
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
