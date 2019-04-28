package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class FunctionCallExpression implements Expression
{
	private final Expression functionObject;

	private final List<Expression> arguments;

	public FunctionCallExpression(Expression functionObject, List<Expression> arguments)
	{
		super();
		this.functionObject=functionObject;
		this.arguments=arguments;
	}

	public Expression getFunctionObject()
	{
		return functionObject;
	}

	public List<Expression> getArguments()
	{
		return arguments;
	}

	@Override
	public String toCode()
	{
		String functionSerial=functionObject.toCode();
		StringBuilder stb=new StringBuilder(2 + functionSerial.length() + 100 * arguments.size());
		stb.append(functionSerial).append('(');
		for (Iterator<Expression> iterator=arguments.iterator(); iterator.hasNext();)
		{
			Expression argument=iterator.next();
			stb.append(argument.toCode());
			if (iterator.hasNext())
			{
				stb.append(',');
			}
		}
		stb.append(')');
		return stb.toString();
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getFunctionEvaluator().evaluate(context, functionObject, arguments);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reducedExpression;
		List<Expression> reducedArguments=new ArrayList<>(arguments.size());
		boolean allConstants=true;
		for (Iterator<Expression> iterator=arguments.iterator(); iterator.hasNext() && allConstants;)
		{
			Expression argument=iterator.next();
			Expression reducedArgument=argument.reduce(evaluatorFactory);
			if (reducedArgument instanceof ConstantExpression)
			{
				reducedArguments.add(reducedArgument);
			}
			else
			{
				allConstants=false;
			}
		}
		if (allConstants)
		{
			reducedExpression=new FunctionCallExpression(functionObject, reducedArguments);
		}
		else
		{
			reducedExpression=this;
		}
		return reducedExpression;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		// return this.functionObject.getDatatype(context, evaluatorFactory);
		return functionObject.getDatatype(context, evaluatorFactory);
	}
}
