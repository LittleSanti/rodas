package com.samajackun.rodas.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import com.samajackun.rodas.core.eval.ColumnNotFoundException;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.MapList;
import com.samajackun.rodas.core.eval.NameAlreadyBoundException;
import com.samajackun.rodas.core.eval.NameNotBoundException;
import com.samajackun.rodas.core.eval.PrefixNotFoundException;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableSource;

@Ignore("API obsoleto")
public class DefaultBuildingContextTest
{
	@Test
	public void toExecutionContext()
	{
		Provider provider=new MyProvider();
		MapList<String, Source> sources=new MapList<>();
		DefaultBuildingContext buildingContext=new DefaultBuildingContext(provider, sources);
		try
		{
			Source source1=new TableSource("country");
			buildingContext.addSource("country", source1);
			buildingContext.bindPublicColumn("country", "idCountry");
			buildingContext.bindPublicColumn("country", "name");
			MapList<String, Cursor> cursorMap=new MapList<>();
			Cursor cursor1=TestUtils.createCursor("month");
			cursorMap.put("country", cursor1);
			cursor1.next();
			Context executionContext=buildingContext.toExecutionContext(cursorMap);
			assertEquals(1, executionContext.getColumnByName("country.idCountry"));
			assertEquals("enero", executionContext.getColumnByName("country.name"));
		}
		catch (NameAlreadyBoundException | ColumnNotFoundException | PrefixNotFoundException | ProviderException | NameNotBoundException | CursorException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void fork()
	{
		Provider provider=new MyProvider();
		MapList<String, Source> sources=new MapList<>();
		DefaultBuildingContext buildingContext=new DefaultBuildingContext(provider, sources);
		try
		{
			Source source1=new TableSource("country");
			buildingContext.addSource("country", source1);
			buildingContext.bindPublicColumn("country", "idCountry");
			buildingContext.bindPublicColumn("country", "name");
			MapList<String, Cursor> cursorMap=new MapList<>();
			Cursor cursor1=TestUtils.createCursor("month");
			cursorMap.put("country", cursor1);
			cursor1.next();
			Context executionContext=buildingContext.toExecutionContext(cursorMap);
			assertEquals(1, executionContext.getColumnByName("country.idCountry"));
			assertEquals("enero", executionContext.getColumnByName("country.name"));
		}
		catch (NameAlreadyBoundException | ColumnNotFoundException | PrefixNotFoundException | ProviderException | NameNotBoundException | CursorException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
