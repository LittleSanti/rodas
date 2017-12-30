package com.samajackun.rodas.sql.model;

import java.util.Set;

import com.samajackun.rodas.sql.eval.EvaluationException;

public interface Executor
{
	public Object evaluate()
		throws EvaluationException;

	public Set<Source> browseAllReferencedSources();
}
