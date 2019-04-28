package com.samajackun.rodas.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.execution.Cursor;

public class TableSourceTest
{
	@Test
	public void getTable()
	{
		TableSource source=new TableSource("country");
		assertEquals("country", source.getTable());
		assertNull(source.getAlias());
	}

	@Test
	public void getAlias()
	{
		TableSource source=new TableSource("country");
		assertNull(source.getAlias());
	}

	@Test
	public void execute()
	{
		TableSource source=new TableSource("country");
		MyEngine engine=new MyEngine();
		MyOpenContext context=new MyOpenContext();
		Provider provider=new MyProvider();
		context.setProvider(provider);
		try
		{
			Cursor cursor=source.execute(engine, context);
			System.out.println(cursor.getColumnMap());
			assertEquals(3, cursor.getColumnMap().size());
			assertEquals(Integer.valueOf(0), cursor.getColumnMap().get("idCountry"));
			assertEquals(Integer.valueOf(1), cursor.getColumnMap().get("name"));
			assertEquals(Integer.valueOf(2), cursor.getColumnMap().get("area"));
		}
		catch (EngineException | EvaluationException | ProviderException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
