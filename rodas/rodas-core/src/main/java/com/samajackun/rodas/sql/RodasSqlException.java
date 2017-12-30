package com.samajackun.rodas.sql;

public class RodasSqlException extends Exception
{
	private static final long serialVersionUID=-9027450793578126776L;

	public RodasSqlException()
	{
		super();
	}

	public RodasSqlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RodasSqlException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RodasSqlException(String message)
	{
		super(message);
	}

	public RodasSqlException(Throwable cause)
	{
		super(cause);
	}

}
