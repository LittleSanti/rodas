package com.samajackun.rodas.core.eval.functions;

public final class Casts
{
	private Casts()
	{
	}

	public static String asString(String functionName, String parameterName, Object value)
		throws InvalidParameterType
	{
		if (value == null || value instanceof String)
		{
			return (String)value;
		}
		else
		{
			throw new InvalidParameterType(functionName, parameterName, "String");
		}
	}

	public static long asNumber(String functionName, String parameterName, Object value)
		throws InvalidParameterType
	{
		if (value instanceof Number)
		{
			return ((Number)value).longValue();
		}
		else
		{
			throw new InvalidParameterType(functionName, parameterName, "Number");
		}
	}

	public static boolean asBoolean(String functionName, String parameterName, Object value)
		throws InvalidParameterType
	{
		if (value instanceof Boolean)
		{
			return ((Boolean)value).booleanValue();
		}
		else
		{
			throw new InvalidParameterType(functionName, parameterName, "Boolean");
		}
	}
}
