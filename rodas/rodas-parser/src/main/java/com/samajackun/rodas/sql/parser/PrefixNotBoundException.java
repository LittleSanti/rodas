package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.core.eval.EvaluationException;

public class PrefixNotBoundException extends EvaluationException
{
	private static final long serialVersionUID=-2781080079033512192L;

	private final String prefix;

	public PrefixNotBoundException(String prefix)
	{
		super("Prefix '" + prefix + "' not bound in current context");
		this.prefix=prefix;
	}

	public String getPrefix()
	{
		return this.prefix;
	}
}
