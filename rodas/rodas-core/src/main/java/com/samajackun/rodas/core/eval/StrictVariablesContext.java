package com.samajackun.rodas.core.eval;

import java.util.function.Supplier;

public class StrictVariablesContext extends AbstractVariablesContext
{
	@Override
	public Object get(Name name)
		throws VariableNotFoundException
	{
		Object value;
		Supplier<Object> supplier=getMap().get(name);
		if (supplier != null)
		{
			value=supplier.get();
			if (value == null)
			{
				if (!getMap().containsKey(name))
				{
					throw new VariableNotFoundException(name);
				}
			}
		}
		else
		{
			throw new VariableNotFoundException(name);
		}
		return value;
	}
}
