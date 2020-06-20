package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

// TODO Falta procesar el alias.
public class AliasedSource implements Source
{
	private static final long serialVersionUID=250852036138699181L;

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
			code+=" AS \"" + this.alias + "\"";
		}
		return code;
	}

	@Override
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return this.source.execute(engine, context);
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
