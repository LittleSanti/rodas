package com.samajackun.rodas.core.model;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ParehentesizedExpression implements Expression
{
	private static final long serialVersionUID=6083529862980489998L;

	private final Expression expression;

	public ParehentesizedExpression(Expression expression)
	{
		super();
		this.expression=expression;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

	@Override
	public String toCode()
	{
		return "(" + this.expression.toCode() + ")";
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.expression.evaluate(context, evaluatorFactory);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.expression.reduce(evaluatorFactory);
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return this.expression.getDatatype(context, evaluatorFactory);
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return Collections.singletonList(this.expression);
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.expression == null)
			? 0
			: this.expression.hashCode());
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
		if (!(obj instanceof Expression))
		{
			return false;
		}
		Expression other=(Expression)obj;
		return this.expression.equals(other);
	}

}
