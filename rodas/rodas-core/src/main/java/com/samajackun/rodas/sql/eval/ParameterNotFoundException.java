package com.samajackun.rodas.sql.eval;

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
