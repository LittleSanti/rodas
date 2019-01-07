package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.context.DefaultBuildingContext;
import com.samajackun.rodas.core.eval.ColumnNotFoundException;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.PrefixNotFoundException;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.model.BooleanExpression;
import com.samajackun.rodas.core.model.CursorException;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.EqualsExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableSource;

public class JoinedSourceTest
{
	@Test
	public void test()
		throws CursorException,
		ColumnNotFoundException,
		PrefixNotFoundException
	{
		Source left=new TableSource("city");
		Source right=new TableSource("country");
		BooleanExpression condition=new EqualsExpression("=", new IdentifierExpression("idCity"), new IdentifierExpression("idCountry"));
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		JoinedSource joinedSource=new JoinedSource(left, right, condition, evaluatorFactory);
		Engine engine=new MyEngine();
		MyProvider provider=new MyProvider();
		Map<String, Source> cursors=new HashMap<>();
		cursors.put("x1", left);
		cursors.put("x2", right);
		Context context=new DefaultBuildingContext(cursors);
		context.bindPrivateColumn(null, "idCity");
		context.bindPrivateColumn(null, "idCountry");
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
