package com.samajackun.rodas.core.eval.functions;

public class ExactNumberOfArgumentsRequiredException extends FunctionEvaluationException
{
	private static final long serialVersionUID=-7499024800893274743L;

	public ExactNumberOfArgumentsRequiredException(String function, int minimum)
	{
		super("Function " + function + " requires " + minimum + " arguments", function);
	}
}
