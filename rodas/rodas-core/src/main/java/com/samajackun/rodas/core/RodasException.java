package com.samajackun.rodas.core;

public class RodasException extends Exception
{
	private static final long serialVersionUID=-9027450793578126776L;

	public RodasException()
	{
		super();
	}

	public RodasException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RodasException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RodasException(String message)
	{
		super(message);
	}

	public RodasException(Throwable cause)
	{
		super(cause);
	}

}
