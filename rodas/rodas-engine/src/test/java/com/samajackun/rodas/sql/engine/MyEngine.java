package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.DefaultCursor;
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.CrossSource;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.OnJoinedSource;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableData;
import com.samajackun.rodas.core.model.TableSource;

public class MyEngine implements Engine
{
	private final EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();

	// @Override
	// public Cursor execute(SelectSentence source, Provider provider, Context context)
	// throws EngineException,
	// EvaluationException,
	// ProviderException
	// {
	// if (source.getSourceDeclarations().size() == 1)
	// {
	// if (source.getWhereExpression() == null || source.getWhereExpression() instanceof BooleanExpression)
	// {
	// BooleanExpression booleanExpression=(BooleanExpression)source.getWhereExpression();
	// List<Object[]> output=new ArrayList<>(100);
	// Cursor sourceCursor1=source.getSourceDeclarations().get(0).execute(this, provider, context);
	// Context context1=null;// TODO context.getSubcontext(source.getAlias());
	// while (sourceCursor1.hasNext())
	// {
	// sourceCursor1.next();
	// for (Map.Entry<String, Integer> entry : sourceCursor1.getColumnMap().entrySet())
	// {
	// // Object value=sourceCursor1.getRowData().get(entry.getValue());
	// // context.putSubcontext(entry.getKey(), context1);
	// }
	// if (booleanExpression == null || booleanExpression.evaluate(context1, this.evaluatorFactory))
	// {
	// Object[] row=new Object[sourceCursor1.getColumnMap().size()];
	// int i=0;
	// for (String c : source.getColumnNames(provider))
	// {
	// // Object value=sourceCursor1.getRowData().get(c);
	// AliasedExpression aliasedExpression=source.getSelectExpressionsMap().get(c);
	// Object value=aliasedExpression.evaluate(context1, this.evaluatorFactory);
	// // System.out.printf("*row.put(%s, %s)\r\n", c, value);
	// row[i++]=value;
	// }
	// output.add(row);
	// }
	// }
	// Map<String, Integer> columnMap=new HashMap<>((int)(1.7d * source.getColumnNames(provider).size()));
	// int i=0;
	// for (String c : source.getColumnNames(provider))
	// {
	// columnMap.put(c, i++);
	// }
	// TableData tableData=new MyTableData(output);
	// // List<ColumnMetadata> metadata=provider.getColumnsMetadataFromTable(source.getTable()).getListOfColumnMetadata();
	// Cursor cursor=new DefaultCursor(metadata, tableData);
	// return cursor;
	// }
	// else
	// {
	// throw new EngineException("El WHERE debería devolver una condición boolean");
	// }
	// }
	// else
	// {
	// throw new EngineException("Lo siento; de momento, sólo tolero un FROM");
	// }
	// }

	@Override
	public Cursor execute(SelectSentence selectSource, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		// Esta es la parte más compleja de todo Rodas:
		// Cómo ejecutar una SELECT una vez contamos con toda la sentencia parseada:
		// Y lo vamos a resolver mediante el paradigma de las COMPOSICIONES SUCESIVAS:

		// 1-Cursor inicial:
		Cursor cursor=createCursor(selectSource.getSource(), context);
		Map<String, Cursor> cursorMap=new HashMap<>();
		cursorMap.put("x", cursor);
		context.getVariablesManager().pushLocalContext(new CursorMapVariablesContext(context.getVariablesManager().peekLocalContext(), cursorMap));

		// 2-Filtrar:
		if (selectSource.getWhereExpression() != null)
		{
			cursor=new FilteredCursor(cursor, context, this.evaluatorFactory, selectSource.getWhereExpression());
		}

		// 3-Agrupar:
		if (!selectSource.getGroupExpressions().isEmpty())
		{
			cursor=new GroupedCursor(cursor, context, this.evaluatorFactory, selectSource.getGroupExpressions(), selectSource.getSelectExpressionsMap());
			// 4-Re-filtrar:
			if (selectSource.getHavingExpression() != null)
			{
				cursor=new FilteredCursor(cursor, context, this.evaluatorFactory, selectSource.getHavingExpression());
			}
		}

		// 5-Ordenar:
		if (!selectSource.getOrderClauses().isEmpty())
		{
			cursor=new OrderedCursor(cursor, context, this.evaluatorFactory, selectSource.getOrderClauses());
		}

		// 6-Seleccionar columnas:
		cursor=new SelectingCursor(cursor, context, this.evaluatorFactory, selectSource.getSelectExpressions());

		// FIXME Hay que determinar dónde meter el popLocalContext: ¿En Cursor.close()? ¿En OrderedCursor.close()? ¿En FilteredCursor.close()?
		// Aquí, en principio, no debería ir, pues entonces perdemos el CursorMapVariablesContext que hemos "pushado" al comienzo deste método.
		// context.getVariablesManager().popLocalContext();

		return cursor;
	}

	private Cursor createCursor(Source source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		// FIXME No estoy seguro de que este sea un buen patrón de diseño.
		Cursor cursor;
		if (source instanceof TableSource)
		{
			cursor=execute((TableSource)source, context);
		}
		else if (source instanceof SelectSentence)
		{
			cursor=execute((SelectSentence)source, context);
		}
		else if (source instanceof CrossSource)
		{
			cursor=execute((CrossSource)source, context);
		}
		else if (source instanceof OnJoinedSource)
		{
			cursor=execute((OnJoinedSource)source, context);
		}
		else
		{
			throw new IllegalArgumentException();
		}
		return cursor;
	}

	@Override
	public Cursor execute(OnJoinedSource source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Cursor cursorLeft=source.getLeftSource().execute(this, context);
		Cursor cursorRight=source.getRightSource().execute(this, context);
		return new JoinedCursor(cursorLeft, cursorRight, source.getOn(), this.evaluatorFactory, context);
	}

	@Override
	public Cursor execute(TableSource source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		TableData tableData=context.getProvider().getTableData(source.getTable());
		List<ColumnMetadata> metadata=context.getProvider().getColumnsMetadataFromTable(source.getTable()).getListOfColumnMetadata();
		Cursor cursor=new DefaultCursor(metadata, tableData);
		return cursor;
	}

	@Override
	public Cursor execute(CrossSource crossSource, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		List<Cursor> cursors=new ArrayList<>(crossSource.getSources().size());
		for (Source source : crossSource.getSources())
		{
			cursors.add(source.execute(this, context));
		}
		CrossCursor crossCursor=new CrossCursor(cursors);
		return crossCursor;
	}
}
