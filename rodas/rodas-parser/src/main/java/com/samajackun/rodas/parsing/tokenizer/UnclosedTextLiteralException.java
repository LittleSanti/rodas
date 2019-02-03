package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.source.Source;

public class UnclosedTextLiteralException extends TokenizerException
{
	private static final long serialVersionUID=3798533023211315813L;

	public UnclosedTextLiteralException(Source source)
	{
		super(source, "Text literal not closed");
	}
}
