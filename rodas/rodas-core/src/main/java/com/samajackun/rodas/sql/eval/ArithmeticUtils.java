package com.samajackun.rodas.sql.eval;

import java.util.Date;

public final class ArithmeticUtils
{
	private ArithmeticUtils()
	{
	}

	public static Number computeAdd(Object value1, Object value2)
		throws EvaluationException
	{
		Number x;
		if (value1 == null || value2 == null)
		{
			x=null;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				if (value1 instanceof Integer)
				{
					int v1=((Number)value1).intValue();
					if (value2 instanceof Integer)
					{
						x=v1 + ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 + ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 + ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 + ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 + ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Long)
				{
					long v1=((Number)value1).longValue();
					if (value2 instanceof Integer)
					{
						x=v1 + ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 + ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 + ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 + ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 + ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Float)
				{
					float v1=((Number)value1).floatValue();
					if (value2 instanceof Integer)
					{
						x=v1 + ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 + ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 + ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 + ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 + ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Double)
				{
					double v1=((Number)value1).doubleValue();
					if (value2 instanceof Integer)
					{
						x=v1 + ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 + ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 + ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 + ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 + ((Number)value2).intValue();
					}
				}
				else
				{
					// Por defecto se trata como integer:
					int v1=((Number)value1).intValue();
					if (value2 instanceof Integer)
					{
						x=v1 + ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 + ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 + ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 + ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 + ((Number)value2).intValue();
					}
				}
			}
			else
			{
				throw new NotMatchingTypesException(value1.getClass(), value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			throw new NotMatchingTypesException(value1.getClass(), Number.class);
		}
		else if (value1 instanceof Date)
		{
			throw new NotMatchingTypesException(value1.getClass(), Number.class);
		}
		// else if (value1 instanceof String)
		// {
		// if (value2 instanceof String)
		// {
		// x=TextUtils.computeConcat((String)value1, (String)value2);
		// }
		// else
		// {
		// throw new NotMatchingTypesException(value1.getClass(), value2.getClass());
		// }
		// }
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static Number computeSubstract(Object value1, Object value2)
		throws EvaluationException
	{
		Number x;
		if (value1 == null || value2 == null)
		{
			x=null;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				if (value1 instanceof Integer)
				{
					int v1=((Number)value1).intValue();
					if (value2 instanceof Integer)
					{
						x=v1 - ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 - ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 - ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 - ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 - ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Long)
				{
					long v1=((Number)value1).longValue();
					if (value2 instanceof Integer)
					{
						x=v1 - ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 - ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 - ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 - ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 - ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Float)
				{
					float v1=((Number)value1).floatValue();
					if (value2 instanceof Integer)
					{
						x=v1 - ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 - ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 - ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 - ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 - ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Double)
				{
					double v1=((Number)value1).doubleValue();
					if (value2 instanceof Integer)
					{
						x=v1 - ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 - ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 - ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 - ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 - ((Number)value2).intValue();
					}
				}
				else
				{
					// Por defecto se trata como integer:
					int v1=((Number)value1).intValue();
					if (value2 instanceof Integer)
					{
						x=v1 - ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 - ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 - ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 - ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 - ((Number)value2).intValue();
					}
				}
			}
			else
			{
				throw new NotMatchingTypesException(value1.getClass(), value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			throw new NotMatchingTypesException(value1.getClass(), Number.class);
		}
		else if (value1 instanceof Date)
		{
			throw new NotMatchingTypesException(value1.getClass(), Number.class);
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static Number computeMultiply(Object value1, Object value2)
		throws EvaluationException
	{
		Number x;
		if (value1 == null || value2 == null)
		{
			x=null;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				if (value1 instanceof Integer)
				{
					int v1=((Number)value1).intValue();
					if (value2 instanceof Integer)
					{
						x=v1 * ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 * ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 * ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 * ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 * ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Long)
				{
					long v1=((Number)value1).longValue();
					if (value2 instanceof Integer)
					{
						x=v1 * ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 * ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 * ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 * ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 * ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Float)
				{
					float v1=((Number)value1).floatValue();
					if (value2 instanceof Integer)
					{
						x=v1 * ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 * ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 * ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 * ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 * ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Double)
				{
					double v1=((Number)value1).doubleValue();
					if (value2 instanceof Integer)
					{
						x=v1 * ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 * ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 * ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 * ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 * ((Number)value2).intValue();
					}
				}
				else
				{
					// Por defecto se trata como integer:
					int v1=((Number)value1).intValue();
					if (value2 instanceof Integer)
					{
						x=v1 * ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 * ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 * ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 * ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 * ((Number)value2).intValue();
					}
				}
			}
			else
			{
				throw new NotMatchingTypesException(value1.getClass(), value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			throw new NotMatchingTypesException(value1.getClass(), Number.class);
		}
		else if (value1 instanceof Date)
		{
			throw new NotMatchingTypesException(value1.getClass(), Number.class);
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static Number computeDivide(Object value1, Object value2)
		throws EvaluationException
	{
		Number x;
		if (value1 == null || value2 == null)
		{
			x=null;
		}
		else if (value1 instanceof Number)
		{
			if (value2 instanceof Number)
			{
				if (value1 instanceof Integer)
				{
					int v1=((Number)value1).intValue();
					if (value2 instanceof Integer)
					{
						x=v1 / ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 / ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 / ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 / ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 / ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Long)
				{
					long v1=((Number)value1).longValue();
					if (value2 instanceof Integer)
					{
						x=v1 / ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 / ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 / ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 / ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 / ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Float)
				{
					float v1=((Number)value1).floatValue();
					if (value2 instanceof Integer)
					{
						x=v1 / ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 / ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 / ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 / ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 / ((Number)value2).intValue();
					}
				}
				else if (value1 instanceof Double)
				{
					double v1=((Number)value1).doubleValue();
					if (value2 instanceof Integer)
					{
						x=v1 / ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 / ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 / ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 / ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 / ((Number)value2).intValue();
					}
				}
				else
				{
					// Por defecto se trata como integer:
					int v1=((Number)value1).intValue();
					if (value2 instanceof Integer)
					{
						x=v1 / ((Number)value2).intValue();
					}
					else if (value2 instanceof Long)
					{
						x=v1 / ((Number)value2).longValue();
					}
					else if (value2 instanceof Float)
					{
						x=v1 / ((Number)value2).floatValue();
					}
					else if (value2 instanceof Double)
					{
						x=v1 / ((Number)value2).doubleValue();
					}
					else
					{
						x=v1 / ((Number)value2).intValue();
					}
				}
			}
			else
			{
				throw new NotMatchingTypesException(value1.getClass(), value2.getClass());
			}
		}
		else if (value1 instanceof Boolean)
		{
			throw new NotMatchingTypesException(value1.getClass(), Number.class);
		}
		else if (value1 instanceof Date)
		{
			throw new NotMatchingTypesException(value1.getClass(), Number.class);
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static Number computeUnitMinus(Object value1)
		throws EvaluationException
	{
		Number x;
		if (value1 == null)
		{
			x=null;
		}
		else if (value1 instanceof Number)
		{
			if (value1 instanceof Integer)
			{
				x=-((Number)value1).intValue();
			}
			else if (value1 instanceof Long)
			{
				x=-((Number)value1).longValue();
			}
			else if (value1 instanceof Long)
			{
				x=-((Number)value1).longValue();
			}
			else
			{
				// Por defecto se trata como integer:
				x=-((Number)value1).intValue();
			}
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}

	public static Number computeUnitPlus(Object value1)
		throws EvaluationException
	{
		Number x;
		if (value1 == null)
		{
			x=null;
		}
		else if (value1 instanceof Number)
		{
			x=(Number)value1;
		}
		else
		{
			throw new UnsuportedDatatypeException(value1.getClass());
		}
		return x;
	}
}
