package com.samajackun.rodas.sql.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.model.RowData;

public class RowDataContext implements VariablesContext
{
	private final VariablesContext parent;

	private final Map<String, Integer> columnMap;

	private RowData src;

	private final Map<Name, Object> calculatedValues=new HashMap<>();

	public RowDataContext(VariablesContext parent, Map<String, Integer> columnMap)
	{
		super();
		this.parent=parent;
		this.columnMap=columnMap;
	}

	@Override
	public boolean contains(Name name)
	{
		boolean x=this.columnMap.get(name.asString()) != null;
		if (!x && this.parent != null)
		{
			x=this.parent.contains(name);
		}
		return x;
	}

	@Override
	public Object get(Name name)
		throws VariableNotFoundException
	{
		Object value;
		Integer index=this.columnMap.get(name.asString());
		if (index == null)
		{
			if (this.parent == null)
			{
				throw new VariableNotFoundException(name);
			}
			else
			{
				value=this.parent.get(name);
			}
		}
		else
		{
			value=this.src.get(index);
		}
		return value;
	}

	@Override
	public void set(Name name, Object value)
	{
		this.calculatedValues.put(name, value);
	}

	@Override
	public Object setIfAbsent(Name name, Supplier<Object> supplier)
	{
		// Sin cacheo:
		return supplier.get();
	}

	@Override
	public void remove(Name name)
	{
		this.calculatedValues.remove(name);
	}

	@Override
	public void clear()
	{
		this.calculatedValues.clear();
	}

	public RowData getSrc()
	{
		return this.src;
	}

	public void setSrc(RowData src)
	{
		this.src=src;
		this.calculatedValues.clear();
	}

}
