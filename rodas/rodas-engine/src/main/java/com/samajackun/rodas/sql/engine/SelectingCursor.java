package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.rodas.core.model.RowData;

public class SelectingCursor extends AbstractComposedCursor
{
	private final List<ColumnMetaData> metadata;

	private final Map<String, Integer> columnMap;

	private final Context context;

	private final List<AliasedExpression> selectExpressions;

	private final EvaluatorFactory evaluatorFactory;

	private final RowData rowData;

	public SelectingCursor(Cursor src, Context context, EvaluatorFactory evaluatorFactory, List<AliasedExpression> selectExpressions)
		throws MetadataException
	{
		super(src);
		List<ColumnMetaData> metadata=new ArrayList<>(selectExpressions.size());
		Map<String, Integer> columnMap=new HashMap<>((int)(1.7d * selectExpressions.size()));
		int p=0;
		for (AliasedExpression expression : selectExpressions)
		{
			String alias=expression.getAlias();
			Datatype datatype=expression.getExpression().getDatatype(context, evaluatorFactory);
			boolean nullable=true;
			ColumnMetaData columnMetadata=new ColumnMetaData(alias, datatype, nullable);
			metadata.add(columnMetadata);
			columnMap.put(alias, p++);
		}
		this.metadata=metadata;
		this.columnMap=columnMap;
		this.context=context;
		this.evaluatorFactory=evaluatorFactory;
		this.selectExpressions=selectExpressions;
		this.rowData=new MyRowData();
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return this.columnMap;
	}

	@Override
	public List<ColumnMetaData> getMetadata()
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
		return this.rowData;
	}

	private class MyRowData implements RowData
	{
		@Override
		public Object get(int column)
		{
			try
			{
				return SelectingCursor.this.selectExpressions.get(column).evaluate(SelectingCursor.this.context, SelectingCursor.this.evaluatorFactory);
			}
			catch (EvaluationException e)
			{
				// FIXME
				throw new java.lang.RuntimeException("Exception without treatment", e);
			}
		}

		@Override
		public long position()
		{
			return getCurrentPosition();
		}
	}

}
