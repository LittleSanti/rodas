package com.samajackun.rodas.core.eval.functions;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.FunctionEvaluator;
import com.samajackun.rodas.core.model.Expression;

public abstract class AbstractFunctionEvaluator extends AbstractEvaluator implements FunctionEvaluator
{
	public AbstractFunctionEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	public Object evaluate(Context context, Expression functionExpression, List<Expression> arguments)
		throws EvaluationException
	{
		Object obj=resolveFunction(context, functionExpression);
		if (obj == null)
		{
			throw new FunctionNotFoundException(functionExpression.toCode());
		}
		else if (obj instanceof Function)
		{
			Function function=(Function)obj;
			List<Object> argumentValues=evaluateArguments(context, arguments);
			Object returnValue=function.call(context, argumentValues);
			return returnValue;
		}
		else
		{
			throw new NotAFunctionException(functionExpression);
		}
	}

	protected Object resolveFunction(Context context, Expression functionExpression)
		throws EvaluationException
	{
		return functionExpression.evaluate(context, getEvaluatorFactory());
	}

	protected List<Object> evaluateArguments(Context context, List<Expression> arguments)
		throws EvaluationException
	{
		List<Object> values=new ArrayList<>(arguments.size());
		for (Expression argument : arguments)
		{
			Object value=evaluateArgument(context, argument);
			values.add(value);
		}
		return values;
	}

	protected Object evaluateArgument(Context context, Expression argument)
		throws EvaluationException
	{
		return argument.evaluate(context, getEvaluatorFactory());
	}
}
