package com.samajackun.rodas.sql.eval;


import com.samajackun.rodas.sql.model.SelectSentence;
import com.samajackun.rodas.sql.model.TableSource;

public interface QueryEvaluator
{
	public Object evaluate(Context context, SelectSentence expression)
		throws EvaluationException;

	public Object evaluate(Context context, TableSource expression)
		throws EvaluationException;
}
