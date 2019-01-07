package com.samajackun.rodas.core.eval;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class AbstractVariablesManager implements VariablesManager
{
	private final VariablesContext globalVariables=createVariablesContext();

	private final Deque<VariablesContext> localVariables=new ArrayDeque<>();

	@Override
	public Object getGlobalVariable(String name)
		throws VariableNotFoundException
	{
		return this.globalVariables.get(name);
	}

	@Override
	public void setGlobalVariable(String name, Object value)
	{
		this.globalVariables.set(name, value);
	}

	@Override
	public void removeGlobalVariable(String name)
	{
		this.globalVariables.remove(name);
	}

	@Override
	public Object getLocalVariable(String name)
		throws VariableNotFoundException
	{
		return this.localVariables.peek().get(name);
	}

	@Override
	public void setLocalVariable(String name, Object value)
	{
		this.localVariables.peek().set(name, value);
	}

	@Override
	public void removeLocalVariable(String name)
	{
		this.localVariables.peek().remove(name);
	}

	@Override
	public void pushLocalContext()
	{
		this.localVariables.push(createVariablesContext());
	}

	@Override
	public void popLocalContext()
	{
		this.localVariables.pop();
	}

	protected abstract VariablesContext createVariablesContext();

}
