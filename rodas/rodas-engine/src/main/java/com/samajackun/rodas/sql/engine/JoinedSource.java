package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.BooleanExpression;
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Cursor;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.Source;

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
		return this.left.toCode() + joinTypeToCode() + this.right.toCode() + " ON " + this.condition.toCode();
	}

	protected String joinTypeToCode()
	{
		return " INNER JOIN ";
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
		Cursor cursorLeft=this.left.execute(engine, provider, context);
		Cursor cursorRight=this.right.execute(engine, provider, context);
		JoinedCursor cursor=new JoinedCursor(cursorLeft, cursorRight, this.condition, this.evaluatorFactory, context);
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
