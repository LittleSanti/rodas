package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.TableSource;

public class MyEngineTest
{
	@Test
	public void executeTable()
	{
		SqlEngine engine=new SqlEngine();
		TableSource source=new TableSource("country");
		Provider provider=new MyProvider();
		MyOpenContext context=new MyOpenContext();
		context.setProvider(provider);
		try
		{
			Cursor cursor=engine.execute(source, context);
			assertNotNull(cursor);
			assertEquals(3, cursor.getNumberOfColumns());
			assertTrue(cursor.hasNext());
			cursor.next();
			assertEquals(1, cursor.getRowData().get(0));
			assertEquals("spain", cursor.getRowData().get(1));
			assertEquals(121.1d, cursor.getRowData().get(2));
			assertTrue(cursor.hasNext());
			cursor.next();
			assertEquals(2, cursor.getRowData().get(0));
			assertEquals("portugal", cursor.getRowData().get(1));
			assertEquals(122.2d, cursor.getRowData().get(2));
			assertTrue(cursor.hasNext());
			cursor.next();
			assertEquals(3, cursor.getRowData().get(0));
			assertEquals("italy", cursor.getRowData().get(1));
			assertEquals(123.3d, cursor.getRowData().get(2));
			assertFalse(cursor.hasNext());
		}
		catch (EngineException | EvaluationException | ProviderException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
