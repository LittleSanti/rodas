package com.samajackun.rodas.core.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.context.TestUtils;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.model.FunctionExpression;
import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.rodas.core.model.TextConstantExpression;

public class MyFunctionEvaluatorTest
{
	private final DefaultEvaluatorFactory myEvaluatorFactory=new DefaultEvaluatorFactory();

	@Test
	public void minWithTwoIntArguments()
	{
		Context context=TestUtils.createContext();
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
		Context context=TestUtils.createContext();
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
		Context context=TestUtils.createContext();
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
		Context context=TestUtils.createContext();
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
		Context context=TestUtils.createContext();
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
