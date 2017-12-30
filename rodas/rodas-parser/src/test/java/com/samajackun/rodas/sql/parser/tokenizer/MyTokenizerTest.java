package com.samajackun.rodas.sql.parser.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MyTokenizerTest
{
	@Test
	public void empty()
	{
		String src="";
		MySettings settings=new MySettings();
		try
		{
			MyTokenizer tokenizer=new MyTokenizer(src, settings);
			assertFalse(tokenizer.hasMoreTokens());
			tokenizer.nextToken();
			fail("Expected TokenizerException");
		}
		catch (TokenizerException e)
		{
			// OK.
		}
	}

	@Test
	public void oneCharacter()
	{
		String src="a";
		try
		{
			MySettings settings=new MySettings();
			MyTokenizer tokenizer=new MyTokenizer(src, settings);
			assertTrue(tokenizer.hasMoreTokens());
			assertEquals("a", tokenizer.nextToken());
			assertFalse(tokenizer.hasMoreTokens());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void twoCharacters()
	{
		String src="ab";
		try
		{
			MySettings settings=new MySettings();
			MyTokenizer tokenizer=new MyTokenizer(src, settings);

			assertTrue(tokenizer.hasMoreTokens());
			assertEquals("a", tokenizer.nextToken());

			assertTrue(tokenizer.hasMoreTokens());
			assertEquals("b", tokenizer.nextToken());
			assertFalse(tokenizer.hasMoreTokens());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void threeCharacters()
	{
		String src="abc";
		try
		{
			MySettings settings=new MySettings();
			MyTokenizer tokenizer=new MyTokenizer(src, settings);

			assertTrue(tokenizer.hasMoreTokens());
			assertEquals("a", tokenizer.nextToken());

			assertTrue(tokenizer.hasMoreTokens());
			assertEquals("b", tokenizer.nextToken());

			assertTrue(tokenizer.hasMoreTokens());
			assertEquals("c", tokenizer.nextToken());

			assertFalse(tokenizer.hasMoreTokens());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
