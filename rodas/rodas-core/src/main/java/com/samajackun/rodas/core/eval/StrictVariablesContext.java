package com.samajackun.rodas.core.eval;

public class StrictVariablesContext extends AbstractVariablesContext
{
	public Object get(String name)
		throws VariableNotFoundException
	{
		Object value=getMap().get(name);
		if (value == null)
		{
			if (!getMap().containsKey(name))
			{
				throw new VariableNotFoundException(name);
			}
		}
		return value;
	}
}
