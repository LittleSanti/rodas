package com.samajackun.rodas.sql.eval;

import java.util.Map;

import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;

/**
 * Represents the state of a query while its results are being constructed:
 * A place where data may be bound by a name.
 * A context is the same for all the columns within a row.
 * A context relies upon its parent context which represents the relating row.
 * In the last place, a context also contains the user input parametes.
 *
 * @author SKN
 */
public interface Context
{
	void bindPublicColumn(String prefix, String column)
		throws CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException;

	void bindPrivateColumn(String prefix, String column)
		throws CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException;

	Object getColumnByName(String column, String prefix)
		throws NameNotBoundException;

	Object getColumnByName(String column)
		throws NameNotBoundException;

	Object getColumnByIndex(int column)
		throws IndexNotBoundException;

	int getColumnIndexByName(String column, String prefix)
		throws NameNotBoundException;

	Object getParameter(String name)
		throws ParameterNotFoundException;

	void setParameter(String name, Object value);

	Context fork(Map<String, Cursor> cursors);
}
