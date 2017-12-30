package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.sql.RodasSqlException;

public class ParserException extends RodasSqlException
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
