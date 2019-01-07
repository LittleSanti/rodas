package com.samajackun.rodas.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.context.TestUtils;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.model.BooleanConstantExpression;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.MetadataException;

public class BooleanConstantExpressionTest
{
	@Test
	public void test()
	{
		BooleanConstantExpression expression=new BooleanConstantExpression("true", true);
		Context context=TestUtils.createContext();
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
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
