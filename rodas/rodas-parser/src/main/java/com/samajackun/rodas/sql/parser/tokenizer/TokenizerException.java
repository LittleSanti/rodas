package com.samajackun.rodas.sql.parser.tokenizer;

import com.samajackun.rodas.sql.parser.ParserException;

public class TokenizerException extends ParserException
{
	private static final long serialVersionUID=8467630853778732936L;

	public TokenizerException()
	{
		super();
	}

	public TokenizerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TokenizerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TokenizerException(String message)
	{
		super(message);
	}

	public TokenizerException(Throwable cause)
	{
		super(cause);
	}
}
