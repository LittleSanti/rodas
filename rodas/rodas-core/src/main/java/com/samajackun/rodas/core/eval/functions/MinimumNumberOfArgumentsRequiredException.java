package com.samajackun.rodas.core.eval.functions;

public class MinimumNumberOfArgumentsRequiredException extends FunctionEvaluationException
{
	private static final long serialVersionUID=-7499024800893274743L;

	public MinimumNumberOfArgumentsRequiredException(String function, int minimum)
	{
		super("Function " + function + " requires at least " + minimum + " arguments", function);
	}
}
