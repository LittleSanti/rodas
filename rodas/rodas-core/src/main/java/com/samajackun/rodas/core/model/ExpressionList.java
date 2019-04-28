package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ExpressionList implements Expression
{
	private final List<Expression> expressions;

	public ExpressionList()
	{
		expressions=new ArrayList<Expression>();
	}

	public ExpressionList(int size)
	{
		expressions=new ArrayList<Expression>(size);
	}

	public List<Expression> getExpressions()
	{
		return expressions;
	}

	public void add(Expression expression)
	{
		expressions.add(expression);
	}

	@Override
	public String toCode()
	{
		String s="";
		for (Expression e : expressions)
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
		ExpressionList reduced=new ExpressionList(expressions.size());
		for (Expression src : expressions)
		{
			reduced.add(src.reduce(evaluatorFactory));
		}
		return reduced;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.ARRAY;
	}
}
