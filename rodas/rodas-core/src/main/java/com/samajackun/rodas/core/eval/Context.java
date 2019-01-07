package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.Cursor;
import com.samajackun.rodas.core.model.CursorException;

public interface Context
{
	MapList<String, Cursor> getCursors();

	Object getColumnByName(String column, String prefix)
		throws NameNotBoundException,
		CursorException;

	Object getColumnByName(String column)
		throws NameNotBoundException,
		CursorException;

	int getColumnIndexByName(String column, String prefix)
		throws NameNotBoundException;

	Context fork(Context subContext);

	Object getColumnByIndex(int index)
		throws IndexNotBoundException,
		CursorException;

	public VariablesManager getVariablesManager();

	public Runtime getRuntime();
}