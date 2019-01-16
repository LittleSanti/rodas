package com.samajackun.rodas.parsing.parser;

import com.samajackun.rodas.core.RodasException;

public class ParserException extends RodasException
{
	private static final long serialVersionUID=8467630853778732936L;

	public ParserException()
	{
		super();
	}

	public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ParserException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ParserException(String message)
	{
		super(message);
	}

	public ParserException(Throwable cause)
	{
		super(cause);
	}
}
