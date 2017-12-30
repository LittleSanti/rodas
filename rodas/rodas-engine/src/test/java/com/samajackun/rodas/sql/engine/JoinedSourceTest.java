package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.eval.MyContext;
import com.samajackun.rodas.sql.model.BooleanExpression;
import com.samajackun.rodas.sql.model.Engine;
import com.samajackun.rodas.sql.model.EngineException;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.Source;
import com.samajackun.rodas.sql.model.TableSource;

public class JoinedSourceTest
{
	@Test
	public void test()
	{
		Source left=new TableSource("city");
		Source right=new TableSource("country");
		BooleanExpression condition=null;
		EvaluatorFactory evaluatorFactory=null;
		JoinedSource joinedSource=new JoinedSource(left, right, condition, evaluatorFactory);
		Engine engine=new MyEngine();
		MyProvider provider=new MyProvider();
		Context context=new MyContext();
		try
		{
			joinedSource.execute(engine, provider, context);
		}
		catch (EngineException | EvaluationException | ProviderException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
