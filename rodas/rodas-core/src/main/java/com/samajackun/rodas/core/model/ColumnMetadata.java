package com.samajackun.rodas.core.model;

public class ColumnMetadata
{
	private final String name;

	private final Datatype datatype;

	private final boolean nullable;

	public ColumnMetadata(String name, Datatype datatype, boolean nullable)
	{
		super();
		this.name=name;
		this.datatype=datatype;
		this.nullable=nullable;
	}

	public String getName()
	{
		return this.name;
	}

	public Datatype getDatatype()
	{
		return this.datatype;
	}

	public boolean isNullable()
	{
		return this.nullable;
	}

}
