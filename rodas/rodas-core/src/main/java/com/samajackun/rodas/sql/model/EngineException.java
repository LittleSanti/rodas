package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.RodasSqlException;

public class EngineException extends RodasSqlException
{
	private static final long serialVersionUID=-6132084407089066606L;

	public EngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EngineException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public EngineException(String message)
	{
		super(message);
	}

	public EngineException(Throwable cause)
	{
		super(cause);
	}
}
