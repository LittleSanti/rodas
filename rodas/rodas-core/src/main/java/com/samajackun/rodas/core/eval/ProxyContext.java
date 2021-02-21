package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.Provider;

public class ProxyContext implements Context
{

	private final Context src;

	private final VariablesManager variablesManager;

	private final EvaluatorFactory evaluatorFactory;

	public ProxyContext(Context src, VariablesContext newVariablesContext, EvaluatorFactory evaluatorFactory)
	{
		super();
		this.src=src;
		this.variablesManager=new ProxyVariablesManager(src.getVariablesManager(), newVariablesContext);
		this.evaluatorFactory=evaluatorFactory;
	}

	@Override
	public Provider getProvider()
	{
		return this.src.getProvider();
	}

	@Override
	public VariablesManager getVariablesManager()
	{
		return this.variablesManager;
	}

	@Override
	public Runtime getRuntime()
	{
		return this.src.getRuntime();
	}

	@Override
	public Object evaluate(Expression expression, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		// TODO
		return null;
	}

	@Override
	public EvaluatorFactory getEvaluatorFactory()
	{
		return this.evaluatorFactory;
	}

}
