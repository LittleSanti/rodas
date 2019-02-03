package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.source.Source;

public class EndOfStreamException extends TokenizerException
{
	private static final long serialVersionUID=2891676661429476016L;

	public EndOfStreamException(Source source)
	{
		super(source, "End of stream");
	}
}
