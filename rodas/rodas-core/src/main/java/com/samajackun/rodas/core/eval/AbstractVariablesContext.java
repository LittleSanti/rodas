package com.samajackun.rodas.core.eval;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractVariablesContext implements VariablesContext
{
	private final Map<String, Object> map=new HashMap<>();

	public AbstractVariablesContext()
	{
		super();
	}

	@Override
	public void set(String name, Object value)
	{
		this.map.put(name, value);
	}

	@Override
	public void remove(String name)
	{
		this.map.remove(name);
	}

	protected Map<String, Object> getMap()
	{
		return this.map;
	}
}