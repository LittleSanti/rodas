package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public interface BooleanExpression extends Expression
{
	@Override
	public Boolean evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException;

	@Override
	default Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.BOOLEAN;
	}
}
