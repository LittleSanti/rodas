package com.samajackun.rodas.sql.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.model.RowData;

class RowDataVariablesContext implements VariablesContext
{
	private final VariablesContext parent;

	private RowData rowData;

	private final Map<String, Integer> columnMap;

	private final Map<Name, Object> calculatedValues=new HashMap<>();

	public RowDataVariablesContext(VariablesContext parent, Map<String, Integer> columnMap)
	{
		super();
		this.parent=parent;
		this.columnMap=columnMap;
	}

	@Override
	public boolean contains(Name name)
	{
		boolean x=columnMap.get(name.asString()) != null;
		if (!x && parent != null)
		{
			x=parent.contains(name);
		}
		return x;
	}

	@Override
	public Object get(Name name)
		throws VariableNotFoundException
	{
		Object value;
		Integer index=columnMap.get(name.asString());
		if (index == null)
		{
			if (parent == null)
			{
				throw new VariableNotFoundException(name);
			}
			else
			{
				value=parent.get(name);
			}
		}
		else
		{
			value=rowData.get(index);
		}
		return value;
	}

	@Override
	public void set(Name name, Object value)
	{
		calculatedValues.put(name, value);
	}

	@Override
	public Object setIfAbsent(Name name, Supplier<Object> supplier)
	{
		// Con cacheo:
		return calculatedValues.computeIfAbsent(name, (k) -> supplier);
	}

	@Override
	public void remove(Name name)
	{
		calculatedValues.remove(name);
	}

	@Override
	public void clear()
	{
		calculatedValues.clear();
	}

	public void setRowData(RowData rowData)
	{
		this.rowData=rowData;
	}
}
