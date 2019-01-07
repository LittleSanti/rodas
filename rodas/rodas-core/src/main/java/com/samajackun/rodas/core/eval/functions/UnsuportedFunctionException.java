package com.samajackun.rodas.core.eval.functions;

public class UnsuportedFunctionException extends FunctionEvaluationException
{
	private static final long serialVersionUID=3786518717244166983L;

	public UnsuportedFunctionException(String function)
	{
		super("Function " + function + " not found", function);
	}
}
