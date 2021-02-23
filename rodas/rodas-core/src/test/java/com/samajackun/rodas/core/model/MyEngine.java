package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.TableCursor;

public class MyEngine implements Engine
{
	private final EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();

	@Override
	public Cursor execute(SelectSentence source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		List<ColumnMetaData> metadata=new ArrayList<>(source.getSelectExpressions().size());
		for (AliasedExpression expression : source.getSelectExpressions())
		{
			String name=expression.getAlias() != null
				? expression.getAlias()
				: expression.getExpression().getName().asString();
			Datatype datatype=expression.getExpression().getDatatype(context, this.evaluatorFactory);
			boolean nullable=true;
			ColumnMetaData columnMetadata=new ColumnMetaData(name, datatype, nullable);
			metadata.add(columnMetadata);
		}
		IterableTableData tableData=null;
		Cursor cursor=new TableCursor(metadata, tableData);
		return cursor;
	}

	@Override
	public Cursor execute(OnJoinedSource source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Cursor execute(TableSource source, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		TableData tableData=context.getProvider().getTableMetaData(source.getTable()).getTableData();
		List<ColumnMetaData> metadata=context.getProvider().getTableMetaData(source.getTable()).getListOfColumnMetadata();
		Cursor cursor=new TableCursor(metadata, tableData);
		return cursor;
	}

	@Override
	public Cursor execute(CrossSource crossSource, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		// Cursor cursor=new CrossCursor(crossSource.getSources());
		// return cursor;
		throw new UnsupportedOperationException();
	}
}
