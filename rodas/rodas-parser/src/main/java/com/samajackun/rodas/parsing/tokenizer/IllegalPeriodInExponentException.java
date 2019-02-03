package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.source.Source;

public class IllegalPeriodInExponentException extends WrongFormatException
{
	private static final long serialVersionUID=2245507817740868917L;

	public IllegalPeriodInExponentException(Source source)
	{
		super(source, "Wrong number format: Period not allowed in exponent");
	}
}
