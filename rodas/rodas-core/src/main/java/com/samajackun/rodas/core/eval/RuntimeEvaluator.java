package com.samajackun.rodas.core.eval;

import java.io.File;

public interface RuntimeEvaluator
{
	public File evaluateCurrentDirectory(Context context)
		throws EvaluationException;

}
