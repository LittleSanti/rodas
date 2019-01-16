package com.samajackun.rodas.core.eval;

public class StrictVariablesManager extends AbstractVariablesManager
{
	@Override
	protected VariablesContext createVariablesContext()
	{
		return new StrictVariablesContext();
	}

	@Override
	protected Object getValueForVariableNotFound(String name)
		throws VariableNotFoundException
	{
		throw new VariableNotFoundException(name);
	}

	@Override
	protected VariablesContext getVariablesContextForVariableNotFound(String name)
		throws VariableNotFoundException
	{
		throw new VariableNotFoundException(name);
	}
}
