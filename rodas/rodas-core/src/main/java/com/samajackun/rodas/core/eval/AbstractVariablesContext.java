package com.samajackun.rodas.core.eval;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractVariablesContext implements VariablesContext
{
	private final Map<Name, Supplier<Object>> map=new HashMap<>();

	public AbstractVariablesContext()
	{
		super();
	}

	@Override
	public void set(Name name, Object value)
	{
		// throw new UnsupportedOperationException();
		this.map.put(name, () -> value);
	}

	@Override
	public Object setIfAbsent(Name name, Supplier<Object> supplier)
	{
		// Sin cacheo:
		Supplier<Object> supplier2=this.map.computeIfAbsent(name, x -> supplier);
		return supplier2 == null
			? null
			: supplier2.get();
	}

	@Override
	public void remove(Name name)
	{
		this.map.remove(name);
	}

	protected Map<Name, Supplier<Object>> getMap()
	{
		return this.map;
	}

	@Override
	public boolean contains(Name name)
	{
		return getMap().containsKey(name);
	}

	@Override
	public void clear()
	{
		this.map.clear();
	}
}