package com.samajackun.rodas.sql.eval;

import com.samajackun.rodas.sql.model.CursorException;

public interface NewContext
{
	void bindPublicColumn(String prefix, String column)
		throws CursorException,
		ColumnNotFoundException;

	void bindPrivateColumn(String prefix, String column)
		throws CursorException,
		ColumnNotFoundException;

	Object getColumnByName(String column, String prefix)
		throws NameNotBoundException;

	Object getColumnByName(String column)
		throws NameNotBoundException;

	Object getColumnByIndex(int column)
		throws IndexNotBoundException;

	Object getParameter(String name)
		throws ParameterNotFoundException;

}