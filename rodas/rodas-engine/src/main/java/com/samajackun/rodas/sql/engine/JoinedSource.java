package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.model.BooleanExpression;
import com.samajackun.rodas.sql.model.ColumnMetadata;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.Engine;
import com.samajackun.rodas.sql.model.EngineException;
import com.samajackun.rodas.sql.model.MetadataException;
import com.samajackun.rodas.sql.model.Provider;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.Source;

public class JoinedSource implements Source
{
	private final Source left;

	private final Source right;

	private final BooleanExpression condition;

	private final EvaluatorFactory evaluatorFactory;

	public JoinedSource(Source left, Source right, BooleanExpression condition, EvaluatorFactory evaluatorFactory)
	{
		this.left=left;
		this.right=right;
		this.condition=condition;
		this.evaluatorFactory=evaluatorFactory;
	}

	@Override
	public String toCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasColumn(String column, Provider provider)
		throws ProviderException
	{
		return this.left.hasColumn(column, provider) || this.right.hasColumn(column, provider);
	}

	@Override
	public Cursor execute(Engine engine, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		JoinedCursor cursor=new JoinedCursor(this.left.execute(engine, provider, context), this.right.execute(engine, provider, context), this.condition, this.evaluatorFactory);
		cursor.initContext(context);
		return cursor;
	}

	@Override
	public List<String> getColumnNames(Provider provider)
		throws ProviderException
	{
		List<String> leftColumns=this.left.getColumnNames(provider);
		List<String> rightColumns=this.left.getColumnNames(provider);
		List<String> columnNames=new ArrayList<>(leftColumns.size() + rightColumns.size());
		columnNames.addAll(leftColumns);
		columnNames.addAll(rightColumns);
		return columnNames;
	}

	@Override
	public ColumnMetadata getColumnMetadata(int column, Provider provider, Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException,
		ProviderException
	{
		ColumnMetadata columnMetadata;
		List<String> columnNamesLeft=this.left.getColumnNames(provider);
		List<String> columnNamesRight=this.right.getColumnNames(provider);
		if (column < columnNamesLeft.size())
		{
			columnMetadata=this.left.getColumnMetadata(column, provider, context, evaluatorFactory);
		}
		else
		{
			column-=columnNamesLeft.size();
			if (column < columnNamesRight.size())
			{
				columnMetadata=this.right.getColumnMetadata(column, provider, context, evaluatorFactory);
			}
			else
			{
				throw new MetadataException("Column index too high");
			}
		}
		return columnMetadata;
	}
}
