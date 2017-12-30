package com.samajackun.rodas.sql.eval;

import java.util.List;

import com.samajackun.rodas.sql.model.Expression;

public interface CollectionsEvaluator
{
	public boolean evaluateIn(Context context, Expression expression1, Expression expression2)
		throws EvaluationException;

	public boolean evaluateExists(Context context, Expression expression1)
		throws EvaluationException;

	public Object evaluateAsterisk(Context context, Expression expression1)
		throws EvaluationException;

	public Object evaluateList(Context context, List<Expression> expressions)
		throws EvaluationException;

	// {
	// if (this.prefix != null)
	// {
	// context=context.getSubcontext(this.prefix);
	// if (context == null)
	// {
	// throw new PrefixNotBoundException(this.prefix);
	// }
	// }
	// for (Map.Entry<String, Object> entry : context.getBoundValues().entrySet())
	// {
	// context.getResult().addAlias(entry.getKey(), entry.getValue());
	// }
	// return context.getBoundValues().values();
	// }

}
