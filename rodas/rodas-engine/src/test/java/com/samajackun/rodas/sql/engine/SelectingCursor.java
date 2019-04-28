package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.rodas.core.model.RowData;

public class SelectingCursor extends AbstractComposedCursor
{
	private final List<ColumnMetadata> metadata;

	public SelectingCursor(Cursor src, Context context, EvaluatorFactory evaluatorFactory, List<AliasedExpression> selectExpressions)
		throws MetadataException
	{
		super(src);
		List<ColumnMetadata> metadata=new ArrayList<>(selectExpressions.size());
		for (AliasedExpression expression : selectExpressions)
		{
			String alias=expression.getAlias();
			Datatype datatype=expression.getExpression().getDatatype(context, evaluatorFactory);
			boolean nullable=true;
			ColumnMetadata columnMetadata=new ColumnMetadata(alias, datatype, nullable);
			metadata.add(columnMetadata);
		}
		this.metadata=metadata;
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		// TODO Falta mapear.
		return super.getColumnMap();
	}

	@Override
	public List<ColumnMetadata> getMetadata()
		throws CursorException
	{
		return this.metadata;
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.metadata.size();
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		// TODO Falta hacer aquí el mapeo por índices.
		return super.getRowData();
	}
}
