package com.samajackun.rodas.core.eval;

import java.util.List;

import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public interface FunctionEvaluator
{
	public Object evaluate(Context context, String function, List<Expression> arguments)
		throws EvaluationException;

	public Datatype getDatatype(Context context, String function, List<Expression> arguments)
		throws MetadataException;

}
