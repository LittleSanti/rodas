package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ListConstantExpression extends ConstantExpression
{
	private final List<ConstantExpression> list=new ArrayList<>();

	public ListConstantExpression()
	{
		super("");
	}

	public void add(ConstantExpression item)
	{
		this.list.add(item);
	}

	@Override
	public Object evaluateOnce(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		List<Object> result=new ArrayList<>(this.list.size());
		for (ConstantExpression item : this.list)
		{
			result.add(item.evaluate(context, evaluatorFactory));
		}
		return result;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		if (this.list.size() == 1)
		{
			return this.list.get(0).getDatatype(context, evaluatorFactory);
		}
		throw new ExpressionWithTooManyColumnsException(this);
	}
}
