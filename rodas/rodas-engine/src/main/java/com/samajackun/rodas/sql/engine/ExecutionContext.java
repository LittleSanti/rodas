package com.samajackun.rodas.sql.engine;

import com.samajackun.rodas.sql.eval.IndexNotBoundException;
import com.samajackun.rodas.sql.eval.NameNotBoundException;
import com.samajackun.rodas.sql.eval.ParameterNotFoundException;

public interface ExecutionContext
{
	Object getColumnByName(String column, String prefix)
		throws NameNotBoundException;

	Object getColumnByName(String column)
		throws NameNotBoundException;

	Object getColumnByIndex(int column)
		throws IndexNotBoundException;

	Object getParameter(String name)
		throws ParameterNotFoundException;

}