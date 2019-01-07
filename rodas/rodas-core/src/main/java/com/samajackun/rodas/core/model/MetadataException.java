package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.EvaluationException;

public class MetadataException extends EvaluationException
{
	private static final long serialVersionUID=1L;

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