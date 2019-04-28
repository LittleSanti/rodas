package com.samajackun.rodas.core.eval.evaluators;

import java.sql.Timestamp;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.BaseEvaluator;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.NameNotBoundException;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.BooleanConstantExpression;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.DatetimeConstantExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.rodas.core.model.NamedParameterExpression;
import com.samajackun.rodas.core.model.NullConstantExpression;
import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.rodas.core.model.TextConstantExpression;

public class DefaultBaseEvaluator extends AbstractEvaluator implements BaseEvaluator
{
	public DefaultBaseEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	public Object evaluate(Context context, IdentifierExpression expression)
		throws EvaluationException
	{
		return context.getVariablesManager().getNearestVariable(expression.getName());
		// Object value;
		// if (expression.getPrefix() == null)
		// {
		// value=context.getVariablesManager().getNearestVariable(expression.getName());
		// }
		// else
		// {
		// VariablesContext variablesContext=context.getVariablesManager().getVariablesContextByAlias(expression.getName().getPrefix());
		// value=variablesContext.get(expression.getIdentifier());
		// }
		// return value;
		// try
		// {
		// // Object value=context.getColumnByName(expression.getIdentifier(), expression.getPrefix());
		// if (value == null)
		// {
		// throw new NameNotBoundException(expression.getIdentifier(), expression.getPrefix());
		// }
		// return value;
		// }
		// catch (CursorException e)
		// {
		// throw new EvaluationException(e);
		// }
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
		Object value=context.evaluate(expression.getExpression(), getEvaluatorFactory());
		// context.getResult().addAlias(expression.getAlias(), value);
		return value;
	}

	@Override
	public Object evaluate(Context context, NamedParameterExpression expression)
		throws EvaluationException
	{
		try
		{
			return context.getVariablesManager().getGlobalVariable(expression.getName());
		}
		catch (VariableNotFoundException e)
		{
			throw new EvaluationException(e);
		}
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
		catch (NameNotBoundException | CursorException e)
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
			else
			{
				throw new IllegalArgumentException("Unknown datatype " + cl.getName());
			}
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
