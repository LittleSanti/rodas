package com.samajackun.rodas.core.eval;

public interface VariablesContext
{
	public boolean contains(String name);

	public Object get(String name)
		throws VariableNotFoundException;

	public void set(String name, Object value);

	public void remove(String name);

}