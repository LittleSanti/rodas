package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.TableCursor;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.CrossSource;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.OnJoinedSource;
import com.samajackun.rodas.core.model.OneRowSource;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableData;
import com.samajackun.rodas.core.model.TableMetaData;
import com.samajackun.rodas.core.model.TableSource;

public class SqlEngine implements Engine
{
	private final EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();

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
		Cursor cursor=createCursor(getNonNullSource(selectSource.getSource()), context);
		// Hay que crear aquí un nuevo VariablesContext y pasárselo a createCursor.
		// context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));

		// Map<String, Cursor> cursorMap=new HashMap<>();
		// cursorMap.put("x", cursor); // FIXME
		// context.getVariablesManager().pushLocalContext(new CursorMapVariablesContext(context.getVariablesManager().peekLocalContext(), cursorMap));
		// context.getVariablesManager().pushLocalContext(new CursorVariablesContext(context.getVariablesManager().peekLocalContext(), cursor));

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

		// PoppingVariablesContextCursor sirve para hacer el popLocalContext en Cursor.close().
		// No se debe hacer antes, pues entonces perdemos el CursorMapVariablesContext que hemos "pushado" al comienzo deste método.
		// cursor=new PoppingVariablesContextCursor(cursor, context);

		return cursor;
	}

	private Source getNonNullSource(Source source)
	{
		return source == null
			? OneRowSource.getInstance()
			: source;
	}

	private Cursor createCursor(Source source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Cursor cursor=source.execute(this, context);
		RowDataVariablesContext newVariablesContext=new RowDataVariablesContext(context.getVariablesManager().peekLocalContext(), cursor.getColumnMap());
		newVariablesContext.setCurrentCursor(cursor);
		context.getVariablesManager().pushLocalContext(newVariablesContext);
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
		TableMetaData metaData=context.getProvider().getTableMetaData(source.getTable());
		TableData tableData=metaData.getTableData();
		List<ColumnMetaData> metadata=metaData.getListOfColumnMetadata();
		Cursor cursor=new TableCursor(metadata, tableData);
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
