package com.samajackun.rodas.sql.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.FunctionCallExpression;
import com.samajackun.rodas.core.model.SelectSentence;

public class GroupingValidator
{
	// Comprueba que se cumplan estas reglas:
	// En la SELECT sólo puede haber:
	// * expresiones constantes,
	// * expresiones con funciones de agregado
	// * expresiones sin funciones de agregado
	// -Las funciones de agregado sólo pueden ser: count, sum, max, min, avg.
	// -Las columnas referenciadas dentro de las expresiones que no estén incluídas en la cláusula GROUP BY han de estar dentro de alguna función de agregado.
	// -Las columnas referenciadas dentro de las expresiones sin funciones de agregado deben estar incluídas en la cláusula GROUP BY.

	private final Set<String> AGGREGATE_FUNCTIONS=new HashSet<>(Arrays.asList(new String[] {
		"avg",
		"max",
		"min",
		"count",
		"sum"
	}));

	public void validateGroupSelect(SelectSentence sentence)
		throws NotAGroupedException
	{
		validateSelectExpressions(sentence.getSelectExpressions(), sentence.getGroupExpressions());
	}

	private void validateSelectExpressions(List<AliasedExpression> selectExpressions, List<Expression> groupExpressions)
		throws NotAGroupedException
	{
		for (AliasedExpression selectExpression : selectExpressions)
		{
			validateSelectExpression(selectExpression.getExpression(), new HashSet<>(groupExpressions));
		}
	}

	private void validateSelectExpression(Expression selectExpression, Set<Expression> groupExpressions)
		throws NotAGroupedException
	{
		if (isConstant(selectExpression))
		{
			// OK. Válida en cualquier caso.
		}
		else if (isAggregateFunction(selectExpression))
		{
			// TODO
		}
		else
		{
			validateExpressionIsGrouped(selectExpression, groupExpressions);
			// List<Expression> subExpressions=selectExpression.getSubExpressions();
			// for (Expression subExpression : subExpressions)
			// {
			// validateExpressionIsGrouped(subExpression, groupExpressions);
			// }
		}
	}

	private boolean isAggregateFunction(Expression selectExpression)
	{
		boolean x;
		if (selectExpression instanceof FunctionCallExpression)
		{
			FunctionCallExpression functionCallExpression=(FunctionCallExpression)selectExpression;
			String name=functionCallExpression.getName().asString();
			x=this.AGGREGATE_FUNCTIONS.contains(name.toLowerCase());
		}
		else
		{
			x=false;
		}
		return x;
	}

	private void validateExpressionIsGrouped(Expression expression, Set<Expression> groupExpressions)
		throws NotAGroupedException
	{
		if (isConstant(expression))
		{
			// OK.
		}
		else if (groupExpressions.contains(expression))
		{
			// OK.
		}
		else
		{
			List<Expression> subExpressions=expression.getSubExpressions();
			if (subExpressions.isEmpty())
			{
				throw new NotAGroupedException(expression);
			}
			else
			{
				for (Expression subExpression : subExpressions)
				{
					validateExpressionIsGrouped(subExpression, groupExpressions);
				}
			}
		}
	}

	private boolean isConstant(Expression expression)
	{
		return expression instanceof ConstantExpression;
	}
}
