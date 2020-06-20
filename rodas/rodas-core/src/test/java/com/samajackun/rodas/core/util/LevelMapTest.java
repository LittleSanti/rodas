package com.samajackun.rodas.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class LevelMapTest
{
	private LevelMap<String, String> create()
	{
		return LevelMap.<String, String> createRoot();
	}

	@Test
	public void root()
	{
		LevelMap<String, String> level0=create();
		level0.put("day", "monday");
		assertFalse(level0.containsKey("month"));
		assertNull(level0.getAtCurrentLevel("month"));
		assertEquals("monday", level0.getAtCurrentLevel("day"));
		assertEquals("monday", level0.getAtCurrentOrPreviousLevel("day"));
		assertSame(level0, level0.getAncestor(0));
	}

	@Test
	public void levelOne()
	{
		LevelMap<String, String> level0=create();
		level0.put("day", "monday");
		level0.put("month", "january");
		LevelMap<String, String> level1=level0.push();
		level1.put("month", "february");
		level1.put("planet", "mars");

		assertEquals("february", level1.getAtCurrentLevel("month"));
		assertNull(level1.getAtCurrentLevel("day"));
		assertEquals("mars", level1.getAtCurrentLevel("planet"));
		assertNull(level1.getAtCurrentLevel("author"));

		assertEquals("february", level1.getAtCurrentOrPreviousLevel("month"));
		assertEquals("monday", level1.getAtCurrentOrPreviousLevel("day"));
		assertEquals("mars", level1.getAtCurrentOrPreviousLevel("planet"));
		assertNull(level1.getAtCurrentOrPreviousLevel("author"));

		assertSame(level0, level1.getAncestor(1));

		level1.pop(level1);
	}
}
