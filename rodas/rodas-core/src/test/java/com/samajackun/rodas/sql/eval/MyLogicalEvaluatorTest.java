package com.samajackun.rodas.sql.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.sql.model.BooleanConstantExpression;
import com.samajackun.rodas.sql.model.NotExpression;
import com.samajackun.rodas.sql.model.TestUtils;

public class MyLogicalEvaluatorTest
{
	private final MyEvaluatorFactory myEvaluatorFactory=new MyEvaluatorFactory();

	@Test
	public void notBooleanTrue()
	{
		Context context=TestUtils.createContext();
		NotExpression expression=new NotExpression("not", BooleanConstantExpression.createTrueConstrantExpression("true"));
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

}
