package com.samajackun.rodas.sql.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class TableSource implements Source
{
	private final String table;

	public TableSource(String table)
	{
		super();
		this.table=table;
	}

	public String getTable()
	{
		return this.table;
	}

	@Override
	public String toCode()
	{
		return this.table;
	}

	@Override
	public boolean hasColumn(String column, Provider provider)
		throws ProviderException
	{
		return provider.getColumnMapFromTable(this.table).containsKey(column);
	}

	@Override
	public Cursor execute(Engine engine, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return engine.execute(this, provider, context);
		// return new Cursor(provider.getColumnNamesFromTable(this.table), provider.getTableData(this.table));
	}

	@Override
	public List<String> getColumnNames(Provider provider)
		throws ProviderException
	{
		Map<String, Integer> map=provider.getColumnMapFromTable(this.table);
		List<String> columnNames=new ArrayList<>(map.size());
		for (int i=0; i < map.size(); i++)
		{
			columnNames.add(null);
		}
		map.forEach((k, v) -> columnNames.set(v, k));
		return columnNames;
	}

	@Override
	public String getAlias()
	{
		return this.table;
	}

	@Override
	public ColumnMetadata getColumnMetadata(int column, Provider provider, Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException,
		ProviderException
	{
		return provider.getColumnsMetadataFromTable(this.table).getColumnMetadata(column);
	}

	@Override
	public Map<String, Integer> getColumnNamesMap(Provider provider)
		throws ProviderException
	{
		return provider.getColumnMapFromTable(this.table);
	}
}
