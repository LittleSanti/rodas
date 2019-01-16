package com.samajackun.rodas.parsing.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;

public class MyTokenizerTest
{
	@Test
	public void empty()
		throws IOException
	{
		String src="";
		MySettings settings=new MySettings();
		try
		{
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			assertNull(tokenizer.nextToken());
			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void oneCharacter()
		throws IOException
	{
		String src="a";
		try
		{
			MySettings settings=new MySettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			assertEquals("a", tokenizer.nextToken());
			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void twoCharacters()
		throws IOException
	{
		String src="ab";
		try
		{
			MySettings settings=new MySettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			assertEquals("a", tokenizer.nextToken());
			assertEquals("b", tokenizer.nextToken());
			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void threeCharacters()
		throws IOException
	{
		String src="abc";
		try
		{
			MySettings settings=new MySettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			assertEquals("a", tokenizer.nextToken());
			assertEquals("b", tokenizer.nextToken());
			assertEquals("c", tokenizer.nextToken());
			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void pushbackOne()
		throws IOException
	{
		String src="abc";
		try
		{
			MySettings settings=new MySettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			String token1=tokenizer.nextToken();
			assertEquals("a", token1);
			String token2=tokenizer.nextToken();
			assertEquals("b", token2);
			String token3=tokenizer.nextToken();
			assertEquals("c", token3);
			tokenizer.pushBackToken(token3);
			String token4=tokenizer.nextToken();
			assertEquals("c", token4);
			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void pushbackTwo()
		throws IOException
	{
		String src="abc";
		try
		{
			MySettings settings=new MySettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			String token1=tokenizer.nextToken();
			assertEquals("a", token1);
			String token2=tokenizer.nextToken();
			assertEquals("b", token2);
			String token3=tokenizer.nextToken();
			assertEquals("c", token3);
			tokenizer.pushBackToken(token3);
			tokenizer.pushBackToken(token2);
			String token4=tokenizer.nextToken();
			assertEquals("b", token4);
			String token5=tokenizer.nextToken();
			assertEquals("c", token5);
			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
