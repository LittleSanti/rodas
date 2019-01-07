package com.samajackun.rodas.core.model;

import java.util.Collections;
import java.util.Set;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

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
