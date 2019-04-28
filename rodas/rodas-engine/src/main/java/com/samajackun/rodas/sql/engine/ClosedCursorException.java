package com.samajackun.rodas.sql.engine;

import com.samajackun.rodas.core.execution.CursorException;

public class ClosedCursorException extends CursorException
{
	private static final long serialVersionUID=1310116382851025363L;

	public ClosedCursorException()
	{
		super("Closed cursor");
	}
}
