package com.samajackun.rodas.core.model;

import java.util.Set;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.NameNotBoundException;
import com.samajackun.rodas.core.eval.VariableNotFoundException;

public class IdentifierExecutor extends AbstractExecutor
{
	// TODO private final int index;

	// private final String alias;
	//
	// private final String identifier;
	private final Name name;

	public IdentifierExecutor(Context context, EvaluatorFactory evaluatorFactory, String alias, String identifier)
		throws NameNotBoundException,
		NameNotBoundException
	{
		super(context, evaluatorFactory);
		// this.alias=alias;
		// this.identifier=identifier;
		// TODO this.index=getContext().getColumnIndexByName(identifier, (alias != null)
		// ? alias
		// : identifier);
		this.name=Name.instanceOf(alias, identifier);
	}

	@Override
	public Object evaluate()
		throws EvaluationException
	{
		try
		{
			return getContext().getVariablesManager().getNearestVariable(this.name);
			// return getContext().getColumnByIndex(this.index);
		}
		catch (VariableNotFoundException e)
		{
			throw new EvaluationException(e);
		}
	}

	@Override
	public Set<Source> browseAllReferencedSources()
	{
		// TODO
		return null;
		// return Collections.singleton(this.alias != null
		// ? this.subContext.getContextForAlias(this.alias)
		// : this.subContext.lookup(this.identifier));
	}
}
