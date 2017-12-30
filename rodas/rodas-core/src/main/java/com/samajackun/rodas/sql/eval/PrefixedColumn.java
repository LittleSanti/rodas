package com.samajackun.rodas.sql.eval;

public class PrefixedColumn
{
	private final String prefix;

	private final String column;

	public PrefixedColumn(String prefix, String column)
	{
		super();
		this.prefix=prefix;
		this.column=column;
	}

	public String getPrefix()
	{
		return this.prefix;
	}

	public String getColumn()
	{
		return this.column;
	}
}
