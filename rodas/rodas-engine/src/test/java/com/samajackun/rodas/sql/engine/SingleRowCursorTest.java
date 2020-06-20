package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ProviderException;

public class SingleRowCursorTest
{

	@Test
	public void test()
		throws ProviderException
	{
		Cursor cursor=new SingleRowCursor();
		try
		{
			assertTrue(cursor.hasNext());
			cursor.next();
			assertFalse(cursor.hasNext());
		}
		catch (CursorException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
