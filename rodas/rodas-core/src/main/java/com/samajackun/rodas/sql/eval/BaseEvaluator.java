package com.samajackun.rodas.sql.eval;

import com.samajackun.rodas.sql.model.AliasedExpression;
import com.samajackun.rodas.sql.model.BooleanConstantExpression;
import com.samajackun.rodas.sql.model.Datatype;
import com.samajackun.rodas.sql.model.DatetimeConstantExpression;
import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.model.IdentifierExpression;
import com.samajackun.rodas.sql.model.MetadataException;
import com.samajackun.rodas.sql.model.NamedParameterExpression;
import com.samajackun.rodas.sql.model.NullConstantExpression;
import com.samajackun.rodas.sql.model.NumericConstantExpression;
import com.samajackun.rodas.sql.model.TextConstantExpression;

public interface BaseEvaluator
{
	public Object evaluate(Context context, IdentifierExpression expression)
		throws EvaluationException;

	public boolean evaluate(Context context, BooleanConstantExpression expression)
		throws EvaluationException;

	public Object evaluate(Context context, TextConstantExpression expression)
		throws EvaluationException;

	public Object evaluate(Context context, NumericConstantExpression expression)
		throws EvaluationException;

	public Object evaluate(Context context, NullConstantExpression expression)
		throws EvaluationException;

	public Object evaluate(Context context, AliasedExpression expression)
		throws EvaluationException;
	// Object value=(this.prefix != null
	// ? context.getSubcontext(this.prefix)
	// : context).getBoundValues().get(this.identifier);
	// if (value == null)
	// {
	// throw new NameNotBoundException(this.identifier);
	// }
	// return value;

	public Object evaluate(Context context, NamedParameterExpression expression)
		throws EvaluationException;

	public Object evaluate(Context context, DatetimeConstantExpression datetimeConstantExpression)
		throws EvaluationException;

	public Datatype getDatatype(Context context, IdentifierExpression identifierExpression)
		throws MetadataException;

	public Datatype getDatatype(Context context, NumericConstantExpression numericConstantExpression)
		throws MetadataException;

	public Datatype guessDatatype(Context context, Expression expression)
		throws MetadataException;

}
