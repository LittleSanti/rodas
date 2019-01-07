package com.samajackun.rodas.core.eval;

public class ParameterNotFoundException extends EvaluationException
{
	private static final long serialVersionUID=4449495800377112331L;

	private final String parameter;

	public ParameterNotFoundException(String parameter)
	{
		super("Parameter '" + parameter + "' not found in context");
		this.parameter=parameter;
	}

	public String getParameter()
	{
		return this.parameter;
	}
}
