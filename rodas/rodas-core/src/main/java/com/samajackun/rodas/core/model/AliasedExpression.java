package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class AliasedExpression implements Codeable
{
	private static final long serialVersionUID=-212472329399875852L;

	private final Expression expression;

	private String alias;

	private final String rawAlias;

	public AliasedExpression(Expression expression, String rawAlias)
	{
		super();
		this.expression=expression;
		this.rawAlias=rawAlias;
		this.alias=rawAlias;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

	public String getAlias()
	{
		return this.alias;
	}

	public void setAlias(String alias)
	{
		if (this.alias != null)
		{
			throw new IllegalStateException();
		}
		this.alias=alias;
	}

	@Override
	public String toCode()
	{
		String code=this.expression.toCode();
		if (this.rawAlias != null)
		{
			code+=" AS " + this.rawAlias;
		}

		return code;
	}

	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.getExpression().evaluate(context, evaluatorFactory);
	}

	public List<AliasedExpression> toPhysicalExpressions(Provider provider)
		throws ProviderException
	{
		List<Expression> expressions=this.expression.toPhysicalExpressions(provider);
		List<AliasedExpression> aliasedExpressions=new ArrayList<>(expressions.size());
		expressions.forEach(k -> aliasedExpressions.add(new AliasedExpression(k, this.alias)));
		return aliasedExpressions;
	}
}
