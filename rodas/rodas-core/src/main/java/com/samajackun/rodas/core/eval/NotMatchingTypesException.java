package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.Expression;

public class NotMatchingTypesException extends EvaluationException
{
	private static final long serialVersionUID=-7978495523679345558L;

	private final Class<?> expectedType;

	private final Class<?> receivedType;

	public NotMatchingTypesException(Class<?> expectedType, Class<?> receivedType)
	{
		super("Operand type expected to be " + expectedType.getName() + ", but a " + receivedType.getName() + " was received instead");
		this.expectedType=expectedType;
		this.receivedType=receivedType;
	}

	public NotMatchingTypesException(Class<?> expectedType, Class<?> receivedType, Expression sourceExpression)
	{
		this(expectedType, receivedType);
		setSourceExpression(sourceExpression);
	}

	public Class<?> getExpectedType()
	{
		return this.expectedType;
	}

	public Class<?> getReceivedType()
	{
		return this.receivedType;
	}

}
