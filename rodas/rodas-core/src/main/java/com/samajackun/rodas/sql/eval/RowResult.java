package com.samajackun.rodas.sql.eval;

public interface RowResult
{
	public void addAlias(String key, Object value)
		throws EvaluationException;
}
