package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.source.Source;

public class IllegalDoublePeriodException extends WrongFormatException
{
	private static final long serialVersionUID=2245507817740868917L;

	public IllegalDoublePeriodException(Source source)
	{
		super(source, "Wrong number format: Double period in constant");
	}
}
