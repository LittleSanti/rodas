package com.samajackun.rodas.core.eval;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultRuntime implements Runtime
{
	private final Map<String, Object> systemObjects=new HashMap<>();

	private PrintWriter out=new PrintWriter(System.out, true);

	private PrintWriter err=new PrintWriter(System.err, true);

	@Override
	public PrintWriter getOut()
	{
		return this.out;
	}

	public void setOut(PrintWriter out)
	{
		this.out=out;
	}

	@Override
	public PrintWriter getErr()
	{
		return this.err;
	}

	public void setErr(PrintWriter err)
	{
		this.err=err;
	}

	@Override
	public Set<String> getSystemObjectsKeys()
	{
		return this.systemObjects.keySet();
	}

	@Override
	public Object getSystemObject(String key)
	{
		return this.systemObjects.get(key);
	}

	@Override
	public Object putSystemObject(String key, Object value)
	{
		return this.systemObjects.put(key, value);
	}
}
