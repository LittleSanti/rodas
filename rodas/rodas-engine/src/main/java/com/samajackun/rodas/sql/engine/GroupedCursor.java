package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.execution.CachedCursor;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.rodas.core.model.RowData;

public class GroupedCursor implements Cursor
{
	private final Cursor src;

	public GroupedCursor(Cursor cursor, Context context, EvaluatorFactory evaluatorFactory, List<Expression> groupExpressions, Map<String, AliasedExpression> selectExpressionMap)
		throws CursorException
	{
		try
		{
			this.src=group(cursor, context, evaluatorFactory, groupExpressions, selectExpressionMap);
		}
		catch (MetadataException e)
		{
			throw new CursorException(e);
		}
	}

	private Cursor group(Cursor cursor, Context context, EvaluatorFactory evaluatorFactory, List<Expression> groupExpressions, Map<String, AliasedExpression> selectExpressionMap)
		throws CursorException,
		MetadataException
	{
		List<ColumnMetaData> metadata=createMetadata(context, evaluatorFactory, selectExpressionMap);
		MemoryCachedCursor groupedCursor=new MemoryCachedCursor(metadata);
		RowDataVariablesContext rowDataVariablesContext=new RowDataVariablesContext(context.getVariablesManager().peekLocalContext(), cursor.getColumnMap());
		Comparator<RowData> comparator=createComparator(context, evaluatorFactory, groupExpressions, rowDataVariablesContext);
		SortedMap<RowData, Collection<RowData>> map=new TreeMap<>(comparator);
		context.getVariablesManager().pushLocalContext(rowDataVariablesContext);
		while (cursor.hasNext())
		{
			cursor.next();
			RowData row=cursor.getCachedRowData();
			// FIXME Este add(row) es sólo para pruebas: lo que hay que hacer aquí es alimentar el FunctionAggregator correspondiente en cada columna:
			System.out.printf("Row=%s\n", row);
			map.computeIfAbsent(row, r -> new ArrayList<>()).add(row);
		}
		context.getVariablesManager().popLocalContext();
		map.keySet().forEach(row -> groupedCursor.addRow(row));
		// map.keySet().forEach(row -> System.out.printf("grouped row=%s\n", row));
		return new MemoryCursor(groupedCursor);
	}

	private Comparator<RowData> createComparator(Context context, EvaluatorFactory evaluatorFactory, List<Expression> groupExpressions, RowDataVariablesContext rowDataVariablesContext)
		throws CursorException
	{
		Comparator<RowData> comparator=null;
		for (Expression exp : groupExpressions)
		{
			@SuppressWarnings({
				"unchecked",
				"rawtypes"
			})
			Comparator<RowData> comparatorStep=Comparator.comparing(r -> {
				Object value;
				try
				{
					// rowDataVariablesContext.setRowData(r);
					value=exp.evaluate(context, evaluatorFactory);
				}
				catch (EvaluationException e)
				{
					// FIXME
					value="???ERROR???";
				}
				return (Comparable)value;
			});
			comparator=(comparator == null)
				? comparatorStep
				: comparator.thenComparing(comparatorStep);
		}
		return comparator;
	}

	private List<ColumnMetaData> createMetadata(Context context, EvaluatorFactory evaluatorFactory, Map<String, AliasedExpression> selectExpressionMap)
		throws MetadataException
	{
		List<ColumnMetaData> metadata=new ArrayList<>();
		for (Map.Entry<String, AliasedExpression> entry : selectExpressionMap.entrySet())
		{
			ColumnMetaData columnMetadata=new ColumnMetaData(entry.getKey(), entry.getValue().getExpression().getDatatype(context, evaluatorFactory), false);
			metadata.add(columnMetadata);
		}
		return metadata;
	}

	@Override
	public void close()
		throws CursorException
	{
		this.src.close();
	}

	@Override
	public void next()
		throws CursorException
	{
		this.src.next();
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		return this.src.hasNext();
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		return this.src.getRowData();
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return this.src.getColumnMap();
	}

	@Override
	public List<ColumnMetaData> getMetadata()
		throws CursorException
	{
		return this.src.getMetadata();
	}

	@Override
	public void reset()
		throws CursorException
	{
		this.src.reset();
	}

	@Override
	public int getNumberOfColumns()
	{
		return this.src.getNumberOfColumns();
	}

	@Override
	public boolean isCached()
	{
		return this.src.isCached();
	}

	@Override
	public CachedCursor toCachedCursor()
		throws CursorException
	{
		return this.src.toCachedCursor();
	}
}
