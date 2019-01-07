package com.samajackun.rodas.core.eval;

public class StrictVariablesManager extends AbstractVariablesManager
{
	@Override
	protected VariablesContext createVariablesContext()
	{
		return new StrictVariablesContext();
	}
}
