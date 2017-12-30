package com.samajackun.rodas.sql.model;

import java.util.Collections;
import java.util.Set;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class ConstantExecutor extends AbstractExecutor
{
	private final Object value;

	public ConstantExecutor(Context context, EvaluatorFactory evaluatorFactory, Object value)
	{
		super(context, evaluatorFactory);
		this.value=value;
	}

	@Override
	public Object evaluate()
		throws EvaluationException
	{
		return this.value;
	}

	@Override
	public Set<Source> browseAllReferencedSources()
	{
		return Collections.emptySet();
	}
}
