package com.samajackun.rodas.sql.model;

import java.util.Set;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.eval.NameNotBoundException;

public class IdentifierExecutor extends AbstractExecutor
{
	private final int index;

	// private final String alias;
	//
	// private final String identifier;

	public IdentifierExecutor(Context context, EvaluatorFactory evaluatorFactory, String alias, String identifier)
		throws NameNotBoundException,
		NameNotBoundException
	{
		super(context, evaluatorFactory);
		// this.alias=alias;
		// this.identifier=identifier;
		this.index=getContext().getColumnIndexByName(identifier, (alias != null)
			? alias
			: identifier);
	}

	@Override
	public Object evaluate()
		throws EvaluationException
	{
		try
		{
			return getContext().getColumnByIndex(this.index);
		}
		catch (CursorException e)
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
