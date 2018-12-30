package com.samajackun.rodas.sql.eval;

import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;

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

	Object getParameter(String name)
		throws ParameterNotFoundException;

	void setParameter(String name, Object value);

	Context fork(Context subContext);

	Object getColumnByIndex(int index)
		throws IndexNotBoundException,
		CursorException;

}