package com.samajackun.rodas.core.model;

public final class TextUtils
{
	private TextUtils()
	{
	}

	public static String computeConcat(String value1, String value2)
	{
		return value1 + value2;
	}

	public static String toString(Object value)
	{
		return value == null
			? "null"
			: value.toString();
	}

}