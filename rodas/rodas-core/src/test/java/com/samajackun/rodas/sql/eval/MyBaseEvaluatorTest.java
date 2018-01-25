package com.samajackun.rodas.sql.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.sql.model.BooleanConstantExpression;
import com.samajackun.rodas.sql.model.IdentifierExpression;
import com.samajackun.rodas.sql.model.NullConstantExpression;
import com.samajackun.rodas.sql.model.NumericConstantExpression;
import com.samajackun.rodas.sql.model.TestUtils;
import com.samajackun.rodas.sql.model.TextConstantExpression;

public class MyBaseEvaluatorTest
{
	private final MyEvaluatorFactory myEvaluatorFactory=new MyEvaluatorFactory();

	@Test
	public void evaluateNumericExpression()
	{
		Context context=TestUtils.createContext();
		NumericConstantExpression expression=new NumericConstantExpression("120", 120);
		try
		{
			assertEquals(120, this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateTextExpression()
	{
		Context context=TestUtils.createContext();
		TextConstantExpression expression=new TextConstantExpression("enero");
		try
		{
			assertEquals("enero", this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateBooleanTrueExpression()
	{
		Context context=TestUtils.createContext();
		BooleanConstantExpression expression=BooleanConstantExpression.createTrueConstrantExpression("true");
		try
		{
			assertEquals(true, this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateBooleanFalseExpression()
	{
		Context context=TestUtils.createContext();
		BooleanConstantExpression expression=BooleanConstantExpression.createFalseConstrantExpression("false");
		try
		{
			assertEquals(false, this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateExistingIdentifierExpression()
	{
		Context context=TestUtils.createContext();
		IdentifierExpression expression=new IdentifierExpression("name");
		try
		{
			assertEquals("enero", this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateUnexistingIdentifierExpression()
	{
		Context context=TestUtils.createContext();
		IdentifierExpression expression=new IdentifierExpression("wrong");
		try
		{
			this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression);
			fail("Expected NameNotBoundException");
		}
		catch (NameNotBoundException e)
		{
			assertNull(e.getContext());
			assertEquals("wrong", e.getName());
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateNullConstantExpression()
	{
		Context context=TestUtils.createContext();
		NullConstantExpression expression=new NullConstantExpression("xnul");
		try
		{
			assertNull(this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
