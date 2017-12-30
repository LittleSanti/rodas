package com.samajackun.rodas.sql.eval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.model.Cursor;

public class MyContext implements Context
{
	private final Map<String, Context> subcontexts=new HashMap<String, Context>();

	private final List<Context> unnamedContexts=new ArrayList<Context>();

	private final Map<String, Integer> boundValuesMap=new HashMap<String, Integer>();

	private final List<Object> boundValuesList=new ArrayList<Object>();

	private final Map<String, Object> parameters=new HashMap<String, Object>();

	@Override
	public void putSubcontext(String name, Context context)
		throws NameAlreadyBoundException
	{
		if (name != null)
		{
			Context oldContext=this.subcontexts.put(name, context);
			if (oldContext != null)
			{
				throw new NameAlreadyBoundException(name);
			}
		}
		else
		{
			this.unnamedContexts.add(context);
		}
	}

	@Override
	public Context getSubcontext(String prefix)
		throws NameNotBoundException
	{
		return prefix == null
			? this
			: this.subcontexts.get(prefix);
	}

	@Override
	public Object lookup(String name)
		throws NameNotBoundException
	{
		Integer index=this.boundValuesMap.get(name);
		return index == null
			? null
			: this.boundValuesList.get(index);
	}

	@Override
	public void bind(String alias, Object value)
	{
		int index=this.boundValuesList.size();
		this.boundValuesMap.put(alias, index);
		this.boundValuesList.add(value);
	}

	@Override
	public RowResult getResult()
	{
		throw new UnsupportedOperationException();
	}

	public void putParameter(String name, Object value)
	{
		this.parameters.put(name, value);
	}

	@Override
	public Object getParameter(String name)
		throws ParameterNotFoundException
	{
		if (this.parameters.containsKey(name))
		{
			return this.parameters.get(name);
		}
		else
		{
			throw new ParameterNotFoundException(name);
		}
	}

	@Override
	public Context findIdentifier(String identifier)
		throws NameNotBoundException
	{
		Context ctx=null;
		for (Iterator<Context> iterator=this.subcontexts.values().iterator(); iterator.hasNext() && ctx == null;)
		{
			Context c=iterator.next();
			try
			{
				if (c.getColumn(identifier) >= 0)
				{
					ctx=c;
				}
			}
			catch (NameNotBoundException e)
			{
			}
		}
		return ctx;
	}

	@Override
	public Object getValue(int index)
		throws NameNotBoundException
	{
		return this.boundValuesList.get(index);
	}

	@Override
	public Context getContextForAlias(String alias)
		throws NameNotBoundException
	{
		Context ctx=this.subcontexts.get(alias);
		if (ctx == null)
		{
			throw new NameNotBoundException(alias);
		}
		return ctx;
	}

	@Override
	public int getColumn(String name)
		throws NameNotBoundException
	{
		Integer index=this.boundValuesMap.get(name);
		if (index == null)
		{
			throw new NameNotBoundException(name);
		}
		return index;
	}

	@Override
	public Context addCursor(Cursor cursor)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context fork()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
