package com.samajackun.rodas.sql.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.sql.context.TestUtils;
import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.eval.MyEvaluatorFactory;

public class BooleanConstantExpressionTest
{
	@Test
	public void test()
	{
		BooleanConstantExpression expression=new BooleanConstantExpression("true", true);
		Context context=TestUtils.createContext();
		EvaluatorFactory evaluatorFactory=new MyEvaluatorFactory();
		try
		{
			assertEquals(Datatype.BOOLEAN, expression.getDatatype(context, evaluatorFactory));
		}
		catch (MetadataException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
