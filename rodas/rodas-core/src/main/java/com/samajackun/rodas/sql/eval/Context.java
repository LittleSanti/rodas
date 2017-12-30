package com.samajackun.rodas.sql.eval;

import com.samajackun.rodas.sql.model.Cursor;

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
	public void putSubcontext(String prefix, Context context)
		throws NameAlreadyBoundException;

	public Context getSubcontext(String prefix)
		throws NameNotBoundException;

	public Context findIdentifier(String identifier)
		throws NameNotBoundException;

	public Object lookup(String column)
		throws NameNotBoundException;

	public Object getValue(int column)
		throws NameNotBoundException;

	public void bind(String alias, Object value)
		throws NameAlreadyBoundException;

	public RowResult getResult();

	public Object getParameter(String parameter)
		throws ParameterNotFoundException;

	public Context getContextForAlias(String alias)
		throws NameNotBoundException;

	public int getColumn(String column)
		throws NameNotBoundException;

	public Context fork();

	public Context addCursor(Cursor cursor);
}
