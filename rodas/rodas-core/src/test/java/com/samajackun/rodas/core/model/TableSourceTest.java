package com.samajackun.rodas.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.TableSource;

public class TableSourceTest
{
	@Test
	public void getColumnNames()
	{
		TableSource source=new TableSource("country");
		MyProvider provider=new MyProvider();
		try
		{
			List<String> columnNames=source.getColumnNames(provider);
			assertEquals("idCountry", columnNames.get(0));
			assertEquals("name", columnNames.get(1));
			assertEquals("area", columnNames.get(2));
		}
		catch (ProviderException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void getColumnNamesMap()
	{
		TableSource source=new TableSource("country");
		MyProvider provider=new MyProvider();
		try
		{
			Map<String, Integer> map=source.getColumnNamesMap(provider);
			System.out.println("map=" + map);
			assertEquals(3, map.size());
			assertEquals(Integer.valueOf(0), map.get("idCountry"));
			assertEquals(Integer.valueOf(1), map.get("name"));
			assertEquals(Integer.valueOf(2), map.get("area"));
		}
		catch (ProviderException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
