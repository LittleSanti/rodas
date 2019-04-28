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
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.rodas.core.model.RowData;

public class GroupedCursor implements Cursor
{
	private final Cursor src;

	public GroupedCursor(Cursor cursor, Context context, EvaluatorFactory evaluatorFactory, List<Expression> groupExpressions, Map<String, AliasedExpression> selectExpressionMap) throws CursorException
	{
		try
		{
			src=group(cursor, context, evaluatorFactory, groupExpressions, selectExpressionMap);
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
		List<ColumnMetadata> metadata=createMetadata(context, evaluatorFactory, selectExpressionMap);
		MemoryCachedCursor groupedCursor=new MemoryCachedCursor(metadata);
		RowDataVariablesContext rowDataVariablesContext=new RowDataVariablesContext(context.getVariablesManager().peekLocalContext(), cursor.getColumnMap());
		Comparator<RowData> comparator=createComparator(context, evaluatorFactory, groupExpressions, rowDataVariablesContext);
		SortedMap<RowData, Collection<RowData>> map=new TreeMap<>(comparator);
		context.getVariablesManager().pushLocalContext(rowDataVariablesContext);
		while (cursor.hasNext())
		{
			cursor.next();
			RowData row=cursor.getCachedRowData();
			// FIXME Este add(row) es sólo para pruebas: lo que hay que hacer aquí es alimentar la FunctionContext correspondiente en cada columna:
			System.out.printf("Row=%s\n", row);
			map.computeIfAbsent(row, r -> new ArrayList<>()).add(row);
		}
		context.getVariablesManager().popLocalContext();
		map.keySet().forEach(row -> groupedCursor.addRow(row));
		map.keySet().forEach(row -> System.out.printf("grouped row=%s\n", row));
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
					rowDataVariablesContext.setRowData(r);
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

	private List<ColumnMetadata> createMetadata(Context context, EvaluatorFactory evaluatorFactory, Map<String, AliasedExpression> selectExpressionMap)
		throws MetadataException
	{
		List<ColumnMetadata> metadata=new ArrayList<>();
		for (Map.Entry<String, AliasedExpression> entry : selectExpressionMap.entrySet())
		{
			ColumnMetadata columnMetadata=new ColumnMetadata(entry.getKey(), entry.getValue().getExpression().getDatatype(context, evaluatorFactory), false);
			metadata.add(columnMetadata);
		}
		return metadata;
	}

	@Override
	public void close()
		throws CursorException
	{
		src.close();
	}

	@Override
	public void next()
		throws CursorException
	{
		src.next();
	}

	@Override
	public boolean hasNext()
		throws CursorException
	{
		return src.hasNext();
	}

	@Override
	public RowData getRowData()
		throws CursorException
	{
		return src.getRowData();
	}

	@Override
	public Map<String, Integer> getColumnMap()
		throws CursorException
	{
		return src.getColumnMap();
	}

	@Override
	public List<ColumnMetadata> getMetadata()
		throws CursorException
	{
		return src.getMetadata();
	}

	@Override
	public void reset()
		throws CursorException
	{
		src.reset();
	}

	@Override
	public int getNumberOfColumns()
	{
		return src.getNumberOfColumns();
	}

	@Override
	public boolean isCached()
	{
		return src.isCached();
	}

	@Override
	public CachedCursor toCachedCursor()
		throws CursorException
	{
		return src.toCachedCursor();
	}
}