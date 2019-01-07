package com.samajackun.rodas.core.model;

public class BinaryExpressionsFactories
{
	private static final BinaryExpressionsFactories INSTANCE=new BinaryExpressionsFactories();

	public static BinaryExpressionsFactories getInstance()
	{
		return INSTANCE;
	}

	private BinaryExpressionsFactories()
	{
	}

	public IExpressionFactory createAndExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new AndExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createOrExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new OrExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createEqualsExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new EqualsExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createNotEqualsExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new NotEqualsExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createGreaterThanExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new GreaterThanExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createGreaterThanOrEqualsExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new GreaterThanOrEqualsExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createLowerThanExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new LowerThanExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createLowerThanOrEqualsExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new LowerThanOrEqualsExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createAddExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				Expression result=new AddExpression(operator, exp1, exp2);
				return result;
			}
		};
	}

	public IExpressionFactory createSubstractExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new SubstractExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createMultiplyExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new MultiplyExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createDivideExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new DivideExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createIsExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new IsExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createIsOfTypeExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new IsOfTypeExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createLikeExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new LikeExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createInExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new InExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createConcatExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return new ConcatExpression(operator, exp1, exp2);
			}
		};
	}

	public IExpressionFactory createIsNotExpressionFactory(String operator, IExpressionFactory expressionFactory)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return expressionFactory.create(new NotExpression(operator, exp2));
			}
		};
	}

	public IExpressionFactory createBetweenExpressionFactory(String operator, Expression exp1)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				if (exp2 instanceof AndExpression)
				{
					return new BetweenExpression(operator, exp1, (AndExpression)exp2);
				}
				else
				{
					throw new IllegalArgumentException("Expected AndExpression instead of " + exp2.getClass().getName());
				}
			}
		};
	}

	public IExpressionFactory createNotExpressionFactory(String operator)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp)
			{
				return new NotExpression(operator, exp);
			}
		};
	}

	public IExpressionFactory createNotExpressionFactory(String operator, IExpressionFactory expressionFactory)
	{
		return new IExpressionFactory()
		{
			@Override
			public Expression create(Expression exp2)
			{
				return expressionFactory.create(new NotExpression(operator, exp2));
			}
		};
	}
}
