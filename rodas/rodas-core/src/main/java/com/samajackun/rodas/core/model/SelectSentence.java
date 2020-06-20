package com.samajackun.rodas.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;

public class SelectSentence implements Source, Sentence, Expression
{
	private static final long serialVersionUID=5862006849796771401L;

	private final List<WithDeclaration> withDeclarations=new ArrayList<>();

	private final List<AliasedExpression> selectExpressions=new ArrayList<>();

	private final Map<String, AliasedExpression> selectExpressionsMap=new LinkedHashMap<>();

	private final List<String> columnNames=new ArrayList<>();

	private final List<String> options=new ArrayList<>();

	private Source source;

	private Expression whereExpression;

	private final List<Expression> groupExpressions=new ArrayList<>();

	private Expression havingExpression;

	private final List<OrderClause> orderClauses=new ArrayList<>();

	public List<WithDeclaration> getWithDeclarations()
	{
		return this.withDeclarations;
	}

	public List<AliasedExpression> getSelectExpressions()
	{
		return this.selectExpressions;
	}

	public Map<String, AliasedExpression> getSelectExpressionsMap()
	{
		return this.selectExpressionsMap;
	}

	public void addSelectExpressions(Collection<AliasedExpression> expressions)
	{
		this.selectExpressions.addAll(expressions);
		for (AliasedExpression ae : expressions)
		{
			String alias=ae.getAlias() == null
				? createAlias()
				: ae.getAlias();
			this.selectExpressionsMap.put(alias, ae);
			this.columnNames.add(alias);
		}
	}

	private String createAlias()
	{
		return "column_" + this.selectExpressionsMap.size();
	}

	public List<String> getOptions()
	{
		return this.options;
	}

	public Source getSource()
	{
		return this.source;
	}

	public void setSource(Source source)
	{
		this.source=source;
	}

	public Expression getWhereExpression()
	{
		return this.whereExpression;
	}

	public void setWhereExpression(Expression whereExpression)
	{
		this.whereExpression=whereExpression;
	}

	public List<Expression> getGroupExpressions()
	{
		return this.groupExpressions;
	}

	public Expression getHavingExpression()
	{
		return this.havingExpression;
	}

	public void setHavingExpression(Expression havingExpression)
	{
		this.havingExpression=havingExpression;
	}

	public List<OrderClause> getOrderClauses()
	{
		return this.orderClauses;
	}

	@Override
	public String toCode()
	{
		StringBuilder stb=new StringBuilder(4096);

		// WITH
		codeToBuffer("WITH", this.withDeclarations, stb);

		// SELECT
		codeToBuffer("SELECT", this.selectExpressions, stb);

		// FROM
		codeToBuffer("FROM", this.source, stb);

		// WHERE
		codeToBuffer("WHERE", this.whereExpression, stb);

		// GROUP
		codeToBuffer("GROUP BY", this.groupExpressions, stb);

		// HAVING
		codeToBuffer("HAVING", this.havingExpression, stb);

		// ORDER
		codeToBuffer("ORDER BY", this.orderClauses, stb);
		return stb.toString();
	}

	public String toPatternCode()
	{
		// Igual que toCode, pero eliminando comentarios y reemplazando parámetros nominales por '?'.
		// return toCode(WhitespaceBehaviour.IGNORE, CommentsBehaviour.IGNORE);
		return null;
	}

	private void codeToBuffer(String head, Codeable codeable, StringBuilder stb)
	{
		if (codeable != null)
		{
			if (stb.length() > 0)
			{
				stb.append(' ');
			}
			stb.append(head).append(' ');
			stb.append(codeable.toCode());
		}
	}

	private void codeToBuffer(String head, List<? extends Codeable> list, StringBuilder stb)
	{
		if (!list.isEmpty())
		{
			if (stb.length() > 0)
			{
				stb.append(' ');
			}
			stb.append(head).append(' ');
			for (Iterator<? extends Codeable> iterator=list.iterator(); iterator.hasNext();)
			{
				Codeable codeable=iterator.next();
				stb.append(codeable.toCode());
				if (iterator.hasNext())
				{
					stb.append(',');
				}
			}
		}
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getQueryEvaluator().evaluate(context, this);
	}

