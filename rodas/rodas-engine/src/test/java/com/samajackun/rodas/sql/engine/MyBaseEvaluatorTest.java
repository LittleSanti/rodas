package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;

import org.junit.Test;

import com.samajackun.rodas.core.context.TestUtils;
import com.samajackun.rodas.core.eval.ColumnNotFoundException;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.DefaultContext;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.eval.VariablesManager;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.BooleanConstantExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.NamedParameterExpression;
import com.samajackun.rodas.core.model.NullConstantExpression;
import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.TextConstantExpression;

public class MyBaseEvaluatorTest
{
	private final DefaultEvaluatorFactory myEvaluatorFactory=new DefaultEvaluatorFactory();

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
		throws ProviderException
	{
		// Context context=TestUtils.createContext();
		DefaultContext context=new DefaultContext();
		Cursor cursor=TestUtils.createCursor("month");
		VariablesContext globalVariablesContext=new StrictVariablesContext();
		globalVariablesContext.set(Name.instanceOf("year"), 2019);
		VariablesManager variablesManager=new StrictVariablesManager(globalVariablesContext);
		context.setVariablesManager(variablesManager);
		variablesManager.pushLocalContext(new CursorVariablesContext(null, cursor));
		IdentifierExpression expression=new IdentifierExpression("name");
		try
		{
			cursor.next();
			assertEquals("enero", this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (EvaluationException | CursorException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			variablesManager.popLocalContext();
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
		catch (VariableNotFoundException e)
		{
			assertEquals("wrong", e.getName().asString());
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

	@Test
	public void evaluateIdentifierExpressionWithAlias()
		throws ProviderException
	{
		try
		{
			Cursor cursor1=TestUtils.createCursor("month");
			DefaultContext context=new DefaultContext();
			context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
			Map<String, Cursor> cursorMap=new HashMap<>();
			cursorMap.put("mes", cursor1);
			VariablesContext newVariablesContext=new CursorMapVariablesContext(context.getVariablesManager().peekLocalContext(), cursorMap);
			context.getVariablesManager().pushLocalContext(newVariablesContext);
			cursor1.next();
			IdentifierExpression expression=new IdentifierExpression("mes", "name");
			assertEquals("enero", this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (ColumnNotFoundException e)
		{
			throw new ServiceConfigurationError(e.toString(), e);
		}
		catch (EvaluationException | CursorException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateIdentifierExpressionAndIterate()
		throws ProviderException
	{
		try
		{
			Cursor cursor1=TestUtils.createCursor("month");
			DefaultContext context=new DefaultContext();
			VariablesManager variablesManager=new StrictVariablesManager(new CursorVariablesContext(null, cursor1));
			context.setVariablesManager(variablesManager);

			IdentifierExpression expression=new IdentifierExpression("name");
			cursor1.next();
			assertEquals("enero", this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
			cursor1.next();
			assertEquals("febrero", this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
			cursor1.next();
			assertEquals("marzo", this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (CursorException | ColumnNotFoundException e)
		{
			throw new ServiceConfigurationError(e.toString(), e);
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void evaluateExistingParameterExpression()
	{
		Context context=TestUtils.createContext();
		context.getVariablesManager().setGlobalVariable(Name.instanceOf("dia"), "lunes");
		NamedParameterExpression expression=new NamedParameterExpression("dia");
		try
		{
			assertEquals("lunes", this.myEvaluatorFactory.getBaseEvaluator().evaluate(context, expression));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
