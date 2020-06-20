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
		boolean x=this.calculatedValues.containsKey(name);
		if (!x)
		{
			x=this.columnMap.get(name.asString()) != null;
		}
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
		Object value=this.calculatedValues.get(name);
		if (value == null && !this.calculatedValues.containsKey(name))
		{
			Integer index=this.columnMap.get(name.asString());
			if (index == null)
			{
				// Esta búsqueda jerárquica quizá no la podamos evitar, pues si al evaluar la fila de un rowser hubiera asignaciones condicionales,
				// sería impossible determinar en tiempo de compilación cuál es el verdadero scope en que está definida una variable.
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
				value=this.rowData.get(index);
			}
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
		// Con cacheo:
		return this.calculatedValues.computeIfAbsent(name, (k) -> supplier.get());
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

	public void setRowData(RowData rowData)
	{
		this.rowData=rowData;
	}
}
