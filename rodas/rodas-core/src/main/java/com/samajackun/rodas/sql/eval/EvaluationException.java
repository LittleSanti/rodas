package com.samajackun.rodas.sql.eval;

import com.samajackun.rodas.sql.RodasSqlException;
import com.samajackun.rodas.sql.model.Expression;

public class EvaluationException extends RodasSqlException
{
	private static final long serialVersionUID=-6889212332182068560L;

	private Expression sourceExpression;

	public EvaluationException(String message)
	{
		super(message);
	}

	public EvaluationException(Throwable cause)
	{
		super(cause);
	}

	public EvaluationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public EvaluationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public Expression getSourceExpression()
	{
		return this.sourceExpression;
	}

	public void setSourceExpression(Expression sourceExpression)
	{
		this.sourceExpression=sourceExpression;
	}

	@Override
	public String getMessage()
	{
		return super.getMessage() + (this.sourceExpression == null
			? ""
			: " in expression " + this.sourceExpression.toCode());
	}
}
