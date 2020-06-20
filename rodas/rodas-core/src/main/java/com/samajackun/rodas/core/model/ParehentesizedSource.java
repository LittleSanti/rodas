package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

public class ParehentesizedSource implements Source
{
	private static final long serialVersionUID=-4777536811994962855L;

	private final Source source;

	public ParehentesizedSource(Source source)
	{
		super();
		this.source=source;
	}

	public Source getSource()
	{
		return this.source;
	}

	@Override
	public String toCode()
	{
		return "(" + this.source.toCode() + ")";
	}

	@Override
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return this.source.execute(engine, context);
	}

	@Override
	public String getAlias()
	{
		return this.source.getAlias();
	}

	// @Override
	// public boolean hasColumn(String column)
	// throws ProviderException
	// {
	// return this.source.hasColumn(column);
	// }
	//
	// @Override
	// public List<String> getColumnNames()
	// throws ProviderException
	// {
	// return this.source.getColumnNames();
	// }
	//
	// @Override
	// public ColumnMetadata getColumnMetadata(int column, Context context, EvaluatorFactory evaluatorFactory)
	// throws MetadataException,
	// ProviderException
	// {
	// return this.source.getColumnMetadata(column, context, evaluatorFactory);
	// }
}
