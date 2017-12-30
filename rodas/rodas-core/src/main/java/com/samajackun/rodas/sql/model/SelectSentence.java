package com.samajackun.rodas.sql.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;

public class SelectSentence implements Source, Sentence, Expression
{
	private final List<WithDeclaration> withDeclarations=new ArrayList<WithDeclaration>();

	private final List<AliasedExpression> selectExpressions=new ArrayList<AliasedExpression>();

	private final Map<String, AliasedExpression> selectExpressionsMap=new LinkedHashMap<String, AliasedExpression>();

	private final List<String> columnNames=new ArrayList<>();

	private final List<String> options=new ArrayList<String>();

	private final List<Source> sourceDeclarations=new ArrayList<Source>();

	private Expression whereExpression;

	private final List<Expression> groupExpressions=new ArrayList<Expression>();

	private Expression havingExpression;

	private final List<Expression> orderExpressions=new ArrayList<Expression>();

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

	public List<Source> getSourceDeclarations()
	{
		return this.sourceDeclarations;
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

	public List<Expression> getOrderExpressions()
	{
		return this.orderExpressions;
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
		codeToBuffer("FROM", this.sourceDeclarations, stb);

		// WHERE
		codeToBuffer("WHERE", this.whereExpression, stb);

		// GROUP
		codeToBuffer("GROUP BY", this.groupExpressions, stb);

		// HAVING
		codeToBuffer("HAVING", this.havingExpression, stb);

		// ORDER
		codeToBuffer("ORDER BY", this.orderExpressions, stb);
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
	public boolean hasColumn(String column, Provider provider)
		throws ProviderException
	{
		return this.selectExpressionsMap.containsKey(column);
	}

	@Override
	public Cursor execute(Engine engine, Provider provider, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return engine.execute(this, provider, context);
		// return new Cursor(provider.getColumnNamesFromTable(this.table), provider.getTableData(this.table));
	}

	@Override
	public List<String> getColumnNames(Provider provider)
		throws ProviderException
	{
		// return this.selectExpressions.stream().map(e -> e.getAlias()).collect(Collectors.toCollection(ArrayList::new));
		return this.columnNames;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		// TODO Hay que ejecutar la sentencia, si no hay tablas involucradas. Pero para eso necesito un Engine.
		throw new UnsupportedOperationException();
	}

	@Override
	public ColumnMetadata getColumnMetadata(int column, Provider provider, Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException,
		ProviderException
	{
		AliasedExpression aliasedExpression=this.selectExpressions.get(column);
		Datatype datatype=aliasedExpression.getExpression().getDatatype(context, evaluatorFactory);
		ColumnMetadata columnMetadata=new ColumnMetadata(aliasedExpression.getAlias(), datatype, true);
		return columnMetadata;
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
}
