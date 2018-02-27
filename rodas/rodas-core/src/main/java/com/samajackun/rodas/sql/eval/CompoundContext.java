package com.samajackun.rodas.sql.eval;

import java.util.Map;

import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;

public class CompoundContext implements Context
{
	private final Context parentContext;

	private final Context targetContext;

	public CompoundContext(Context parentContext, Context targetContext)
	{
		super();
		this.parentContext=parentContext;
		this.targetContext=targetContext;
	}

	@Override
	public void bindPublicColumn(String prefix, String column)
		throws CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		this.targetContext.bindPublicColumn(prefix, column);
	}

	@Override
	public void bindPrivateColumn(String prefix, String column)
		throws CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		this.targetContext.bindPrivateColumn(prefix, column);
	}

	@Override
	public Object getColumnByName(String column, String prefix)
		throws NameNotBoundException
	{
		try
		{
			return this.targetContext.getColumnByName(column, prefix);
		}
		catch (NameNotBoundException e)
		{
			// TODO
			return this.parentContext.getColumnByName(column, prefix);
		}
	}

	@Override
	public Object getColumnByName(String column)
		throws NameNotBoundException
	{
		try
		{
			return this.targetContext.getColumnByName(column);
		}
		catch (NameNotBoundException e)
		{
			// TODO
			return this.parentContext.getColumnByName(column);
		}
	}

	@Override
	public Object getColumnByIndex(int column)
		throws IndexNotBoundException
	{
		try
		{
			return this.targetContext.getColumnByIndex(column);
		}
		catch (IndexNotBoundException e)
		{
			// TODO
			return this.parentContext.getColumnByIndex(column);
		}
	}

	@Override
	public int getColumnIndexByName(String column, String prefix)
		throws NameNotBoundException
	{
		try
		{
			return this.targetContext.getColumnIndexByName(column, prefix);
		}
		catch (NameNotBoundException e)
		{
			// TODO
			return this.parentContext.getColumnIndexByName(column, prefix);
		}
	}

	@Override
	public Object getParameter(String name)
		throws ParameterNotFoundException
	{
		Object value=this.targetContext.getParameter(name);
		if (value == null)
		{
			value=this.parentContext.getParameter(name);
		}
		return value;
	}

	@Override
	public void setParameter(String name, Object value)
	{
		this.targetContext.setParameter(name, value);
	}

	@Override
	public Context fork(Map<String, Cursor> cursors)
	{
		return new CompoundContext(new DefaultContext(cursors), this);
	}
}
