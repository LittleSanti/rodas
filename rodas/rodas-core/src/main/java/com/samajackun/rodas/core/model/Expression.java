package com.samajackun.rodas.core.model;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.Name;

public interface Expression extends Codeable
{
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException;

	default Object evaluateAndReport(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		try
		{
			return evaluate(context, evaluatorFactory);
		}
		catch (EvaluationException e)
		{
			e.setSourceExpression(this);
			throw e;
		}
	}

	public default Name getCodeAsName()
	{
		return Name.instanceOf(toCode());
	}

	public default Name getName()
	{
		return null;
	}

	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException;

	default Expression reduceAndReport(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		try
		{
			return reduce(evaluatorFactory);
		}
		catch (EvaluationException e)
		{
			e.setSourceExpression(this);
			throw e;
		}
	}

	public default Executor createExecutor(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return new OperationExecutor(context, evaluatorFactory, reduce(evaluatorFactory));
	}

	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException;

	public default List<Expression> toPhysicalExpressions(Provider provider)
		throws ProviderException
	{
		return Collections.singletonList(this);
	}

	public default Object feedAndEvaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluate(context, evaluatorFactory);
	}

	public List<Expression> getSubExpressions();
}
