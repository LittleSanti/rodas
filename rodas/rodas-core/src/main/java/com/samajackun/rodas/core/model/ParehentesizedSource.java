package com.samajackun.rodas.core.model;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ParehentesizedSource implements Source
{
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
	public boolean hasColumn(String column, Provider provider)
		throws ProviderException
	{
		return this.source.hasColumn(column, provider);
	}

	@Override
	public Cursor execute(Engine engine, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return this.source.execute(engine, provider, context);
	}

	@Override
	public List<String> getColumnNames(Provider provider)
		throws ProviderException
	{
		return this.source.getColumnNames(provider);
	}

	@Override
	public String getAlias()
	{
		return this.source.getAlias();
	}

	@Override
	public ColumnMetadata getColumnMetadata(int column, Provider provider, Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException,
		ProviderException
	{
		return this.source.getColumnMetadata(column, provider, context, evaluatorFactory);
	}
}
