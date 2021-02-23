package com.samajackun.rodas.sql.engine;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.DefaultContext;
import com.samajackun.rodas.core.execution.Cursor;

/**
 * This class should be extended by any nested Cursor.
 * It has the responsability of create a new context for the new cursor,
 * with a new VariablesContext object, which takes the current VariablesContext as parent.
 *
 * @author pqSanti
 */
public class AbstractNestedCursor extends AbstractComposedCursor
{
	private final Context context;

	public AbstractNestedCursor(Cursor src, Context producerContext)
	{
		super(src);
		this.context=DefaultContext.createNestedContext(producerContext, new CursorVariablesContext(producerContext.getVariablesManager().peekLocalContext(), src));
	}

	protected Context getContext()
	{
		return this.context;
	}

}
