package com.samajackun.rodas.core.eval;

public class PrefixNotFoundException extends EvaluationException
{
	private static final long serialVersionUID=165502157976070542L;

	private final String prefix;

	public PrefixNotFoundException(String prefix)
	{
		super("Prefix '" + prefix + "' not found in current context");
		this.prefix=prefix;
	}

	public String getPrefix()
	{
		return this.prefix;
	}
}
