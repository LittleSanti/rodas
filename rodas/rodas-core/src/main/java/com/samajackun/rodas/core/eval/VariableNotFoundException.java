package com.samajackun.rodas.core.eval;

public class VariableNotFoundException extends EvaluationException
{
	private static final long serialVersionUID=-3876535480389152497L;

	private final Name name;

	public VariableNotFoundException(Name name)
	{
		super("Variable '" + name.asString() + "' not found");
		this.name=name;
	}

	public Name getName()
	{
		return this.name;
	}

}
