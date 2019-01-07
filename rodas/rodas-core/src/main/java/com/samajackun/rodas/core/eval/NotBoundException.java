package com.samajackun.rodas.core.eval;

public class NotBoundException extends EvaluationException
{
	private static final long serialVersionUID=306581623684288842L;

	private final String context;

	public NotBoundException(String context, String message)
	{
		super(message);
		this.context=context;
	}

	public String getContext()
	{
		return this.context;
	}
}