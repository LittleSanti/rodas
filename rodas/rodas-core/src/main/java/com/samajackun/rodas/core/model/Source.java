package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

public interface Source extends Codeable
{
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;

	default public String getAlias()
	{
		return null;
	}

	// public boolean hasColumn(String column)
	// throws ProviderException;
	//
	// public List<String> getColumnNames()
	// throws ProviderException;
	//
	// public ColumnMetadata getColumnMetadata(int column, Context context, EvaluatorFactory evaluatorFactory)
	// throws MetadataException,
	// ProviderException;
	//
	// public default Map<String, Integer> getColumnNamesMap()
	// throws ProviderException
	// {
	// List<String> names=getColumnNames();
	// Map<String, Integer> map=new HashMap<>((int)(1.7d * names.size()));
	// AtomicInteger index=new AtomicInteger(-1);
	// names.stream().forEachOrdered(s -> map.put(s, index.incrementAndGet()));
	// return map;
	// }
	//
}
