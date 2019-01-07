package com.samajackun.rodas.core.eval;

public interface RowResult
{
	public void addAlias(String key, Object value)
		throws EvaluationException;
}
