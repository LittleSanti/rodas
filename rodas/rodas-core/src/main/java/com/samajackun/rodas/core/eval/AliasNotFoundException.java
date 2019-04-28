package com.samajackun.rodas.core.eval;

public class AliasNotFoundException extends EvaluationException
{
	private static final long serialVersionUID=1024021600844960934L;

	private final String alias;

	public AliasNotFoundException(String alias)
	{
		super("Alias " + alias + " not found");
		this.alias=alias;
	}

	public String getAlias()
	{
		return this.alias;
	}
}
