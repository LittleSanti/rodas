package com.samajackun.rodas.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public interface Source extends Codeable
{
	public boolean hasColumn(String column, Provider provider)
		throws ProviderException;

	public Cursor execute(Engine engine, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException;

	public List<String> getColumnNames(Provider provider)
		throws ProviderException;

	default public String getAlias()
	{
		return null;
	}

	public ColumnMetadata getColumnMetadata(int column, Provider provider, Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException,
		ProviderException;

	public default Map<String, Integer> getColumnNamesMap(Provider provider)
		throws ProviderException
	{
		List<String> names=getColumnNames(provider);
		Map<String, Integer> map=new HashMap<>((int)(1.7d * names.size()));
		AtomicInteger index=new AtomicInteger(-1);
		names.stream().forEachOrdered(s -> map.put(s, index.incrementAndGet()));
		return map;
	}

}
