package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.model.CrossSource;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableSource;

public class CrossSourceTest
{
	@Test
	public void singleSource()
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Source source=new TableSource("country");
		MyOpenContext context=new MyOpenContext();
		context.setProvider(new MyProvider());
		Cursor cursor=source.execute(new MyEngine(), context);
		RowData row=cursor.getRowData();
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("spain", row.get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("portugal", row.get(1));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("italy", row.get(1));
		assertFalse(cursor.hasNext());
	}

	@Test
	public void crossSource()
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Source source1=new TableSource("country");
		Source source2=new TableSource("city");
		List<Source> sources=Arrays.asList(source1, source2);
		CrossSource crossSource=new CrossSource(sources);
		MyOpenContext context=new MyOpenContext();
		context.setProvider(new MyProvider());
		Cursor cursor=crossSource.execute(new MyEngine(), context);
		// while (cursor.hasNext())
		// {
		// cursor.next();
		// RowData row=cursor.getRowData();
		// // System.out.println(row.get(0) + "," + row.get(1) + ", " + row.get(2) + ", " + row.get(3) + ", " + row.get(4)+ ", " + row.get(5));
		// System.out.println("cursor.next();");
		// // System.out.println("row=cursor.getRowData();");
		// System.out.println("assertEquals(\"" + row.get(1) + "\", row.get(1));");
		// System.out.println("assertEquals(\"" + row.get(5) + "\", row.get(5));");
		// }
		RowData row=cursor.getRowData();
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("spain", row.get(1));
		assertEquals("madrid", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("spain", row.get(1));
		assertEquals("bibo", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("spain", row.get(1));
		assertEquals("zevilla", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("spain", row.get(1));
		assertEquals("lisboa", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("spain", row.get(1));
		assertEquals("são vicente", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("portugal", row.get(1));
		assertEquals("madrid", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("portugal", row.get(1));
		assertEquals("bibo", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("portugal", row.get(1));
		assertEquals("zevilla", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("portugal", row.get(1));
		assertEquals("lisboa", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("portugal", row.get(1));
		assertEquals("são vicente", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("italy", row.get(1));
		assertEquals("madrid", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("italy", row.get(1));
		assertEquals("bibo", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("italy", row.get(1));
		assertEquals("zevilla", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("italy", row.get(1));
		assertEquals("lisboa", row.get(5));
		assertTrue(cursor.hasNext());
		cursor.next();
		assertEquals("italy", row.get(1));
		assertEquals("são vicente", row.get(5));
		assertFalse(cursor.hasNext());
	}
}
