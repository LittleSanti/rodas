package com.samajackun.rodas.core.model;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.Name;

public class IdentifierExpression implements Expression, Prefixed
{
	private static final long serialVersionUID=8848781781238350775L;

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

	@Override
	public List<Expression> getSubExpressions()
	{
		return Collections.emptyList();
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.name == null)
			? 0
			: this.name.hashCode());
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
		IdentifierExpression other=(IdentifierExpression)obj;
		if (this.name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!this.name.equals(other.name))
		{
			return false;
		}
		return true;
	}

}
