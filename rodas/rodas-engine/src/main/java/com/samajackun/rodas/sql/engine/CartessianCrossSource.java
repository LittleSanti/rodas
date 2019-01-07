package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Cursor;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.Source;

public class CartessianCrossSource implements Source
{
	private final List<Source> sources=new ArrayList<>();

	// private enum IterationAction {
	// KEEP, ITERATE, RESET
	// };
	//
	@Override
	public String toCode()
	{
		String s="";
		for (Source source : this.sources)
		{
			if (s.length() > 0)
			{

				s+=",";
			}
			s+=source.toCode();
		}
		return s;
	}

	@Override
	public boolean hasColumn(String column, Provider provider)
		throws ProviderException
	{
		boolean x=false;
		for (Source source : this.sources)
		{
			x=source.hasColumn(column, provider);
			if (x)
			{
				break;
			}
		}
		return x;
	}

	@Override
	public List<String> getColumnNames(Provider provider)
		throws ProviderException
	{
		int totalSize=0;
		List<List<String>> allColumnNames=new ArrayList<>(this.sources.size());
		for (Source source : this.sources)
		{
			List<String> c=source.getColumnNames(provider);
			allColumnNames.add(c);
			totalSize+=c.size();
		}
		List<String> columnNames=new ArrayList<>(totalSize);
		allColumnNames.stream().forEach(s -> columnNames.addAll(s));
		return columnNames;
	}

	@Override
	public Cursor execute(Engine engine, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColumnMetadata getColumnMetadata(int column, Provider provider, Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException,
		ProviderException
	{
		int firstColumn=0;
		int lastColumn=0;
		ColumnMetadata columnMetadata=null;
		for (Source source : this.sources)
		{
			List<String> c=source.getColumnNames(provider);
			lastColumn+=c.size();
			if (column < firstColumn)
			{
				columnMetadata=source.getColumnMetadata(column - firstColumn, provider, context, evaluatorFactory);
				break;
			}
			firstColumn=lastColumn;
		}
		if (columnMetadata == null)
		{
			throw new MetadataException("Column index if too high");
		}
		return columnMetadata;
	}
}
