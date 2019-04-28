package com.samajackun.rodas.core.eval;

import java.util.List;

import com.samajackun.rodas.core.model.Expression;

public interface FunctionEvaluator
{
	public Object evaluate(Context context, Expression functionObject, List<Expression> arguments)
		throws EvaluationException;

	// public Datatype getDatatype(Context context, String function, List<Expression> arguments)
	// throws MetadataException;

}
