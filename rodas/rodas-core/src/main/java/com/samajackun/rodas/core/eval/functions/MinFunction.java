package com.samajackun.rodas.core.eval.functions;

import java.util.Date;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class MinFunction extends AbstractFunction
{
	public MinFunction(EvaluatorFactory evaluatorFactory)
	{
		super("min", evaluatorFactory, 2, 0);
	}

	@Override
	protected Object evaluateFunction(Context context, List<Object> values)
		throws FunctionEvaluationException
	{
		Object result;
		if (hasAnyNull(values))
		{
			result=null;
		}
		else
		{
			Object value1=values.get(0);
			if (value1 instanceof Number)
			{
				Class<?> scale=getMaxScaleType(values);
				if (scale == Integer.class)
				{
					int number1=((Number)value1).intValue();
					for (int i=1; i < values.size(); i++)
					{
						Object value2=values.get(i);
						int number2=((Number)value2).intValue();
						number1=(number1 < number2)
							? number1
							: number2;
					}
					result=number1;
				}
				else if (scale == Long.class)
				{
					long number1=((Number)value1).longValue();
					for (int i=1; i < values.size(); i++)
					{
						Object value2=values.get(i);
						long number2=((Number)value2).longValue();
						number1=(number1 < number2)
							? number1
							: number2;
					}
					result=number1;
				}
				else if (scale == Float.class)
				{
					float number1=((Number)value1).floatValue();
					for (int i=1; i < values.size(); i++)
					{
						Object value2=values.get(i);
						float number2=((Number)value2).floatValue();
						number1=(number1 < number2)
							? number1
							: number2;
					}
					result=number1;
				}
				else if (scale == Double.class)
				{
					double number1=((Number)value1).doubleValue();
					for (int i=1; i < values.size(); i++)
					{
						Object value2=values.get(i);
						double number2=((Number)value2).doubleValue();
						number1=(number1 < number2)
							? number1
							: number2;
					}
					result=number1;
				}
				else
				{
					throw new IllegalStateException();
				}
			}
			else if (value1 instanceof String)
			{
				String string1=((String)value1);
				for (int i=1; i < values.size(); i++)
				{
					Object value2=values.get(i);
					String string2=((String)value2);
					string1=(string1.compareTo(string2) < 0)
						? string1
						: string2;
				}
				result=string1;
			}
			else if (value1 instanceof Date)
			{
				Date date1=((Date)value1);
				for (int i=1; i < values.size(); i++)
				{
					Object value2=values.get(i);
					Date date2=((Date)value2);
					date1=(date1.compareTo(date2) < 0)
						? date1
						: date2;
				}
				result=date1;
			}
			else
			{
				throw new FunctionEvaluationException("min", "Unsupported parameter type " + value1.getClass().getName());
			}
		}
		return result;
	}

	@Override
	public boolean isDeterministic()
	{
		return true;
	}
}
