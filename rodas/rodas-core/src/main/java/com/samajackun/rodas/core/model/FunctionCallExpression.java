package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.Name;

public class FunctionCallExpression implements Expression
{
	private static final long serialVersionUID=-1420889264254733394L;

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
		return this.functionObject;
	}

	public List<Expression> getArguments()
	{
		return this.arguments;
	}

	@Override
	public String toCode()
	{
		String functionSerial=this.functionObject.toCode();
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
		return evaluatorFactory.getFunctionEvaluator().evaluate(context, this.functionObject, this.arguments);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reducedExpression;
		List<Expression> reducedArguments=new ArrayList<>(this.arguments.size());
		boolean allConstants=true;
		for (Iterator<Expression> iterator=this.arguments.iterator(); iterator.hasNext() && allConstants;)
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
			reducedExpression=new FunctionCallExpression(this.functionObject, reducedArguments);
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
		return this.functionObject.getDatatype(context, evaluatorFactory);
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return this.arguments;
	}

	// @Override
	// public Object feedAndEvaluate(Context context, EvaluatorFactory evaluatorFactory)
	// throws EvaluationException
	// {
	// List<Expression> newArguments;
	// if (context.getVariablesManager().getAccumulatorVariables().contains(this))
	// {
	// newArguments=new ArrayList<>(this.arguments);
	// Object oldValue=context.getVariablesManager().getAccumulatorVariables().get(this);
	// newArguments.add(new ObjectConstantExpression(oldValue));
	// }
	// else
	// {
	// newArguments=this.arguments;
	// }
	// Object newValue=evaluatorFactory.getFunctionEvaluator().evaluate(context, this.functionObject, newArguments);
	// context.getVariablesManager().getAccumulatorVariables().put(this, newValue);
	// }
	@Override
	public Name getName()
	{
		return this.functionObject.getCodeAsName();
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.arguments == null)
			? 0
			: this.arguments.hashCode());
		result=prime * result + ((this.functionObject == null)
			? 0
			: this.functionObject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		FunctionCallExpression other=(FunctionCallExpression)obj;
		if (this.functionObject == null)
		{
			if (other.functionObject != null)
			{
				return false;
			}
		}
		else if (!this.functionObject.equals(other.functionObject))
		{
			return false;
		}
		if (this.arguments == null)
		{
			if (other.arguments != null)
			{
				return false;
			}
		}
		Iterator<Expression> iterator1=this.arguments.iterator();
		Iterator<Expression> iterator2=other.arguments.iterator();
		boolean x=true;
		while (iterator1.hasNext() && iterator2.hasNext() && x)
		{
			Expression expression1=iterator1.next();
			Expression expression2=iterator2.next();
			x=expression1.equals(expression2);
		}
		if (x)
		{
			x=iterator1.hasNext() == iterator2.hasNext();
		}
		return x;
	}
}
