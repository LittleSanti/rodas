package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.TableSource;

public class MyDummyQueryEvaluator implements QueryEvaluator
{
	private final EvaluatorFactory evaluatorFactory;

	public MyDummyQueryEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super();
		this.evaluatorFactory=evaluatorFactory;
	}

	@Override
	public Object evaluate(Context context, SelectSentence select)
		throws EvaluationException
	{
		// FIXME Ahora mismo sólo evalúa la primera de las expresiones de la SELECT e ignora el resto.
		return select.getSelectExpressions().get(0).evaluate(context, this.evaluatorFactory);
	}

	@Override
	public Object evaluate(Context context, TableSource table)
		throws EvaluationException
	{
		return null;
	}

}
