package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.sql.eval.ColumnNotFoundException;
import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.DefaultContext;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.EvaluatorFactory;
import com.samajackun.rodas.sql.eval.MyEvaluatorFactory;
import com.samajackun.rodas.sql.eval.PrefixNotFoundException;
import com.samajackun.rodas.sql.model.BooleanExpression;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.CursorException;
import com.samajackun.rodas.sql.model.Engine;
import com.samajackun.rodas.sql.model.EngineException;
import com.samajackun.rodas.sql.model.EqualsExpression;
import com.samajackun.rodas.sql.model.IdentifierExpression;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.Source;
import com.samajackun.rodas.sql.model.TableSource;

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
		EvaluatorFactory evaluatorFactory=new MyEvaluatorFactory();
		JoinedSource joinedSource=new JoinedSource(left, right, condition, evaluatorFactory);
		Engine engine=new MyEngine();
		MyProvider provider=new MyProvider();
		Map<String, Cursor> cursors=new HashMap<>();
		// cursors.put("x1", left);
		// cursors.put("x2", right);
		Context context=new DefaultContext(cursors);
		// context.bindPrivateColumn(null, "idCity");
		// context.bindPrivateColumn(null, "idCountry");
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
