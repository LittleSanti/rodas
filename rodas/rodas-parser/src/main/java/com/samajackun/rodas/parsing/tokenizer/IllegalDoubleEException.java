package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.source.Source;

public class IllegalDoubleEException extends WrongFormatException
{
	private static final long serialVersionUID=2245507817740868917L;

	public IllegalDoubleEException(Source source)
	{
		super(source, "Wrong number format: Double E in constant");
	}
}
