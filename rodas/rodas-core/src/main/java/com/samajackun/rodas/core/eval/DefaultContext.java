package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.RodasRuntimeException;

public class DefaultContext implements Context
{
	private VariablesManager variablesManager;

	private Runtime runtime;

	private Provider provider;

	private EvaluatorFactory evaluatorFactory;

	@Override
	public VariablesManager getVariablesManager()
	{
		return this.variablesManager;
	}

	public DefaultContext setVariablesManager(VariablesManager variablesManager)
	{
		this.variablesManager=variablesManager;
		return this;
	}

	@Override
	public Runtime getRuntime()
	{
		return this.runtime;
	}

	public DefaultContext setRuntime(Runtime runtime)
	{
		this.runtime=runtime;
		return this;
	}

	@Override
	public Provider getProvider()
	{
		return this.provider;
	}

	public DefaultContext setProvider(Provider provider)
	{
		this.provider=provider;
		return this;
	}

	@Override
	public Object evaluate(Expression expression, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		// return expression.evaluate(this, evaluatorFactory);
		VariablesContext variablesContext=this.variablesManager.peekLocalContext();
		// System.out.println("variablesContext.get(" + expression.getName() +
		// ")=" + variablesContext.get(expression.getName()));
		Name name=expression.getCodeAsName();
		Object value=variablesContext.setIfAbsent(name, () -> {
			try
			{
				return expression.evaluate(this, evaluatorFactory);
			}
			catch (EvaluationException e)
			{
				throw new RodasRuntimeException(e);
			}
		});
		return value;
	}

	public Context createSubcontext(VariablesContext newVariablesContext)
	{
		return new ProxyContext(this, newVariablesContext, this.evaluatorFactory);
	}

	@Override
	public EvaluatorFactory getEvaluatorFactory()
	{
		return this.evaluatorFactory;
	}

	public DefaultContext setEvaluatorFactory(EvaluatorFactory evaluatorFactory)
	{
		this.evaluatorFactory=evaluatorFactory;
		return this;
	}

}
