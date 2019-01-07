package com.samajackun.rodas.core.eval;


import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.TableSource;

public interface QueryEvaluator
{
	public Object evaluate(Context context, SelectSentence expression)
		throws EvaluationException;

	public Object evaluate(Context context, TableSource expression)
		throws EvaluationException;
}
