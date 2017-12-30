package com.samajackun.rodas.sql.model;

import java.util.Set;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.eval.NameNotBoundException;

public class IdentifierExecutor extends AbstractExecutor
{
	private final Context subContext;

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
		this.subContext=(alias != null
			? context.getSubcontext(alias)
			: context.findIdentifier(identifier));
		this.index=this.subContext.getColumn(identifier);
	}

	@Override
	public Object evaluate()
		throws EvaluationException
	{
		return this.subContext.getValue(this.index);
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
