package com.samajackun.rodas.core.model;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

public class OnJoinedSource implements Source
{
	private static final long serialVersionUID=5706849727964840994L;

	public enum Type {
		INNER, OUTER, LEFT, RIGHT
	};

	private final Source leftSource;

	private final Source rightSource;

	private final Type type;

	private final BooleanExpression on;

	// private final Map<String, Source> sourceByColumn=new HashMap<>(33);

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

	public BooleanExpression getOn()
	{
		return this.on;
	}

	@Override
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return engine.execute(this, context);
	}

	// @Override
	// public boolean hasColumn(String column)
	// throws ProviderException
	// {
	// boolean x;
	// if (x=this.leftSource.hasColumn(column))
	// {
	// this.sourceByColumn.put(column, this.leftSource);
	// }
	// else if (x=this.rightSource.hasColumn(column))
	// {
	// this.sourceByColumn.put(column, this.rightSource);
	// }
	// return x;
	// }
	//

	// @Override
	// public List<String> getColumnNames()
	// throws ProviderException
	// {
	// List<String> columnNamesLeft=this.leftSource.getColumnNames();
	// List<String> columnNamesRight=this.rightSource.getColumnNames();
	// List<String> columnNames=new ArrayList<>(columnNamesLeft.size() + columnNamesRight.size());
	// columnNames.addAll(columnNamesLeft);
	// columnNames.addAll(columnNamesRight);
	// return columnNames;
	// }
	//
	// @Override
	// public ColumnMetadata getColumnMetadata(int column, Context context, EvaluatorFactory evaluatorFactory)
	// throws MetadataException,
	// ProviderException
	// {
	// ColumnMetadata columnMetadata;
	// List<String> columnNamesLeft=this.leftSource.getColumnNames();
	// List<String> columnNamesRight=this.rightSource.getColumnNames();
	// if (column < columnNamesLeft.size())
	// {
	// columnMetadata=this.leftSource.getColumnMetadata(column, context, evaluatorFactory);
	// }
	// else
	// {
	// column-=columnNamesLeft.size();
	// if (column < columnNamesRight.size())
	// {
	// columnMetadata=this.rightSource.getColumnMetadata(column, context, evaluatorFactory);
	// }
	// else
	// {
	// throw new MetadataException("Column index too high");
	// }
	// }
	// return columnMetadata;
	// }
}
