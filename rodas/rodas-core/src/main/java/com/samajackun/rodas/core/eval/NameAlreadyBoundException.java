package com.samajackun.rodas.core.eval;

public class NameAlreadyBoundException extends EvaluationException
{
	private static final long serialVersionUID=-8333427268357274794L;

	private final String name;

	public NameAlreadyBoundException(String name)
	{
		super("Name '" + name + "' already bound to this context");
		this.name=name;
	}

	public String getName()
	{
		return this.name;
	}
}
