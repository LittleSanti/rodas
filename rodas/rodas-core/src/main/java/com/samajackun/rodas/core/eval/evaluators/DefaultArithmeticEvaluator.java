package com.samajackun.rodas.core.eval.evaluators;

import com.samajackun.rodas.core.eval.AbstractEvaluator;
import com.samajackun.rodas.core.eval.ArithmeticEvaluator;
import com.samajackun.rodas.core.eval.ArithmeticUtils;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class DefaultArithmeticEvaluator extends AbstractEvaluator implements ArithmeticEvaluator
{
	public DefaultArithmeticEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	public Number evaluateAdd(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=context.evaluate(expression1, getEvaluatorFactory());
		Object value2=context.evaluate(expression2, getEvaluatorFactory());
		return ArithmeticUtils.computeAdd(value1, value2);
	}

	@Override
	public Object evaluateAddNumberOrString(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=context.evaluate(expression1, getEvaluatorFactory());
		Object value2=context.evaluate(expression2, getEvaluatorFactory());
		return ArithmeticUtils.computeAddNumberOrString(value1, value2);
	}

	@Override
	public Number evaluateSubstract(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=context.evaluate(expression1, getEvaluatorFactory());
		Object value2=context.evaluate(expression2, getEvaluatorFactory());
		return ArithmeticUtils.computeSubstract(value1, value2);
	}

	@Override
	public Number evaluateMultiply(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=context.evaluate(expression1, getEvaluatorFactory());
		Object value2=context.evaluate(expression2, getEvaluatorFactory());
		return ArithmeticUtils.computeMultiply(value1, value2);
	}

	@Override
	public Number evaluateDivide(Context context, Expression expression1, Expression expression2)
		throws EvaluationException
	{
		Object value1=context.evaluate(expression1, getEvaluatorFactory());
		Object value2=context.evaluate(expression2, getEvaluatorFactory());
		return ArithmeticUtils.computeDivide(value1, value2);
	}

	@Override
	public Number evaluateUnitMinus(Context context, Expression expression)
		throws EvaluationException
	{
		Object value1=context.evaluate(expression, getEvaluatorFactory());
		return ArithmeticUtils.computeUnitMinus(value1);
	}

	@Override
	public Number evaluateUnitPlus(Context context, Expression expression)
		throws EvaluationException
	{
		Object value1=context.evaluate(expression, getEvaluatorFactory());
		return ArithmeticUtils.computeUnitPlus(value1);
	}

	@Override
	public Datatype getDatatypeForAdd(Context context, Expression expression1, Expression expression2)
		throws MetadataException
	{
		Datatype datatype1=expression1.getDatatype(context, getEvaluatorFactory());
		Datatype datatype2=expression2.getDatatype(context, getEvaluatorFactory());
		return getHigestDatatype(datatype1, datatype2);
	}

	@Override
	public Datatype getDatatypeForSubstract(Context context, Expression expression1, Expression expression2)
		throws MetadataException
	{
		Datatype datatype1=expression1.getDatatype(context, getEvaluatorFactory());
		Datatype datatype2=expression2.getDatatype(context, getEvaluatorFactory());
		return getHigestDatatype(datatype1, datatype2);
	}

	private Datatype getHigestDatatype(Datatype datatype1, Datatype datatype2)
	{
		Datatype result;
		if (datatype1 == Datatype.NULL || datatype1 == Datatype.UNKNOWN)
		{
			result=datatype1;
		}
		else if (datatype2 == Datatype.NULL || datatype2 == Datatype.UNKNOWN)
		{
			result=datatype2;
		}
		else if (datatype1 == Datatype.DATE && datatype2 == Datatype.TIME)
		{
			result=Datatype.DATETIME;
		}
		else if (datatype2 == Datatype.DATE && datatype1 == Datatype.TIME)
		{
			result=Datatype.DATETIME;
		}
		else
		{
			result=datatype1.ordinal() > datatype2.ordinal()
				? datatype1
				: datatype2;
		}
		return result;
	}
}
