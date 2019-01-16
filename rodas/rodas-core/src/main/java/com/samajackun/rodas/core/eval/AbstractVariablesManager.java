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

	private VariablesContext currentLocalVariables()
	{
		VariablesContext variablesContext=this.localVariables.peek();
		if (variablesContext == null)
		{
			throw new IllegalStateException("No local context!");
		}
		return variablesContext;
	}

	@Override
	public Object getLocalVariable(String name)
		throws VariableNotFoundException
	{
		return currentLocalVariables().get(name);
	}

	@Override
	public Object getNearestVariable(String name)
		throws VariableNotFoundException
	{
		Object value;
		VariablesContext variablesContext=this.localVariables.peek();
		if (variablesContext != null && variablesContext.contains(name))
		{
			value=variablesContext.get(name);
		}
		else
		{
			if (this.globalVariables.contains(name))
			{
				value=this.globalVariables.get(name);
			}
			else
			{
				value=getValueForVariableNotFound(name);
			}
		}
		return value;
	}

	@Override
	public void setLocalVariable(String name, Object value)
	{
		currentLocalVariables().set(name, value);
	}

	@Override
	public void removeLocalVariable(String name)
	{
		currentLocalVariables().remove(name);
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

	@Override
	public VariablesContext getVariablesContext(String name)
		throws VariableNotFoundException
	{
		VariablesContext variablesContext=this.localVariables.peek();
		if (variablesContext == null || !variablesContext.contains(name))
		{
			variablesContext=this.globalVariables;
			if (variablesContext == null || !variablesContext.contains(name))
			{
				variablesContext=getVariablesContextForVariableNotFound(name);
			}
		}
		return variablesContext;
	}

	protected abstract Object getValueForVariableNotFound(String name)
		throws VariableNotFoundException;

	protected abstract VariablesContext getVariablesContextForVariableNotFound(String name)
		throws VariableNotFoundException;

	protected VariablesContext getGlobalVariables()
	{
		return this.globalVariables;
	}
}
