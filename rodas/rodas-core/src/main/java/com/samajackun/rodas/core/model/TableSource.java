package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

public class TableSource implements Source
{
	private static final long serialVersionUID=3785987493745142450L;

	private final String table;

	public TableSource(String table)
	{
		super();
		this.table=table;
	}

	public String getTable()
	{
		return this.table;
	}

	@Override
	public String toCode()
	{
		return this.table;
	}

	@Override
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return engine.execute(this, context);
	}
}
