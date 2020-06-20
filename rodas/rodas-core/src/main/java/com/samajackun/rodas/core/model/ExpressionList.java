package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;

public class ExpressionList implements Expression
{
	private static final long serialVersionUID=-649422300990487275L;

	private final List<Expression> expressions;

	public ExpressionList()
	{
		this.expressions=new ArrayList<>();
	}

	public ExpressionList(int size)
	{
		this.expressions=new ArrayList<>(size);
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
		return Datatype.ARRAY;
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return this.expressions;
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.expressions == null)
			? 0
			: this.expressions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		ExpressionList other=(ExpressionList)obj;
		if (this.expressions == null)
		{
			Iterator<Expression> iterator1=this.expressions.iterator();
			Iterator<Expression> iterator2=other.expressions.iterator();
			boolean x=true;
			while (iterator1.hasNext() && iterator2.hasNext() && x)
			{
				Expression expression1=iterator1.next();
				Expression expression2=iterator2.next();
				x=expression1.equals(expression2);
			}
			if (x)
			{
				x=iterator1.hasNext() == iterator2.hasNext();
			}
			return x;
		}
		else if (!this.expressions.equals(other.expressions))
		{
			return false;
		}
		return true;
	}
}
