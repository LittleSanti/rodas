package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.OneRowCursor;

public class OneRowSource implements Source
{
	private static final long serialVersionUID=1864556483967623653L;

	private static final OneRowSource INSTANCE=new OneRowSource();

	public static OneRowSource getInstance()
	{
		return INSTANCE;
	}

	private OneRowSource()
	{
	}

	@Override
	public String toCode()
	{
		return "";
	}

	@Override
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return new OneRowCursor();
	}
}
