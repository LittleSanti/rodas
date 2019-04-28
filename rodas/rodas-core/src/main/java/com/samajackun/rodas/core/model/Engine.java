package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

public interface Engine
{
	public Cursor execute(TableSource source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;

	public Cursor execute(SelectSentence source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;

	public Cursor execute(OnJoinedSource source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;

	public Cursor execute(CrossSource crossSource, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;
}
