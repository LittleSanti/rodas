package com.samajackun.rodas.core.eval;

public class VariableNotFoundException extends Exception
{
	private static final long serialVersionUID=-3876535480389152497L;

	private final String name;

	public VariableNotFoundException(String name)
	{
		super();
		this.name=name;
	}

	public String getName()
	{
		return this.name;
	}

}
