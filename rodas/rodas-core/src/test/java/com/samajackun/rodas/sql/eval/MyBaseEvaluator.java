package com.samajackun.rodas.sql.eval;

import java.sql.Timestamp;

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

public class MyBaseEvaluator extends AbstractEvaluator implements BaseEvaluator
{
	public MyBaseEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	public Object evaluate(Context context, IdentifierExpression expression)
		throws EvaluationException
	{
		Object value=context.getColumnByName(expression.getIdentifier(), expression.getPrefix());
		if (value == null)
		{
			throw new NameNotBoundException(expression.getIdentifier(), expression.getPrefix());
		}
		return value;
	}

	@Override
	public boolean evaluate(Context context, BooleanConstantExpression expression)
		throws EvaluationException
	{
		return expression.getBooleanValue();
	}

	@Override
	public Object evaluate(Context context, TextConstantExpression expression)
		throws EvaluationException
	{
		return expression.getValue();
	}

	@Override
	public Object evaluate(Context context, NumericConstantExpression expression)
		throws EvaluationException
	{
		return expression.getNumericValue();
	}

	@Override
	public Object evaluate(Context context, NullConstantExpression expression)
		throws EvaluationException
	{
		return null;
	}

	@Override
	public Object evaluate(Context context, AliasedExpression expression)
		throws EvaluationException
	{
		Object value=expression.evaluate(context, getEvaluatorFactory());
		// context.getResult().addAlias(expression.getAlias(), value);
		return value;
	}

	@Override
	public Object evaluate(Context context, NamedParameterExpression expression)
		throws EvaluationException
	{
		return context.getParameter(expression.getName());
	}

	@Override
	public Object evaluate(Context context, DatetimeConstantExpression expression)
		throws EvaluationException
	{
		return expression.getNumericValue();
	}

	@Override
	public Datatype getDatatype(Context context, IdentifierExpression expression)
		throws MetadataException
	{
		try
		{
			// TODO La determinación de tipos habrá que hacerla a priori.
			Object value=context.getColumnByName(expression.getIdentifier(), expression.getPrefix());
			return guessDatatype(value);
		}
		catch (NameNotBoundException e)
		{
			throw new MetadataException(e);
		}
	}

	private Datatype guessDatatype(Object value)
	{
		Datatype datatype;
		if (value == null)
		{
			datatype=Datatype.NULL;
		}
		else
		{
			Class<?> cl=value.getClass();
			if (cl == int.class || cl == long.class || cl == byte.class || cl == short.class || cl == Integer.class || cl == Long.class || cl == Byte.class || cl == Short.class)
			{
				datatype=Datatype.INTEGER_NUMBER;
			}
			else if (cl == double.class || cl == float.class || cl == Double.class || cl == Float.class)
			{
				datatype=Datatype.DECIMAL_NUMBER;
			}
			else if (cl == String.class)
			{
				datatype=Datatype.TEXT;
			}
			else if (cl == Timestamp.class)
			{
				datatype=Datatype.DATETIME;
			}
			else if (value instanceof java.util.Date)
			{
				datatype=Datatype.DATE;
			}
			throw new IllegalArgumentException("Unknown datatype " + cl.getName());
		}
		return datatype;
	}

	@Override
	public Datatype getDatatype(Context context, NumericConstantExpression expression)
		throws MetadataException
	{
		return guessDatatype(expression.getNumericValue());
	}

	@Override
	public Datatype guessDatatype(Context context, Expression expression)
		throws MetadataException
	{
		return expression.getDatatype(context, getEvaluatorFactory());
	}
}
