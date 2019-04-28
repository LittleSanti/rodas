package com.samajackun.rodas.core.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.model.AddExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.Provider;

public class MyOpenContextTest
{
	private final Provider provider=new MyProvider();

	private final EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();

	private MyOpenContext createContext()
	{
		MyOpenContext context=new MyOpenContext();
		context.setProvider(this.provider);
		return context;
	}

	@Test
	public void evaluateIdentifier()
	{
		MyOpenContext context=createContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().peekLocalContext().set(Name.instanceOf("mes"), "enero");
		Expression expression=new IdentifierExpression("mes");
		try
		{
			Object value=context.evaluate(expression, this.evaluatorFactory);
			assertEquals("enero", value);
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateAdd()
	{
		MyOpenContext context=createContext();
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		context.getVariablesManager().peekLocalContext().set(Name.instanceOf("a"), 1);
		context.getVariablesManager().peekLocalContext().set(Name.instanceOf("b"), 2);
		Expression expression=new AddExpression("+", new IdentifierExpression("a"), new IdentifierExpression("b"));
		try
		{
			Object value=context.evaluate(expression, this.evaluatorFactory);
			assertEquals(3, value);
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
