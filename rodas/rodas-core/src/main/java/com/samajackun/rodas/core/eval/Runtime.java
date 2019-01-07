package com.samajackun.rodas.core.eval;

import java.io.PrintWriter;
import java.util.Set;

public interface Runtime
{
	public PrintWriter getOut();

	public PrintWriter getErr();

	public Set<String> getSystemObjectsKeys();

	public Object getSystemObject(String key);

	public Object putSystemObject(String key, Object value);
}
