package com.samajackun.rodas.core.eval;

import java.util.function.Supplier;

public interface VariablesContext
{
	public boolean contains(Name name);

	public Object get(Name name)
		throws VariableNotFoundException;

	public void set(Name name, Object value);

	public Object setIfAbsent(Name name, Supplier<Object> supplier);

	public void remove(Name name);

	public void clear();
}