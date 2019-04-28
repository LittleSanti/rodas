package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.Name;

public class IdentifierExpression implements Expression, Prefixed
{
	private final Name name;

	public IdentifierExpression(String prefix, String identifier)
	{
		super();
		this.name=Name.instanceOf(prefix, identifier);
	}

	public IdentifierExpression(String identifier)
	{
		this(null, identifier);
	}

	@Override
	public String getPrefix()
	{
		return this.name.getPrefix();
	}

	public String getIdentifier()
	{
		return this.name.getBase().asString();
	}

	@Override
	public String toCode()
	{
		return this.name.asString();
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
	public Name getName()
	{
		return this.name;
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
		return evaluatorFactory.getBaseEvaluator().getDatatype(context, this);
	}
}
