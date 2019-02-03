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
		Name name1=new Name("winter");
		assertNull(name1.getPrefix());
		assertFalse(name1.hasPrefix());
		assertEquals("winter", name1.getBase());
		assertEquals("winter", name1.toString());
		Name name2=new Name("winter");
		assertEquals(name1, name2);
		Name name3=new Name("spring");
		assertNotEquals(name3, name1);
		Map<Name, Integer> map=new HashMap<>();
		map.put(name1, 120);
		assertEquals(120, map.get(name2).intValue());
	}

	@Test
	public void prefixAndBase()
	{
		Name parent1=new Name("winter");
		Name parent2=new Name("winter");
		Name child1=new Name(parent1, "january");
		assertTrue(child1.hasPrefix());
		Name child2=new Name(parent2, "january");
		assertEquals("winter.january", child1.toString());
		Map<Name, Integer> map=new HashMap<>();
		map.put(child1, 120);
		assertEquals(120, map.get(child2).intValue());
	}
}
