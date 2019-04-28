package com.samajackun.rodas.sql.parser;

import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ParserContext
{
	private EvaluatorFactory evaluationFactory;

	public EvaluatorFactory getEvaluationFactory()
	{
		return this.evaluationFactory;
	}

	public void setEvaluationFactory(EvaluatorFactory evaluationFactory)
	{
		this.evaluationFactory=evaluationFactory;
	}
}
