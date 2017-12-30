package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;

public interface Engine
{
	public Cursor execute(TableSource source, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;

	public Cursor execute(SelectSentence source, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;

	public Cursor execute(OnJoinedSource source, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;
}
