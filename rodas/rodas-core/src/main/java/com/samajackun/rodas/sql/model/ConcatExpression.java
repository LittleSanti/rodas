package com.samajackun.rodas.sql.model;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class ConcatExpression extends BinaryExpression
{
	public ConcatExpression(String operator, Expression expression1, Expression expression2)
	{
		super(operator, expression1, expression2);
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getTextEvaluator().evaluateConcat(context, getExpression1(), getExpression2());
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reduced;
		Expression reduced1=getExpression1().reduceAndReport(evaluatorFactory);
		Expression reduced2=getExpression2().reduceAndReport(evaluatorFactory);
		if (getExpression1() instanceof TextConstantExpression && getExpression2() instanceof TextConstantExpression)
		{
			String result=evaluatorFactory.getTextEvaluator().evaluateConcat(null, reduced1, reduced2);
			reduced=new TextConstantExpression(result);
		}
		else
		{
			reduced=this;
		}
		return reduced;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		Datatype datatype1=getExpression1().getDatatype(context, evaluatorFactory);
		Datatype datatype2=getExpression1().getDatatype(context, evaluatorFactory);
		Datatype result;
		switch (datatype1)
		{
			case UNKNOWN:
				result=datatype1;
				break;
			default:
				switch (datatype2)
				{
					case UNKNOWN:
						result=datatype2;
						break;
					default:
						result=Datatype.TEXT;
				}
				break;
		}
		return result;
	}
}
