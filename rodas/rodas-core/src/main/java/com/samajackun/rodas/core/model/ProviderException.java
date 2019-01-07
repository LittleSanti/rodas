package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.RodasException;

public class ProviderException extends RodasException
{
	private static final long serialVersionUID=480834493080921947L;

	public ProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ProviderException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ProviderException(String message)
	{
		super(message);
	}

	public ProviderException(Throwable cause)
	{
		super(cause);
	}
}
