package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.UnsuportedDatatypeException;

public class FunctionExpression implements Expression
{
	private final String function;

	private final List<Expression> arguments=new ArrayList<Expression>();

	public FunctionExpression(String function)
	{
		super();
		this.function=function;
	}

	public String getFunction()
	{
		return this.function;
	}

	public List<Expression> getArguments()
	{
		return this.arguments;
	}

	@Override
	public String toCode()
	{
		String functionSerial=this.function;
		StringBuilder stb=new StringBuilder(2 + functionSerial.length() + 100 * this.arguments.size());
		stb.append(functionSerial).append('(');
		for (Iterator<Expression> iterator=this.arguments.iterator(); iterator.hasNext();)
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
		return evaluatorFactory.getFunctionEvaluator().evaluate(context, this.function, this.arguments);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		List<Expression> reducedArguments=new ArrayList<Expression>(this.arguments.size());
		// this.arguments.stream().forEach(arg -> reducedArguments.add(arg.reduce(evaluatorFactory)));
		for (Expression argument : this.arguments)
		{
			reducedArguments.add(argument.reduce(evaluatorFactory));
		}
		// reducedArguments.stream().forEach(arg -> allArgumentsAreConstants=allArgumentsAreConstants && (arg instanceof ConstantExpression));
		boolean allArgumentsAreConstants=true;
		for (Iterator<Expression> iterator=reducedArguments.iterator(); iterator.hasNext() && allArgumentsAreConstants;)
		{
			Expression argument=iterator.next();
			allArgumentsAreConstants=allArgumentsAreConstants && (argument instanceof ConstantExpression);
		}
		Expression reduced;
		if (allArgumentsAreConstants)
		{
			Object result=evaluatorFactory.getFunctionEvaluator().evaluate(null, this.getFunction(), reducedArguments);
			if (result == null)
			{
				reduced=new NullConstantExpression("null");
			}
			else if (result instanceof Number)
			{
				reduced=new NumericConstantExpression(result.toString(), (Number)result);
			}
			else if (result instanceof String)
			{
				reduced=new TextConstantExpression((String)result);
			}
			// else if (result instanceof Date)
			// {
			// reduced=new DateConstantExpression((String)result);
			// }
			else if (result instanceof Boolean)
			{
				reduced=new BooleanConstantExpression(result.toString(), (Boolean)result);
			}
			else
			{
				throw new UnsuportedDatatypeException(result.getClass());
			}
		}
		else
		{
			reduced=this;
		}
		return reduced;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory) throws MetadataException
	{
		return evaluatorFactory.getFunctionEvaluator().getDatatype(context, this.function, this.arguments);
	}
}