	@Override
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return engine.execute(this, context);
		// return new Cursor(provider.getColumnNamesFromTable(this.table), provider.getTableData(this.table));
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		if (this.selectExpressions.size() == 1)
		{
			return this.selectExpressions.get(0).getExpression().getDatatype(context, evaluatorFactory);
		}
		throw new ExpressionWithTooManyColumnsException(this);
	}

	// @Override
	// public boolean hasColumn(String column)
	// throws ProviderException
	// {
	// return this.selectExpressionsMap.containsKey(column);
	// }
	//
	// @Override
	// public List<String> getColumnNames()
	// throws ProviderException
	// {
	// // return this.selectExpressions.stream().map(e -> e.getAlias()).collect(Collectors.toCollection(ArrayList::new));
	// return this.columnNames;
	// }
	//
	//
	// @Override
	// public ColumnMetadata getColumnMetadata(int column, Context context, EvaluatorFactory evaluatorFactory)
	// throws MetadataException,
	// ProviderException
	// {
	// AliasedExpression aliasedExpression=this.selectExpressions.get(column);
	// Datatype datatype=aliasedExpression.getExpression().getDatatype(context, evaluatorFactory);
	// ColumnMetadata columnMetadata=new ColumnMetadata(aliasedExpression.getAlias(), datatype, true);
	// return columnMetadata;
	// }

	@Override
	public List<Expression> getSubExpressions()
	{
		throw new IllegalArgumentException("Sunselects not allowed here");
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.columnNames == null)
			? 0
			: this.columnNames.hashCode());
		result=prime * result + ((this.groupExpressions == null)
			? 0
			: this.groupExpressions.hashCode());
		result=prime * result + ((this.havingExpression == null)
			? 0
			: this.havingExpression.hashCode());
		result=prime * result + ((this.options == null)
			? 0
			: this.options.hashCode());
		result=prime * result + ((this.orderClauses == null)
			? 0
			: this.orderClauses.hashCode());
		result=prime * result + ((this.selectExpressions == null)
			? 0
			: this.selectExpressions.hashCode());
		result=prime * result + ((this.selectExpressionsMap == null)
			? 0
			: this.selectExpressionsMap.hashCode());
		result=prime * result + ((this.source == null)
			? 0
			: this.source.hashCode());
		result=prime * result + ((this.whereExpression == null)
			? 0
			: this.whereExpression.hashCode());
		result=prime * result + ((this.withDeclarations == null)
			? 0
			: this.withDeclarations.hashCode());
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
		SelectSentence other=(SelectSentence)obj;
		if (this.columnNames == null)
		{
			if (other.columnNames != null)
			{
				return false;
			}
		}
		else if (!this.columnNames.equals(other.columnNames))
		{
			return false;
		}
		if (this.groupExpressions == null)
		{
			if (other.groupExpressions != null)
			{
				return false;
			}
		}
		else if (!this.groupExpressions.equals(other.groupExpressions))
		{
			return false;
		}
		if (this.havingExpression == null)
		{
			if (other.havingExpression != null)
			{
				return false;
			}
		}
		else if (!this.havingExpression.equals(other.havingExpression))
		{
			return false;
		}
		if (this.options == null)
		{
			if (other.options != null)
			{
				return false;
			}
		}
		else if (!this.options.equals(other.options))
		{
			return false;
		}
		if (this.orderClauses == null)
		{
			if (other.orderClauses != null)
			{
				return false;
			}
		}
		else if (!this.orderClauses.equals(other.orderClauses))
		{
			return false;
		}
		if (this.selectExpressions == null)
		{
			if (other.selectExpressions != null)
			{
				return false;
			}
		}
		else if (!this.selectExpressions.equals(other.selectExpressions))
		{
			return false;
		}
		if (this.selectExpressionsMap == null)
		{
			if (other.selectExpressionsMap != null)
			{
				return false;
			}
		}
		else if (!this.selectExpressionsMap.equals(other.selectExpressionsMap))
		{
			return false;
		}
		if (this.source == null)
		{
			if (other.source != null)
			{
				return false;
			}
		}
		else if (!this.source.equals(other.source))
		{
			return false;
		}
		if (this.whereExpression == null)
		{
			if (other.whereExpression != null)
			{
				return false;
			}
		}
		else if (!this.whereExpression.equals(other.whereExpression))
		{
			return false;
		}
		if (this.withDeclarations == null)
		{
			if (other.withDeclarations != null)
			{
				return false;
			}
		}
		else if (!this.withDeclarations.equals(other.withDeclarations))
		{
			return false;
		}
		return true;
	}

}
