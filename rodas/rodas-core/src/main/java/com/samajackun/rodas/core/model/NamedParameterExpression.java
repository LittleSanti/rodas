package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.Name;

public class NamedParameterExpression implements Expression
{
	private final Name name;

	public NamedParameterExpression(String parameter)
	{
		super();
		this.name=Name.instanceOf(parameter);
	}

	@Override
	public Name getName()
	{
		return this.name;
	}

	@Override
	public String toCode()
	{
		return ":" + this.name.asString();
	}

	@Override
	public String toString()
	{
		return toCode();
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getBaseEvaluator().evaluate(context, this);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.UNKNOWN;
	}
}
