package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.source.Source;

public class UnclosedCommentException extends TokenizerException
{
	private static final long serialVersionUID=3798533023211315813L;

	public UnclosedCommentException(Source source)
	{
		super(source, "Comment not closed");
	}
}
