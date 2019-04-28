package com.samajackun.rodas.core.execution;

public class ExhaustedCursorException extends CursorException
{
	private static final long serialVersionUID=3633599778078887323L;

	public ExhaustedCursorException()
	{
		this("Exhausted cursor");
	}

	public ExhaustedCursorException(String s)
	{
		super(s);
	}
}
