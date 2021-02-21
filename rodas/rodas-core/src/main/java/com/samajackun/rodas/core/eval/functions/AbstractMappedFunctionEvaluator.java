package com.samajackun.rodas.core.eval.functions;

import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;

public abstract class AbstractMappedFunctionEvaluator extends AbstractFunctionEvaluator
{
	private final Map<String, Function> map;

	public AbstractMappedFunctionEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
		this.map=createMap(evaluatorFactory);
	}

	protected abstract Map<String, Function> createMap(EvaluatorFactory evaluatorFactory);

	@Override
	protected Object resolveFunction(Context context, Expression functionExpression)
		throws EvaluationException
	{
		Object resolved;
		if (functionExpression instanceof IdentifierExpression)
		{
			String functionName=((IdentifierExpression)functionExpression).getIdentifier();
			resolved=this.map.get(functionName);
		}
		else
		{
			resolved=super.resolveFunction(context, functionExpression);
		}
		return resolved;
	}
}
