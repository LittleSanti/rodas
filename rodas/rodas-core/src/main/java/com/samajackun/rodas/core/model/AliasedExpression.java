package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class AliasedExpression implements Codeable
{
	private final Expression expression;

	private final String alias;

	public AliasedExpression(Expression expression, String alias)
	{
		super();
		this.expression=expression;
		this.alias=alias;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

	public String getAlias()
	{
		return this.alias == null
			? this.expression.getName()
			: this.alias;
	}

	@Override
	public String toCode()
	{
		String code=this.expression.toCode();
		if (this.alias != null)
		{
			code+=" AS " + this.alias;
		}

		return code;
	}

	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.getExpression().evaluate(context, evaluatorFactory);
	}
}
