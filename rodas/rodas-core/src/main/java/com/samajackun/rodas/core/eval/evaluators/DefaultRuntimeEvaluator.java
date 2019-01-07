package com.samajackun.rodas.core.eval.evaluators;

import java.io.File;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.RuntimeEvaluator;

public class DefaultRuntimeEvaluator extends AbstractEvaluator implements RuntimeEvaluator
{
	public DefaultRuntimeEvaluator(DefaultEvaluatorFactory myEvaluatorFactory)
	{
		super(myEvaluatorFactory);
	}

	@Override
	public File evaluateCurrentDirectory(Context context)
		throws EvaluationException
	{
		return (File)context.getRuntime().getSystemObject("test.current.directory");
	}
}
