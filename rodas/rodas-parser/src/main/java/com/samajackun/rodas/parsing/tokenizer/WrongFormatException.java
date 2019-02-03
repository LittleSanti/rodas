package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.source.Source;

public class WrongFormatException extends TokenizerException
{
	private static final long serialVersionUID=-7084167284894985621L;

	public WrongFormatException(Source source, String message, Throwable cause)
	{
		super(source, message, cause);
	}

	public WrongFormatException(Source source, String message)
	{
		super(source, message);
	}

}
