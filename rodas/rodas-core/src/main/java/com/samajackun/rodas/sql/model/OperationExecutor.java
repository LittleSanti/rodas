package com.samajackun.rodas.sql.model;

import java.util.Set;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class OperationExecutor extends AbstractExecutor
{
	private final Expression expression;

	public OperationExecutor(Context context, EvaluatorFactory evaluatorFactory, Expression expression)
	{
		super(context, evaluatorFactory);
		this.expression=expression;
	}

	@Override
	public Object evaluate()
		throws EvaluationException
	{
		return this.expression.evaluate(getContext(), getEvaluatorFactory());
	}

	@Override
	public Set<Source> browseAllReferencedSources()
	{
		// return Collections.singleton(this.alias != null
		// ? this.subContext.getContextForAlias(this.alias)
		// : this.subContext.lookup(this.identifier));
		// TODO
		return null;
	}
}
