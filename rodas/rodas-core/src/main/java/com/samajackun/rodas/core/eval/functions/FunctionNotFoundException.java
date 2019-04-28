package com.samajackun.rodas.core.eval.functions;

public class FunctionNotFoundException extends FunctionEvaluationException
{
	private static final long serialVersionUID=3786518717244166983L;

	public FunctionNotFoundException(String function)
	{
		super("Function " + function + " not found", function);
	}
}
