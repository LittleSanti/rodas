package com.samajackun.rodas.sql.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class OnJoinedSource implements Source
{
	public enum Type {
		INNER, OUTER, LEFT, RIGHT
	};

	private final Source leftSource;

	private final Source rightSource;

	private final Type type;

	private final BooleanExpression on;

	private final Map<String, Source> sourceByColumn=new HashMap<String, Source>(33);

	public OnJoinedSource(Source leftSource, Source rightSource, Type type, BooleanExpression on)
	{
		super();
		this.leftSource=leftSource;
		this.rightSource=rightSource;
		this.type=type;
		this.on=on;
	}

	public Source getLeftSource()
	{
		return this.leftSource;
	}

	public Source getRightSource()
	{
		return this.rightSource;
	}

	public Type getType()
	{
		return this.type;
	}

	@Override
	public String toCode()
	{
		return this.leftSource.toCode() + " " + this.type + " " + "ON " + this.on.toCode();
	}

	@Override
	public boolean hasColumn(String column, Provider provider)
		throws ProviderException
	{
		boolean x;
		if (x=this.leftSource.hasColumn(column, provider))
		{
			this.sourceByColumn.put(column, this.leftSource);
		}
		else if (x=this.rightSource.hasColumn(column, provider))
		{
			this.sourceByColumn.put(column, this.rightSource);
		}
		return x;
	}

	public BooleanExpression getOn()
	{
		return this.on;
	}

	@Override
	public List<String> getColumnNames(Provider provider)
		throws ProviderException
	{
		List<String> columnNamesLeft=this.leftSource.getColumnNames(provider);
		List<String> columnNamesRight=this.rightSource.getColumnNames(provider);
		List<String> columnNames=new ArrayList<>(columnNamesLeft.size() + columnNamesRight.size());
		columnNames.addAll(columnNamesLeft);
		columnNames.addAll(columnNamesRight);
		return columnNames;
	}

	@Override
	public Cursor execute(Engine engine, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return engine.execute(this, provider, context);
	}

	@Override
	public ColumnMetadata getColumnMetadata(int column, Provider provider, Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException,
		ProviderException
	{
		ColumnMetadata columnMetadata;
		List<String> columnNamesLeft=this.leftSource.getColumnNames(provider);
		List<String> columnNamesRight=this.rightSource.getColumnNames(provider);
		if (column < columnNamesLeft.size())
		{
			columnMetadata=this.leftSource.getColumnMetadata(column, provider, context, evaluatorFactory);
		}
		else
		{
			column-=columnNamesLeft.size();
			if (column < columnNamesRight.size())
			{
				columnMetadata=this.rightSource.getColumnMetadata(column, provider, context, evaluatorFactory);
			}
			else
			{
				throw new MetadataException("Column index too high");
			}
		}
		return columnMetadata;
	}
}
