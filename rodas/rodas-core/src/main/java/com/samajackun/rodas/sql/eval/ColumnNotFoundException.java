package com.samajackun.rodas.sql.eval;

public class ColumnNotFoundException extends EvaluationException
{
	private static final long serialVersionUID=8134740501517159278L;

	private final String column;

	public ColumnNotFoundException(String column)
	{
		super("Column '" + column + "' not found");
		this.column=column;
	}

	public String getColumn()
	{
		return this.column;
	}
}
