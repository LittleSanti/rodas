package com.samajackun.rodas.sql.engine;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.execution.CursorException;

public class AmbiguousColumnException extends CursorException
{
	private static final long serialVersionUID=-5619440437866212355L;

	private final Name name;

	public AmbiguousColumnException(Name name)
	{
		super("Ambiguous column " + name.asString());
		this.name=name;
	}

	public Name getName()
	{
		return this.name;
	}
}
