package com.samajackun.rodas.sql.model;

import java.util.Date;

import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.NotMatchingTypesException;
import com.samajackun.rodas.sql.eval.UnsuportedDatatypeException;

public class ReationalUtils
{
	private ReationalUtils()
	{
	}

	public static Boolean computeEquals(Object value1, Object value2)
		throws EvaluationException
	{
		boolean x;
		if (value1 == null)
		{
			x=(value2 == null);
		}
		else if (value2 == null)
		{
			x=false;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				x=(((Number)value1).doubleValue() == ((Number)value2).doubleValue());
			}
			else
			{
				throw new NotMatchingTypesException(Number.class, value2.getClass());
			}
		}
		else if (value1 instanceof String)
		{
			if (value2 instanceof String)
			{
				x=(((String)value1).compareTo((String)value2)) == 0;
			}
			else
			{
				throw new NotMatchingTypesException(String.class, value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			if (value2 instanceof Boolean)
			{
				x=((Boolean)value1) && ((Boolean)value2);
			}
			else
			{
				throw new NotMatchingTypesException(Boolean.class, value2.getClass());
			}
		}
		else if (value1 instanceof Date)
		{
			if (value2 instanceof Date)
			{
				x=(((Date)value1).compareTo((Date)value2)) == 0;
			}
			else
			{
				throw new NotMatchingTypesException(Date.class, value2.getClass());
			}
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static Boolean computeLowerThan(Object value1, Object value2)
		throws EvaluationException
	{
		boolean x;
		if (value1 == null)
		{
			if (value2 == null)
			{
				x=false;
			}
			else
			{
				x=true;
			}
		}
		else if (value2 == null)
		{
			x=false;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				x=(((Number)value1).doubleValue() < ((Number)value2).doubleValue());
			}
			else
			{
				throw new NotMatchingTypesException(Number.class, value2.getClass());
			}
		}
		else if (value1 instanceof String)
		{
			if (value2 instanceof String)
			{
				x=(((String)value1).compareTo((String)value2)) < 0;
			}
			else
			{
				throw new NotMatchingTypesException(String.class, value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			if (value2 instanceof Boolean)
			{
				x=!((Boolean)value1) && ((Boolean)value2);
			}
			else
			{
				throw new NotMatchingTypesException(Boolean.class, value2.getClass());
			}
		}
		else if (value1 instanceof Date)
		{
			if (value2 instanceof Date)
			{
				x=(((Date)value1).compareTo((Date)value2)) < 0;
			}
			else
			{
				throw new NotMatchingTypesException(Date.class, value2.getClass());
			}
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static boolean computeLowerThanOrEquals(Object value1, Object value2)
		throws EvaluationException
	{
		boolean x;
		if (value1 == null)
		{
			x=true;
		}
		else if (value2 == null)
		{
			x=false;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				x=(((Number)value1).doubleValue() <= ((Number)value2).doubleValue());
			}
			else
			{
				throw new NotMatchingTypesException(Number.class, value2.getClass());
			}
		}
		else if (value1 instanceof String)
		{
			if (value2 instanceof String)
			{
				x=(((String)value1).compareTo((String)value2)) <= 0;
			}
			else
			{
				throw new NotMatchingTypesException(String.class, value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			if (value2 instanceof Boolean)
			{
				x=!((Boolean)value1);
			}
			else
			{
				throw new NotMatchingTypesException(Boolean.class, value2.getClass());
			}
		}
		else if (value1 instanceof Date)
		{
			if (value2 instanceof Date)
			{
				x=(((Date)value1).compareTo((Date)value2)) <= 0;
			}
			else
			{
				throw new NotMatchingTypesException(Date.class, value2.getClass());
			}
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static boolean computeGreaterThan(Object value1, Object value2)
		throws EvaluationException
	{
		boolean x;
		if (value1 == null)
		{
			x=false;
		}
		else if (value2 == null)
		{
			x=true;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				x=(((Number)value1).doubleValue() > ((Number)value2).doubleValue());
			}
			else
			{
				throw new NotMatchingTypesException(Number.class, value2.getClass());
			}
		}
		else if (value1 instanceof String)
		{
			if (value2 instanceof String)
			{
				x=(((String)value1).compareTo((String)value2)) > 0;
			}
			else
			{
				throw new NotMatchingTypesException(String.class, value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			if (value2 instanceof Boolean)
			{
				x=((Boolean)value1) && !((Boolean)value2);
			}
			else
			{
				throw new NotMatchingTypesException(Boolean.class, value2.getClass());
			}
		}
		else if (value1 instanceof Date)
		{
			if (value2 instanceof Date)
			{
				x=(((Date)value1).compareTo((Date)value2)) > 0;
			}
			else
			{
				throw new NotMatchingTypesException(Date.class, value2.getClass());
			}
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static boolean computeGreaterThanOrEquals(Object value1, Object value2)
		throws EvaluationException
	{
		boolean x;
		if (value2 == null)
		{
			x=true;
		}
		else if (value1 == null)
		{
			x=false;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				x=(((Number)value1).doubleValue() >= ((Number)value2).doubleValue());
			}
			else
			{
				throw new NotMatchingTypesException(Number.class, value2.getClass());
			}
		}
		else if (value1 instanceof String)
		{
			if (value2 instanceof String)
			{
				x=(((String)value1).compareTo((String)value2)) >= 0;
			}
			else
			{
				throw new NotMatchingTypesException(String.class, value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			if (value2 instanceof Boolean)
			{
				x=!((Boolean)value2);
			}
			else
			{
				throw new NotMatchingTypesException(Boolean.class, value2.getClass());
			}
		}
		else if (value1 instanceof Date)
		{
			if (value2 instanceof Date)
			{
				x=(((Date)value1).compareTo((Date)value2)) >= 0;
			}
			else
			{
				throw new NotMatchingTypesException(Date.class, value2.getClass());
			}
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static boolean computeNotEquals(Object value1, Object value2)
		throws EvaluationException
	{
		boolean x;
		if (value1 == null)
		{
			x=(value2 != null);
		}
		else if (value2 == null)
		{
			x=true;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				x=(((Number)value1).doubleValue() != ((Number)value2).doubleValue());
			}
			else
			{
				throw new NotMatchingTypesException(Number.class, value2.getClass());
			}
		}
		else if (value1 instanceof String)
		{
			if (value2 instanceof String)
			{
				x=(((String)value1).compareTo((String)value2)) != 0;
			}
			else
			{
				throw new NotMatchingTypesException(String.class, value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			if (value2 instanceof Boolean)
			{
				x=((Boolean)value1) ^ ((Boolean)value2);
			}
			else
			{
				throw new NotMatchingTypesException(Boolean.class, value2.getClass());
			}
		}
		else if (value1 instanceof Date)
		{
			if (value2 instanceof Date)
			{
				x=(((Date)value1).compareTo((Date)value2)) != 0;
			}
			else
			{
				throw new NotMatchingTypesException(Date.class, value2.getClass());
			}
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}
}
