package com.samajackun.rodas.parsing.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings;

public class MyTokenizerTest
{
	@Test
	public void empty()
		throws IOException
	{
		String src="";
		TokenizerSettings settings=new TokenizerSettings();
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
			TokenizerSettings settings=new TokenizerSettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			Token token1=tokenizer.nextToken();
			assertEquals("a", token1.getImage());
			assertEquals("a", token1.getValue());
			assertEquals("type1", token1.getType());
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
			TokenizerSettings settings=new TokenizerSettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			Token token1=tokenizer.nextToken();
			assertEquals("a", token1.getImage());
			assertEquals("a", token1.getValue());
			assertEquals("type1", token1.getType());
			Token token2=tokenizer.nextToken();
			assertEquals("b", token2.getImage());
			assertEquals("b", token2.getValue());
			assertEquals("type1", token2.getType());
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
			TokenizerSettings settings=new TokenizerSettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			Token token1=tokenizer.nextToken();
			assertEquals("a", token1.getImage());
			assertEquals("a", token1.getValue());
			assertEquals("type1", token1.getType());

			Token token2=tokenizer.nextToken();
			assertEquals("b", token2.getImage());
			assertEquals("b", token2.getValue());
			assertEquals("type1", token2.getType());

			Token token3=tokenizer.nextToken();
			assertEquals("c", token3.getImage());
			assertEquals("c", token3.getValue());
			assertEquals("type0", token3.getType());

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
			TokenizerSettings settings=new TokenizerSettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);

			tokenizer.nextToken();
			tokenizer.nextToken();
			Token token3=tokenizer.nextToken();

			tokenizer.pushBackToken(token3);

			Token token4=tokenizer.nextToken();
			assertEquals("c", token4.getImage());
			assertEquals("c", token4.getValue());
			assertEquals("type0", token4.getType());

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
			TokenizerSettings settings=new TokenizerSettings();
			MyTokenizer tokenizer=new MyTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			tokenizer.nextToken();
			Token token2=tokenizer.nextToken();
			Token token3=tokenizer.nextToken();

			tokenizer.pushBackToken(token3);
			tokenizer.pushBackToken(token2);

			Token token4=tokenizer.nextToken();
			assertEquals("b", token4.getImage());
			assertEquals("b", token4.getValue());
			assertEquals("type1", token4.getType());

			Token token5=tokenizer.nextToken();
			assertEquals("c", token5.getImage());
			assertEquals("c", token5.getValue());
			assertEquals("type0", token5.getType());

			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
