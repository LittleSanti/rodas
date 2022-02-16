package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.execution.CachedCursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.OrderClause;
import com.samajackun.rodas.core.model.RowData;

final class ComparatorUtils
{
	public static Comparator<RowData> createComparator(Context context, RowDataContext rowDataContext, EvaluatorFactory evaluatorFactory, List<OrderClause> orderClauses)
	{
		return new RowDataComparator(context, rowDataContext, evaluatorFactory, orderClauses);
	}

	public static List<Integer> index(CachedCursor src, Context context, RowDataContext rowDataContext, EvaluatorFactory evaluatorFactory, List<OrderClause> orderClauses)
		throws CursorException
	{
		Comparator<RowData> comparator=ComparatorUtils.createComparator(context, rowDataContext, evaluatorFactory, orderClauses);
		SortedMap<RowData, Integer> map=new TreeMap<>(comparator);
		for (int n=0; n < src.size(); n++)
		{
			RowData row=src.getRowData(n);
			map.put(row, n);
		}
		List<Integer> list=new ArrayList<>(src.size());
		list.addAll(map.values());
		return list;
	}

	private static class RowDataComparator implements Comparator<RowData>
	{
		private final RowDataContext rowDataContext;

		private Comparator<RowData> innerComparator;

		public RowDataComparator(Context context, RowDataContext rowDataContext, EvaluatorFactory evaluatorFactory, List<OrderClause> orderClauses)
		{
			super();
			this.rowDataContext=rowDataContext;
			Comparator<RowData> comparator=null;
			for (OrderClause clause : orderClauses)
			{
				Expression exp=clause.getExpression();
				@SuppressWarnings({
					"unchecked",
					"rawtypes"
				})
				Comparator<RowData> comparatorStep=Comparator.comparing(r -> {
					Object value;
					try
					{
						this.rowDataContext.setSrc(r);
						value=exp.evaluate(context, evaluatorFactory);
						// System.out.println("Del rowdata " + r + " he extraído el valor " + value);
					}
					catch (EvaluationException e)
					{
						// FIXME
						e.printStackTrace();
						value="???ERROR???";
					}
					return (Comparable)value;
				});
				if (!clause.isAscending())
				{
					comparatorStep=comparatorStep.reversed();
				}
				comparator=(comparator == null)
					? comparatorStep
					: comparator.thenComparing(comparatorStep);
			}
			// Este último comparator es para evitar que se produzcan duplicados en el índice:
			comparator=comparator.thenComparing(Comparator.comparing(r -> r.position()));
			this.innerComparator=comparator;
		}

		@Override
		public int compare(RowData o1, RowData o2)
		{
			return this.innerComparator.compare(o1, o2);
		}

	}

}
