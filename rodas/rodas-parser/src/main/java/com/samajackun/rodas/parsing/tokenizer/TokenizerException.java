package com.samajackun.rodas.parsing.tokenizer;

import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.Source;

public class TokenizerException extends ParserException
{
	private static final long serialVersionUID=8467630853778732936L;

	private final int line;

	private final int column;

	private final int index;

	public TokenizerException(Source source, String message, Throwable cause)
	{
		super(formatMessage(message, source), cause);
		this.line=source.getCurrentLine();
		this.column=source.getCurrentColumn();
		this.index=source.getCurrentIndex();
	}

	public TokenizerException(Source source, String message)
	{
		super(formatMessage(message, source));
		this.line=source.getCurrentLine();
		this.column=source.getCurrentColumn();
		this.index=source.getCurrentIndex();
	}

	private static String formatMessage(String message, Source source)
	{
		return message + " at line " + source.getCurrentLine() + " column " + source.getCurrentColumn();
	}

	public int getLine()
	{
		return this.line;
	}

	public int getColumn()
	{
		return this.column;
	}

	public int getIndex()
	{
		return this.index;
	}
}
