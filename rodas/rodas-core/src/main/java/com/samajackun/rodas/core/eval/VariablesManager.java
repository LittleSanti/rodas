package com.samajackun.rodas.core.eval;

public interface VariablesManager
{
	public Object getGlobalVariable(Name name)
		throws VariableNotFoundException;

	public void setGlobalVariable(Name name, Object value);

	public void removeGlobalVariable(Name name);

	public Object getLocalVariable(Name name)
		throws VariableNotFoundException;

	public void setLocalVariable(Name name, Object value);

	public void removeLocalVariable(Name name);

	public Object getNearestVariable(Name name)
		throws VariableNotFoundException;

	public void setNearestVariable(Name name, Object value);

	public void pushLocalContext(VariablesContext newContext);

	public VariablesContext peekLocalContext();

	public VariablesContext popLocalContext();

	// public VariablesContext getVariablesContext(Name name)
	// throws VariableNotFoundException;
	//
	// public VariablesContext getLocalVariablesContext();
}
