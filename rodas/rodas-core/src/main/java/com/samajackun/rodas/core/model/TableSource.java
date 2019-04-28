package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

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
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return engine.execute(this, context);
		// return new Cursor(provider.getColumnNamesFromTable(this.table), provider.getTableData(this.table));
	}

	// @Override
	// public boolean hasColumn(String column)
	// throws ProviderException
	// {
	// return getProvider().getColumnMapFromTable(this.table).containsKey(column);
	// }
	//
	// @Override
	// public List<String> getColumnNames()
	// throws ProviderException
	// {
	// Map<String, Integer> map=getProvider().getColumnMapFromTable(this.table);
	// List<String> columnNames=new ArrayList<>(map.size());
	// for (int i=0; i < map.size(); i++)
	// {
	// columnNames.add(null);
	// }
	// map.forEach((k, v) -> columnNames.set(v, k));
	// return columnNames;
	// }
	//
	// @Override
	// public String getAlias()
	// {
	// return this.table;
	// }
	//
	// @Override
	// public ColumnMetadata getColumnMetadata(int column, Context context, EvaluatorFactory evaluatorFactory)
	// throws MetadataException,
	// ProviderException
	// {
	// return getProvider().getColumnsMetadataFromTable(this.table).getColumnMetadata(column);
	// }
	//
	// @Override
	// public Map<String, Integer> getColumnNamesMap()
	// throws ProviderException
	// {
	// return getProvider().getColumnMapFromTable(this.table);
	// }
}
