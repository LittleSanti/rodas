package com.samajackun.rodas.core.eval.functions;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;

public interface FunctionReference
{
	public Object evaluate(Context context, List<Expression> arguments)
		throws EvaluationException;

	public boolean isDeterministic();
}
