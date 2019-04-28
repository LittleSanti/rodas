package com.samajackun.rodas.core.eval;

public class StrictVariablesContext extends AbstractVariablesContext
{
	@Override
	public Object get(Name name)
		throws VariableNotFoundException
	{
		String nameAsString=name.asString();
		Object value=getMap().get(nameAsString);
		if (value == null)
		{
			if (!getMap().containsKey(nameAsString))
			{
				throw new VariableNotFoundException(name);
			}
		}
		return value;
	}
}
