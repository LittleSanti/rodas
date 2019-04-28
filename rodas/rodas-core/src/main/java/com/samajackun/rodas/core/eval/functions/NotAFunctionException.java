package com.samajackun.rodas.core.eval.functions;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;

public class NotAFunctionException extends EvaluationException
{
	private static final long serialVersionUID=509717852158357079L;

	private final Expression wrongFunction;

	public NotAFunctionException(Expression wrongFunction)
	{
		// FIXME Hay que limitar la longitud del string devuelto por toCode, porque podr�a ser toch�ssimo
		super("Object is not a function: " + wrongFunction.toCode());
		this.wrongFunction=wrongFunction;
	}

	public Expression getWrongFunction()
	{
		return wrongFunction;
	}
}
