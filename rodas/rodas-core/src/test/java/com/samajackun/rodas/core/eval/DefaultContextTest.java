package com.samajackun.rodas.core.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;

import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.model.AddExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.NumericConstantExpression;

public class DefaultContextTest
{
	@Test
	public void evaluate()
	{
		DefaultContext context=new DefaultContext();
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		context.setEvaluatorFactory(evaluatorFactory);
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		Name name=Name.instanceOf("id");
		Expression expression=new AddExpression("+", new NumericConstantExpression("1", 1L), new IdentifierExpression(name.asString()));
		expression=Mockito.spy(expression);

		// 1st iteration:
		context.getVariablesManager().peekLocalContext().set(name, 100L);
		try
		{
			Object value1=context.evaluate(expression, evaluatorFactory);
			assertEquals(101L, value1);
			Mockito.verify(expression, Mockito.times(1)).evaluate(context, evaluatorFactory);
			Object value2=context.evaluate(expression, evaluatorFactory);
			Mockito.verify(expression, Mockito.times(1)).evaluate(context, evaluatorFactory);
			assertEquals(value1, value2);
		}
		catch (EvaluationException e)
		{
			fail(e.toString());
		}
	}

}
