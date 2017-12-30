package com.samajackun.rodas.sql.model;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class ListConstantExpression extends ConstantExpression
{
	private final List<ConstantExpression> list=new ArrayList<ConstantExpression>();

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
		int i=0;
		Object[] result=new Object[this.list.size()];
		for (ConstantExpression item : this.list)
		{
			result[i++]=item.evaluate(context, evaluatorFactory);
		}
		return result;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory) throws MetadataException
	{
		if (this.list.size() == 1)
		{
			return this.list.get(0).getDatatype(context, evaluatorFactory);
		}
		throw new ExpressionWithTooManyColumnsException(this);
	}
}
