package com.samajackun.rodas.sql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Expression;

public class SelectClause
{
	private final List<Expression> columnsInOrder=new ArrayList<>();

	private final List<ColumnMetaData> columnMetadatasInOrder=new ArrayList<>();

	private final Map<Name, Map<Name, Integer>> columnsByBaseOrQualifiedName=new HashMap<>();

	public List<Expression> getColumnsInOrder()
	{
		return this.columnsInOrder;
	}

	public List<ColumnMetaData> getColumnMetadatasInOrder()
	{
		return this.columnMetadatasInOrder;
	}

	public Map<Name, Map<Name, Integer>> getColumnsByBaseOrQualifiedName()
	{
		return this.columnsByBaseOrQualifiedName;
	}

	public int addColumn(Name name, Expression expression, ColumnMetaData columnMetadata)
	{
		int index=this.columnsInOrder.size();
		this.columnsInOrder.add(expression);
		this.columnMetadatasInOrder.add(columnMetadata);
		this.columnsByBaseOrQualifiedName.computeIfAbsent(name, x -> new HashMap<>()).put(name, index);
		this.columnsByBaseOrQualifiedName.computeIfAbsent(name.getBase(), x -> new HashMap<>()).put(name, index);
		return index;
	}

	public int countColums()
	{
		return this.columnsInOrder.size();
	}
}
