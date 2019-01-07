package com.samajackun.rodas.core.model;

import java.util.Set;

import com.samajackun.rodas.core.eval.EvaluationException;

public interface Executor
{
	public Object evaluate()
		throws EvaluationException;

	public Set<Source> browseAllReferencedSources();
}
