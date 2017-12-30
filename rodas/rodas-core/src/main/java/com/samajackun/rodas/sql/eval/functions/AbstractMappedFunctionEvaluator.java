package com.samajackun.rodas.sql.eval.functions;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.eval.AbstractEvaluator;
import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.eval.FunctionEvaluator;
import com.samajackun.rodas.sql.model.Expression;

public abstract class AbstractMappedFunctionEvaluator extends AbstractEvaluator implements FunctionEvaluator
{
	private final Map<String, Function> map;

	public AbstractMappedFunctionEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
		this.map=createMap(evaluatorFactory);
	}

	protected abstract Map<String, Function> createMap(EvaluatorFactory evaluatorFactory);

	@Override
	public Object evaluate(Context context, String functionName, List<Expression> arguments)
		throws EvaluationException
	{
		Function function=this.map.get(functionName);
		if (function == null)
		{
			throw new UnsuportedFunctionException(functionName);
		}
		return function.evaluate(context, arguments);
	}
}
