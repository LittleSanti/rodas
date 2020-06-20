package com.samajackun.rodas.core.eval;

import java.util.ArrayDeque;
import java.util.Deque;

public abstract class AbstractVariablesManager implements VariablesManager
{
	private final VariablesContext globalVariables;

	private final Deque<VariablesContext> localVariables=new ArrayDeque<>();

	protected AbstractVariablesManager(VariablesContext globalVariables)
	{
		super();
		this.globalVariables=globalVariables;
	}

	@Override
	public Object getGlobalVariable(Name name)
		throws VariableNotFoundException
	{
		return getGlobalVariablesContext().get(name);
	}

	@Override
	public void setGlobalVariable(Name name, Object value)
	{
		getGlobalVariablesContext().set(name, value);
	}

	@Override
	public void removeGlobalVariable(Name name)
	{
		getGlobalVariablesContext().remove(name);
	}

	private VariablesContext currentLocalVariables()
	{
		return this.localVariables.isEmpty()
			? getGlobalVariablesContext()
			: this.localVariables.peek();
	}

	@Override
	public Object getLocalVariable(Name name)
		throws VariableNotFoundException
	{
		return currentLocalVariables().get(name);
	}

	@Override
	public Object getNearestVariable(Name name)
		throws VariableNotFoundException
	{
		Object value;
		VariablesContext variablesContext=this.localVariables.peek();
		if (variablesContext == null)
		{
			variablesContext=getGlobalVariablesContext();
		}
		else if (!variablesContext.contains(name))
		{
			// // Vamos a intentar cargar sólo el prefijo:
			// Name prefixName=Name.instanceOf(name.getPrefix());
			// if (variablesContext.contains(name))
			// {
			// variablesContext.get(name);
			// }
			// else
			{
				variablesContext=getGlobalVariablesContext();
			}
		}
		if (variablesContext.contains(name))
		{
			value=variablesContext.get(name);
		}
		else
		{
			value=getValueForVariableNotFound(name);
		}
		return value;
	}

	@Override
	public void setNearestVariable(Name name, Object value)
	{
		VariablesContext variablesContext=this.localVariables.peek();
		if (variablesContext != null && variablesContext.contains(name))
		{
			variablesContext.set(name, value);
		}
		else
		{
			// Por defecto, la consideraremos global:
			getGlobalVariablesContext().set(name, value);
		}
	}

	@Override
	public void setLocalVariable(Name name, Object value)
	{
		currentLocalVariables().set(name, value);
	}

	@Override
	public void removeLocalVariable(Name name)
	{
		currentLocalVariables().remove(name);
	}

	@Override
	public void pushLocalContext(VariablesContext newContext)
	{
		this.localVariables.push(newContext);
	}

	@Override
	public VariablesContext popLocalContext()
	{
		return this.localVariables.pop();
	}
	//
	// @Override
	// public VariablesContext getVariablesContext(Name name)
	// throws VariableNotFoundException
	// {
	// VariablesContext variablesContext=this.localVariables.peek();
	// if (variablesContext == null || !variablesContext.contains(name))
	// {
	// variablesContext=getGlobalVariablesContext();
	// if (variablesContext == null || !variablesContext.contains(name))
	// {
	// variablesContext=getVariablesContextForVariableNotFound(name);
	// }
	// }
	// return variablesContext;
	// }

	protected abstract Object getValueForVariableNotFound(Name name)
		throws VariableNotFoundException;

	protected VariablesContext getVariablesContextForVariableNotFound(Name name)
	{
		return getGlobalVariablesContext();
	}

	public VariablesContext getGlobalVariablesContext()
	{
		return this.globalVariables;
	}

	// @Override
	// public VariablesContext getLocalVariablesContext()
	// {
	// return this.localVariables.peek();
	// }

	@Override
	public final VariablesContext peekLocalContext()
	{
		return currentLocalVariables();
	}
}
