package com.samajackun.rodas.core.eval;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractVariablesContext implements VariablesContext
{
	private final Map<String, Object> map=new HashMap<>();

	public AbstractVariablesContext()
	{
		super();
	}

	@Override
	public void set(Name name, Object value)
	{
		map.put(name.asString(), value);
	}

	@Override
	public Object setIfAbsent(Name name, Supplier<Object> supplier)
	{
		// Sin cacheo:
		return supplier.get();
	}

	@Override
	public void remove(Name name)
	{
		map.remove(name.asString());
	}

	protected Map<String, Object> getMap()
	{
		return map;
	}

	@Override
	public boolean contains(Name name)
	{
		return getMap().containsKey(name.asString());
	}

	@Override
	public void clear()
	{
		map.clear();
	}
}