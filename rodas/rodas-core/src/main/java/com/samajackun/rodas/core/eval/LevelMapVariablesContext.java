package com.samajackun.rodas.core.eval;

import java.util.function.Supplier;

import com.samajackun.rodas.core.util.LevelMap;

public class LevelMapVariablesContext implements VariablesContext
{
	private final LevelMap<Name, Object> levelMap=LevelMap.createRoot();

	@Override
	public boolean contains(Name name)
	{
		return this.levelMap.containsKey(name);
	}

	@Override
	public Object get(Name name)
		throws VariableNotFoundException
	{
		return this.levelMap.getAtCurrentOrPreviousLevel(name);
	}

	@Override
	public void set(Name name, Object value)
	{
		this.levelMap.put(name, value);
	}

	@Override
	public Object setIfAbsent(Name name, Supplier<Object> supplier)
	{
		return this.levelMap.putIfAbsent(name, supplier);
	}

	@Override
	public void remove(Name name)
	{
		this.levelMap.remove(name);
	}

	@Override
	public void clear()
	{
		this.levelMap.clear();
	}
}
