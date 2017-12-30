package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.RodasSqlException;

public class MetadataException extends RodasSqlException
{
	private static final long serialVersionUID=1L;

	public MetadataException()
	{
	}

	public MetadataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MetadataException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MetadataException(String message)
	{
		super(message);
	}

	public MetadataException(Throwable cause)
	{
		super(cause);
	}
}
