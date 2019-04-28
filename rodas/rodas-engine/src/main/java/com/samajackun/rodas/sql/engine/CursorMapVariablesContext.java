package com.samajackun.rodas.sql.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.RodasRuntimeException;

public class CursorMapVariablesContext implements VariablesContext
{
	private final VariablesContext parent;

	private final Map<String, Cursor> cursorMap;

	private final Map<Name, Object> calculatedValues=new HashMap<>();

	private final Map<String, Cursor> directColumnMap=new HashMap<>();

	public CursorMapVariablesContext(VariablesContext parent, Map<String, Cursor> cursorMap)
	{
		super();
		this.parent=parent;
		this.cursorMap=cursorMap;
	}

	@Override
	public boolean contains(Name name)
	{
		try
		{
			Cursor cursor=getCursor(name);
			boolean x=(cursor != null) && cursor.getColumnMap().get(name.getBase().asString()) != null;
			if (!x && this.parent != null)
			{
				x=this.parent.contains(name);
			}
			return x;
		}
		catch (CursorException e)
		{
			// FIXME Es malo no poder propagar la excepción aquí.
			return false;
		}
	}

	private Cursor getCursor(Name name)
	{
		Cursor cursor;
		if (name.getPrefix() == null)
		{
			cursor=this.directColumnMap.computeIfAbsent(name.getBase().asString(), b -> {
				try
				{
					Cursor found=null;
					for (Iterator<Cursor> iterator=this.cursorMap.values().iterator(); iterator.hasNext();)
					{
						Cursor item=iterator.next();
						if (item.getColumnMap().containsKey(b))
						{
							if (found != null)
							{
								throw new AmbiguousColumnException(name);
							}
							else
							{
								found=item;
							}
						}
					}
					return found;
				}
				catch (CursorException e)
				{
					throw new RodasRuntimeException(e);
				}
			});
		}
		else
		{
			cursor=this.cursorMap.get(name.getPrefix());
		}
		return cursor;
	}

	@Override
	public Object get(Name name)
		throws VariableNotFoundException
	{
		try
		{
			Integer index;
			Object value;
			Cursor cursor=getCursor(name);
			index=(cursor == null)
				? null
				: cursor.getColumnMap().get(name.getBase().asString());
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
}
