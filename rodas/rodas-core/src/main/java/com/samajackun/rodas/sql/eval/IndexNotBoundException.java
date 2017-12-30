package com.samajackun.rodas.sql.eval;

public class IndexNotBoundException extends NotBoundException
{
	private static final long serialVersionUID=7682924751182254682L;

	private final int index;

	public IndexNotBoundException(int index, String context)
	{
		super(context, "index " + index + " out of range in context" + (context != null
			? " '" + context + "'"
			: ""));
		this.index=index;
	}

	public IndexNotBoundException(int index)
	{
		this(index, null);
	}

	public int getIndex()
	{
		return this.index;
	}
}
