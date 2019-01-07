package com.samajackun.rodas.core.eval;

public class UnsuportedDatatypeException extends EvaluationException
{
	private static final long serialVersionUID=-8396480665330409972L;

	private final Class<?> datatype;

	public UnsuportedDatatypeException(Class<?> datatype)
	{
		super("Unsuported datatype " + datatype.getName());
		this.datatype=datatype;
	}

	public Class<?> getDatatype()
	{
		return this.datatype;
	}
}
