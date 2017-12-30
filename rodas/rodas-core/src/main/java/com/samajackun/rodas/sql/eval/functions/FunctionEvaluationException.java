package com.samajackun.rodas.sql.eval.functions;

import com.samajackun.rodas.sql.eval.EvaluationException;

public class FunctionEvaluationException extends EvaluationException
{
	private static final long serialVersionUID=878870882277901505L;

	private final String function;

	private final Object[] arguments;

	public FunctionEvaluationException(String message, String function)
	{
		this(message, function, null);
	}

	public FunctionEvaluationException(String message, String function, Object[] arguments)
	{
		super(message);
		this.function=function;
		this.arguments=arguments;
	}

	public String getFunction()
	{
		return this.function;
	}

	public Object[] getArguments()
	{
		return this.arguments;
	}
}
