package com.samajackun.rodas.core.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.context.TestUtils;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.model.BooleanConstantExpression;
import com.samajackun.rodas.core.model.NotExpression;

public class MyLogicalEvaluatorTest
{
	private final DefaultEvaluatorFactory myEvaluatorFactory=new DefaultEvaluatorFactory();

	@Test
	public void notBooleanTrue()
	{
		Context context=TestUtils.createContext();
		NotExpression expression=new NotExpression("not", BooleanConstantExpression.createTrueConstrantExpression("true"));
		System.out.println(expression.toCode());
		try
		{
			assertEquals(false, expression.evaluate(context, this.myEvaluatorFactory));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void notBooleanFalse()
	{
		Context context=TestUtils.createContext();
		NotExpression expression=new NotExpression("not", BooleanConstantExpression.createFalseConstrantExpression("false"));
		try
		{
			assertEquals(true, expression.evaluate(context, this.myEvaluatorFactory));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
