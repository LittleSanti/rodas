package com.samajackun.rodas.core.eval;

public class StrictVariablesManager extends AbstractVariablesManager
{
	public StrictVariablesManager(VariablesContext globalVariables)
	{
		super(globalVariables);
	}

	@Override
	protected Object getValueForVariableNotFound(Name name)
		throws VariableNotFoundException
	{
		throw new VariableNotFoundException(name);
	}
}
