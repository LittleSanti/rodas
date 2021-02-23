package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

public interface Source extends Codeable
{
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;

	default public String getAlias()
	{
		return null;
	}
}
