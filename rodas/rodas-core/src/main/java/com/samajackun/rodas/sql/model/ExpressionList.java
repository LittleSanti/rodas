package com.samajackun.rodas.sql.model;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class ExpressionList implements Expression
{
	private final List<Expression> expressions;

	public ExpressionList()
	{
		this.expressions=new ArrayList<Expression>();
	}

	public ExpressionList(int size)
	{
		this.expressions=new ArrayList<Expression>(size);
	}

	public List<Expression> getExpressions()
	{
		return this.expressions;
	}

	public void add(Expression expression)
	{
		this.expressions.add(expression);
	}

	@Override
	public String toCode()
	{
		String s="";
		for (Expression e : this.expressions)
		{
			if (!s.isEmpty())
			{
				s+=",";
			}
			s+=e.toCode();
		}
		return s;
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getCollectionsEvaluator().evaluateList(context, getExpressions());
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		ExpressionList reduced=new ExpressionList(this.expressions.size());
		for (Expression src : this.expressions)
		{
			reduced.add(src.reduce(evaluatorFactory));
		}
		return reduced;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		if (this.expressions.size() == 1)
		{
			return this.expressions.get(0).getDatatype(context, evaluatorFactory);
		}
		throw new ExpressionWithTooManyColumnsException(this);
	}
}
