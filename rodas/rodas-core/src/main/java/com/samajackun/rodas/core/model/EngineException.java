package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.RodasException;

public class EngineException extends RodasException
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
