package com.samajackun.rodas.core.model;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class AsteriskExpression implements Prefixed, Expression
{
	private static final long serialVersionUID=1633937011078663420L;

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
		result=prime * result + ((this.asterisk == null)
			? 0
			: this.asterisk.hashCode());
		result=prime * result + ((this.prefix == null)
			? 0
			: this.prefix.hashCode());
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
		AsteriskExpression other=(AsteriskExpression)obj;
		if (this.asterisk == null)
		{
			if (other.asterisk != null)
			{
				return false;
			}
		}
		else if (!this.asterisk.equals(other.asterisk))
		{
			return false;
		}
		if (this.prefix == null)
		{
			if (other.prefix != null)
			{
				return false;
			}
		}
		else if (!this.prefix.equals(other.prefix))
		{
			return false;
		}
		return true;
	}
}
