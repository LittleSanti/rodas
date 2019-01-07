package com.samajackun.rodas.core.model;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

// TODO Falta procesar el alias.
public class AliasedSource implements Source
{
	private final Source source;

	private final String alias;

	public AliasedSource(Source source, String alias)
	{
		super();
		this.alias=alias;
		this.source=source;
	}

	public AliasedSource(Source source)
	{
		this(source, null);
	}

	@Override
	public String getAlias()
	{
		return this.alias;
	}

	public Source getSource()
	{
		return this.source;
	}

	@Override
	public String toCode()
	{
		String code=this.source.toCode();
		if (this.alias != null)
		{
			code+=" AS " + this.alias;
		}
		return code;
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
	public ColumnMetadata getColumnMetadata(int column, Provider provider, Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException,
		ProviderException
	{
		return this.source.getColumnMetadata(column, provider, context, evaluatorFactory);
	}
}
