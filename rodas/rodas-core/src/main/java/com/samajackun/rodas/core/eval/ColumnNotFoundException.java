package com.samajackun.rodas.core.eval;

public class ColumnNotFoundException extends EvaluationException
{
	private static final long serialVersionUID=8134740501517159278L;

	private final String prefix;

	private final String column;

	public ColumnNotFoundException(String column)
	{
		this(column, null);
	}

	public ColumnNotFoundException(String column, String prefix)
	{
		super("Column '" + serialize(prefix, column) + "' not found");
		this.column=column;
		this.prefix=prefix;
	}

	private static String serialize(String prefix, String column)
	{
		return (prefix == null
			? prefix + "."
			: "") + column;
	}

	public String getColumn()
	{
		return this.column;
	}

	public String getPrefix()
	{
		return this.prefix;
	}

}
