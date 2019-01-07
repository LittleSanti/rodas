package com.samajackun.rodas.core.eval;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProtectedRuntime implements Runtime
{
	private final Map<String, Object> systemObjects=new HashMap<>();

	private final Map<String, Object> customObjects=new HashMap<>(this.systemObjects);

	private PrintWriter out=new PrintWriter(System.out, true);

	private PrintWriter err=new PrintWriter(System.err, true);

	@Override
	public PrintWriter getOut()
	{
		return this.out;
	}

	public ProtectedRuntime setOut(PrintWriter out)
	{
		this.out=out;
		return this;
	}

	@Override
	public PrintWriter getErr()
	{
		return this.err;
	}

	public ProtectedRuntime setErr(PrintWriter err)
	{
		this.err=err;
		return this;
	}

	@Override
	public Set<String> getSystemObjectsKeys()
	{
		return this.customObjects.keySet();
	}

	@Override
	public Object getSystemObject(String key)
	{
		return this.customObjects.get(key);
	}

	@Override
	public Object putSystemObject(String key, Object value)
	{
		if (this.systemObjects.containsKey(key))
		{
			throw new IllegalArgumentException("Unable to overwrite reserved property '" + key + "'");
		}
		return this.customObjects.put(key, value);
	}

	protected Map<String, Object> getSystemObjects()
	{
		return this.systemObjects;
	}

	protected Map<String, Object> getCustomObjects()
	{
		return this.customObjects;
	}
}
