package com.samajackun.rodas.core.eval;

public class NameNotBoundException extends NotBoundException
{
	private static final long serialVersionUID=-3269041791274763337L;

	private final String name;

	public NameNotBoundException(String name, String context)
	{
		super(context, "name '" + name + "' not bound in context" + (context != null
			? " '" + context + "'"
			: ""));
		this.name=name;
	}

	public NameNotBoundException(String name)
	{
		this(name, null);
	}

	public String getName()
	{
		return this.name;
	}
}
