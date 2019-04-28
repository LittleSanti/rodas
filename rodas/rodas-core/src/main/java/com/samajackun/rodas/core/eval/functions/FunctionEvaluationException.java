package com.samajackun.rodas.core.eval.functions;

import java.util.List;

import com.samajackun.rodas.core.eval.EvaluationException;

public class FunctionEvaluationException extends EvaluationException
{
	private static final long serialVersionUID=878870882277901505L;

	private final String function;

	private final List<Object> arguments;

	public FunctionEvaluationException(String message, String function)
	{
		this(message, function, null);
	}

	public FunctionEvaluationException(String message, String function, List<Object> arguments)
	{
		super(message);
		this.function=function;
		this.arguments=arguments;
	}

	public String getFunction()
	{
		return this.function;
	}

	public List<Object> getArguments()
	{
		return this.arguments;
	}
}
