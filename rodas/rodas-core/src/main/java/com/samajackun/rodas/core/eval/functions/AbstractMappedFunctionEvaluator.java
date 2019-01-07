package com.samajackun.rodas.core.eval.functions;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.FunctionEvaluator;
import com.samajackun.rodas.core.model.Expression;

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
