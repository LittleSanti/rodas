package com.samajackun.rodas.sql.eval;

import java.util.List;


import com.samajackun.rodas.sql.model.Datatype;
import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.model.MetadataException;

public interface FunctionEvaluator
{
	public Object evaluate(Context context, String function, List<Expression> arguments)
		throws EvaluationException;

	public Datatype getDatatype(Context context, String function, List<Expression> arguments)
		throws MetadataException;

}
