package com.samajackun.rodas.sql.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;

public class CursorVariablesContext implements VariablesContext
{
	private final VariablesContext parent;

	private final Cursor cursor;

	private final Map<Name, Object> calculatedValues=new HashMap<>();

	public CursorVariablesContext(VariablesContext parent, Cursor cursor)
	{
		super();
		this.parent=parent;
		this.cursor=cursor;
	}

	@Override
	public boolean contains(Name name)
	{
		try
		{
			boolean x=cursor.getColumnMap().get(name.asString()) != null;
			if (!x && parent != null)
			{
				x=parent.contains(name);
			}
			return x;
		}
		catch (CursorException e)
		{
			// FIXME Es malo no poder propagar la excepción aquí.
			return false;
		}
	}

	@Override
	public Object get(Name name)
		throws VariableNotFoundException
	{
		try
		{
			Object value;
			Integer index=cursor.getColumnMap().get(name.asString());
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
				value=cursor.getRowData().get(index);
			}
			return value;
		}
		catch (CursorException e)
		{
			// FIXME La excepción real queda oculta.
			throw new VariableNotFoundException(name);
		}
	}

	@Override
	public void set(Name name, Object value)
	{
		calculatedValues.put(name, value);
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
		calculatedValues.remove(name);
	}

	@Override
	public void clear()
	{
		calculatedValues.clear();
	}

	// @Override
	// public Object evaluate(Name name, Supplier<Object> supplier)
	// {
	// try
	// {
	// Object value;
	// Integer index=this.cursor.getColumnMap().get(name.asString());
	// if (index == null)
	// {
	// if (this.parent == null)
	// {
	// value=this.calculatedValues.computeIfAbsent(name, k -> supplier);
	// }
	// else
	// {
	// value=this.parent.evaluate(name, supplier);
	// }
	// }
	// else
	// {
	// value=this.cursor.getRowData().get(index);
	// }
	// return value;
	// }
	// catch (CursorException e)
	// {
	// // FIXME No sé qué excepción poner
	// throw new RuntimeException();
	// }
	// }
}
