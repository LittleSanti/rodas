package com.samajackun.rodas.core.eval.functions;

public class InvalidParameterType extends FunctionEvaluationException
{
	private static final long serialVersionUID=-150756098282276112L;

	public InvalidParameterType(String functionName, String parameterName, String expectedType)
	{
		super("Parameter '" + parameterName + "' must be a " + expectedType, functionName);
	}
}
