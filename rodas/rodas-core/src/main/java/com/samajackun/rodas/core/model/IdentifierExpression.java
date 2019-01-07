package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class IdentifierExpression implements Expression, Prefixed
{
	private final String prefix;

	private final String identifier;

	public IdentifierExpression(String prefix, String identifier)
	{
		super();
		this.prefix=prefix;
		this.identifier=identifier;
	}

	public IdentifierExpression(String identifier)
	{
		this(null, identifier);
	}

	@Override
	public String getPrefix()
	{
		return this.prefix;
	}

	public String getIdentifier()
	{
		return this.identifier;
	}

	@Override
	public String toCode()
	{
		return (this.prefix != null
			? this.prefix + "."
			: "") + this.identifier;
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
	public String getName()
	{
		return this.identifier;
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
