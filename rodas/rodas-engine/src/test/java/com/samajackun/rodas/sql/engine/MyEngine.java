package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.eval.MyEvaluatorFactory;
import com.samajackun.rodas.sql.model.AliasedExpression;
import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.Datatype;
import com.samajackun.rodas.sql.model.Engine;
import com.samajackun.rodas.sql.model.EngineException;
import com.samajackun.rodas.sql.model.IterableTableData;
import com.samajackun.rodas.sql.model.OnJoinedSource;
import com.samajackun.rodas.sql.model.Provider;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.SelectSentence;
import com.samajackun.rodas.sql.model.TableData;
import com.samajackun.rodas.sql.model.TableSource;

public class MyEngine implements Engine
{
	private final EvaluatorFactory evaluatorFactory=new MyEvaluatorFactory();

	@Override
	public Cursor execute(TableSource source, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		TableData tableData=provider.getTableData(source.getTable());
		List<ColumnMetadata> metadata=provider.getColumnsMetadataFromTable(source.getTable()).getListOfColumnMetadata();
		Cursor cursor=new DefaultCursor(metadata, tableData);
		return cursor;
	}

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
	public Cursor execute(SelectSentence source, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		List<ColumnMetadata> metadata=new ArrayList<>(source.getSelectExpressions().size());
		for (AliasedExpression expression : source.getSelectExpressions())
		{
			String name=expression.getAlias() != null
				? expression.getAlias()
				: expression.getExpression().getName();
			Datatype datatype=expression.getExpression().getDatatype(context, this.evaluatorFactory);
			boolean nullable=true;
			ColumnMetadata columnMetadata=new ColumnMetadata(name, datatype, nullable);
			metadata.add(columnMetadata);
		}
		IterableTableData tableData=null;
		Cursor cursor=new DefaultCursor(metadata, tableData);
		return cursor;
	}

	@Override
	public Cursor execute(OnJoinedSource source, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
