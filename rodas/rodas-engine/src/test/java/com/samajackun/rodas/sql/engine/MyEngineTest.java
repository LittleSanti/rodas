package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.EngineException;
import com.samajackun.rodas.sql.model.Provider;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.TableSource;

public class MyEngineTest
{
	@Test
	public void executeTable()
	{
		MyEngine engine=new MyEngine();
		TableSource source=new TableSource("country");
		Provider provider=new MyProvider();
		Context context=null;
		try
		{
			Cursor cursor=engine.execute(source, provider, context);
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
