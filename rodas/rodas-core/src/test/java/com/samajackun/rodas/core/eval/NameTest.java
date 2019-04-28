package com.samajackun.rodas.core.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class NameTest
{
	@Test
	public void onlyBase()
	{
		Name name1=Name.instanceOf("winter");
		assertNull(name1.getPrefix());
		assertFalse(name1.hasPrefix());
		assertEquals("winter", name1.getBase().asString());
		assertEquals("winter", name1.asString());
		Name name2=Name.instanceOf("winter");
		assertEquals(name1, name2);
		Name name3=Name.instanceOf("spring");
		assertNotEquals(name3, name1);
		Map<Name, Integer> map=new HashMap<>();
		map.put(name1, 120);
		assertEquals(120, map.get(name2).intValue());
	}

	@Test
	public void prefixAndBase()
	{
		Name child1=Name.instanceOf("winter", "january");
		assertTrue(child1.hasPrefix());
		Name child2=Name.instanceOf("winter", "january");
		assertEquals("winter.january", child1.asString());
		Map<Name, Integer> map=new HashMap<>();
		map.put(child1, 120);
		assertEquals(120, map.get(child2).intValue());
	}
}
