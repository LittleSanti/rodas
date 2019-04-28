package com.samajackun.rodas.core.model;

import org.junit.Test;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.execution.Cursor;

public class CrossSourceTest
{
	@Test
	public void singleSource()
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Source source1=new TableSource("country");
		MyOpenContext context=new MyOpenContext();
		context.setProvider(new MyProvider());
		Cursor cursor1=source1.execute(new MyEngine(), context);
		while (cursor1.hasNext())
		{
			cursor1.next();
			System.out.println(cursor1.getRowData().get(1));
		}
	}
}
