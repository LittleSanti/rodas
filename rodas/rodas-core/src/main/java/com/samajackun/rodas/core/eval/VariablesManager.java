package com.samajackun.rodas.core.eval;

public interface VariablesManager
{
	public Object getGlobalVariable(String name)
		throws VariableNotFoundException;

	public void setGlobalVariable(String name, Object value);

	public void removeGlobalVariable(String name);

	public Object getLocalVariable(String name)
		throws VariableNotFoundException;

	public void setLocalVariable(String name, Object value);

	public void removeLocalVariable(String name);

	public void pushLocalContext();

	public void popLocalContext();
}
