package com.samajackun.rodas.parsing.source;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractSourceTest
{
	protected abstract AbstractSource createSource(String s)
		throws IOException;

	@Test
	public void empty()
		throws IOException
	{
		String src="";
		AbstractSource source=createSource(src);
		Assert.assertEquals(0, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(0, source.getCurrentColumn());
		int x=source.nextChar();
		Assert.assertEquals(-1, x);
		Assert.assertEquals(0, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(0, source.getCurrentColumn());
	}

	@Test
	public void oneChar()
		throws IOException
	{
		String src="a";
		AbstractSource source=createSource(src);
		Assert.assertEquals(0, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(0, source.getCurrentColumn());
		int x;
		x=source.nextChar();
		Assert.assertEquals('a', x);
		Assert.assertEquals(1, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(1, source.getCurrentColumn());

		x=source.nextChar();
		Assert.assertEquals(-1, x);
		Assert.assertEquals(1, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(1, source.getCurrentColumn());
	}

	@Test
	public void twoChars()
		throws IOException
	{
		String src="ab";
		AbstractSource source=createSource(src);
		Assert.assertEquals(0, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(0, source.getCurrentColumn());
		int x;
		x=source.nextChar();
		Assert.assertEquals('a', x);
		Assert.assertEquals(1, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(1, source.getCurrentColumn());

		x=source.nextChar();
		Assert.assertEquals('b', x);
		Assert.assertEquals(2, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(2, source.getCurrentColumn());

		x=source.nextChar();
		Assert.assertEquals(-1, x);
		Assert.assertEquals(2, source.getCurrentIndex());
		Assert.assertEquals(0, source.getCurrentLine());
		Assert.assertEquals(2, source.getCurrentColumn());
	}

	@Test
	public void oneLine()
		throws IOException
	{
		String src="january";
		AbstractSource source=createSource(src);
		int x;
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		Assert.assertEquals(-1, x);
		Assert.assertEquals(0, source.getCurrentLine());
	}

	@Test
	public void oneLineEndingInNewline()
		throws IOException
	{
		String src="january\n";
		AbstractSource source=createSource(src);
		int x;
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		Assert.assertEquals(0, source.getCurrentLine());
		x=source.nextChar();
		Assert.assertEquals('\n', x);
		Assert.assertEquals(1, source.getCurrentLine());
		x=source.nextChar();
		Assert.assertEquals(-1, x);
		Assert.assertEquals(1, source.getCurrentLine());
	}

	@Test
	public void twoLines()
		throws IOException
	{
		String src="january\nfebrurary";
		AbstractSource source=createSource(src);
		int x;
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		Assert.assertEquals(0, source.getCurrentLine());
		x=source.nextChar();
		Assert.assertEquals('\n', x);
		Assert.assertEquals(1, source.getCurrentLine());
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		x=source.nextChar();
		Assert.assertEquals(-1, x);
		Assert.assertEquals(1, source.getCurrentLine());
	}
}
