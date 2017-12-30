package com.samajackun.rodas.sql.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.sql.model.FunctionExpression;
import com.samajackun.rodas.sql.model.NumericConstantExpression;
import com.samajackun.rodas.sql.model.TextConstantExpression;

public class MyFunctionEvaluatorTest
{
	private final MyEvaluatorFactory myEvaluatorFactory=new MyEvaluatorFactory();

	@Test
	public void minWithTwoIntArguments()
	{
		Context context=new MyContext();
		FunctionExpression expression=new FunctionExpression("min");
		expression.getArguments().add(new NumericConstantExpression("120", 120));
		expression.getArguments().add(new NumericConstantExpression("130", 130));
		try
		{
			assertEquals(120, expression.evaluate(context, this.myEvaluatorFactory));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void minWithTwoStringArguments()
	{
		Context context=new MyContext();
		FunctionExpression expression=new FunctionExpression("min");
		expression.getArguments().add(new TextConstantExpression("enero"));
		expression.getArguments().add(new TextConstantExpression("febrero"));
		try
		{
			assertEquals("enero", expression.evaluate(context, this.myEvaluatorFactory));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void maxWithTwoStringArguments()
	{
		Context context=new MyContext();
		FunctionExpression expression=new FunctionExpression("max");
		expression.getArguments().add(new TextConstantExpression("enero"));
		expression.getArguments().add(new TextConstantExpression("febrero"));
		try
		{
			assertEquals("febrero", expression.evaluate(context, this.myEvaluatorFactory));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void maxWithTwoIntArguments()
	{
		Context context=new MyContext();
		FunctionExpression expression=new FunctionExpression("max");
		expression.getArguments().add(new NumericConstantExpression("120", 120));
		expression.getArguments().add(new NumericConstantExpression("130", 130));
		try
		{
			assertEquals(130, expression.evaluate(context, this.myEvaluatorFactory));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void minWithOneArguments()
	{
		Context context=new MyContext();
		FunctionExpression expression=new FunctionExpression("min");
		expression.getArguments().add(new NumericConstantExpression("120", 120));
		try
		{
			expression.evaluate(context, this.myEvaluatorFactory);
			fail("Expected EvaluationException");
		}
		catch (EvaluationException e)
		{
		}
	}
}
