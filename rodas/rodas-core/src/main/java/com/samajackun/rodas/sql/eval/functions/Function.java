package com.samajackun.rodas.sql.eval.functions;

import java.util.List;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.model.Expression;

public interface Function
{
	public Object evaluate(Context context, List<Expression> arguments)
		throws EvaluationException;

	public boolean isDeterministic();
}
