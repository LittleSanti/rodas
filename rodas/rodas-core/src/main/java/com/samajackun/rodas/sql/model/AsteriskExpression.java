package com.samajackun.rodas.sql.model;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class AsteriskExpression implements Prefixed, Expression
{
	private final String prefix;

	private final String asterisk;

	public AsteriskExpression(String asterisk)
	{
		this(null, asterisk);
	}

	public AsteriskExpression(String prefix, String asterisk)
	{
		super();
		this.prefix=prefix;
		this.asterisk=asterisk;
	}

	@Override
	public String getPrefix()
	{
		return this.prefix;
	}

	public String getAsterisk()
	{
		return this.asterisk;
	}

	@Override
	public String toCode()
	{
		return (this.prefix != null
			? this.prefix + "."
			: "") + this.asterisk;
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this;
	}

	@Override
	public List<Expression> toPhysicalExpressions(Provider provider)
		throws ProviderException
	{
		return Collections.singletonList(this);
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		throw new MetadataException("Unable to perform on AsteriskExpression");
	}
}
