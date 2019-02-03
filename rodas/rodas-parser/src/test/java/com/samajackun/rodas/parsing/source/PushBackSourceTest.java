package com.samajackun.rodas.parsing.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class PushBackSourceTest
{
	@Test
	public void emptySourceNextChar()
		throws IOException
	{
		String src="";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertFalse(source.hasMoreChars());
		assertEquals(0, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(0, source.getCurrentColumn());
		assertEquals(-1, source.nextChar());
		assertEquals(0, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(0, source.getCurrentColumn());
	}

	@Test
	public void emptySourceNextCharAfterEnd()
		throws IOException
	{
		String src="";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals(-1, source.nextChar());
		assertEquals(-1, source.nextChar());
		assertEquals(0, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(0, source.getCurrentColumn());
	}

	@Test
	public void emptySourceStartRecordBeforeBeginning()
		throws IOException
	{
		String src="";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		source.startRecord();
		assertEquals("", source.endRecord().toString());
	}

	@Test
	public void emptySourceStartRecordBeforeBeginningEndRecordAfterEnd()
		throws IOException
	{
		String src="";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		source.startRecord();
		assertEquals(-1, source.nextChar());
		assertEquals("", source.endRecord().toString());
	}

	@Test
	public void oneCharSourceNextChar()
		throws IOException
	{
		String src="a";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertTrue(source.hasMoreChars());
		assertEquals(0, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(0, source.getCurrentColumn());

		assertEquals('a', source.nextChar());
		assertFalse(source.hasMoreChars());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());

		assertEquals(-1, source.nextChar());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());
	}

	@Test
	public void oneCharSourceNextCharAfterEnd()
		throws IOException
	{
		String src="a";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));

		assertEquals('a', source.nextChar());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());

		assertEquals(-1, source.nextChar());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());
	}

	@Test
	public void oneCharSourceUngetAfterFirstChar()
		throws IOException
	{
		String src="a";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('a', source.nextChar());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());

		source.unget('a');
		assertEquals(0, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(0, source.getCurrentColumn());

		assertEquals('a', source.nextChar());
		assertEquals(0, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(0, source.getCurrentColumn());

		assertEquals(-1, source.nextChar());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());
		source.unget('a');
		assertEquals('a', source.nextChar());
		assertEquals(0, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(0, source.getCurrentColumn());
	}

	@Test
	public void oneCharSourceUngetAfterEnd()
		throws IOException
	{
		String src="a";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('a', source.nextChar());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());

		assertEquals(-1, source.nextChar());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());
		source.unget('a');
		assertEquals('a', source.nextChar());
		assertEquals(0, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(0, source.getCurrentColumn());
		assertEquals(-1, source.nextChar());
		assertEquals(1, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(1, source.getCurrentColumn());
	}

	@Test
	public void oneCharSourceStartRecordBeforeBeginningEndRecordBeforeEnd()
		throws IOException
	{
		String src="a";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		source.startRecord();
		assertEquals('a', source.nextChar());
		assertEquals("a", source.endRecord().toString());
	}

	@Test
	public void oneCharSourceStartRecordBeforeBeginningEndRecordAfterEnd()
		throws IOException
	{
		String src="a";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		source.startRecord();
		assertEquals('a', source.nextChar());
		assertEquals(-1, source.nextChar());
		assertEquals("a", source.endRecord().toString());
	}

	@Test
	public void someCharsSourceStartRecordBeforeBeginningEndRecordAfterEnd()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('n', source.nextChar());
		source.startRecord();
		assertEquals('u', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.nextChar());
		assertEquals('y', source.nextChar());
		assertEquals(-1, source.nextChar());
		assertEquals("uary", source.endRecord().toString());
	}

	@Test
	public void someCharsSourceStartRecordBeforeBeginningEndRecordBeforeEnd()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('n', source.nextChar());
		source.startRecord();
		assertEquals('u', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.nextChar());
		assertEquals('y', source.nextChar());
		assertEquals("uary", source.endRecord().toString());
		assertEquals(-1, source.nextChar());
	}

	@Test
	public void someCharsSourceStartRecordBeforeBeginningEndRecordInTheMiddle()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('n', source.nextChar());
		source.startRecord();
		assertEquals('u', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.nextChar());
		assertEquals("uar", source.endRecord().toString());
		assertEquals('y', source.nextChar());
		assertEquals(-1, source.nextChar());
	}

	@Test
	public void someCharsSourceRecordAndUnget()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('n', source.nextChar());
		source.unget('n');
		source.startRecord();
		assertEquals('n', source.nextChar());
		assertEquals('u', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.nextChar());
		assertEquals("nuar", source.endRecord().toString());
		assertEquals('y', source.nextChar());
		assertEquals(-1, source.nextChar());
	}

	@Test
	public void ungetAndRecord()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.nextChar());
		source.unget('j');
		source.startRecord();
		assertEquals('j', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('n', source.nextChar());
		assertEquals('u', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.nextChar());
		assertEquals("januar", source.endRecord().toString());
		assertEquals('y', source.nextChar());
		assertEquals(-1, source.nextChar());
	}

	@Test
	public void ungetWhileRecording()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.nextChar());
		source.startRecord();
		assertEquals('a', source.nextChar());
		assertEquals('n', source.nextChar());
		source.unget('n');
		assertEquals('n', source.nextChar());
		assertEquals('u', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.nextChar());
		assertEquals("anuar", source.endRecord().toString());
		assertEquals('y', source.nextChar());
		assertEquals(-1, source.nextChar());
	}

	@Test
	public void ungetBeforeEnd()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('n', source.nextChar());
		assertEquals('u', source.nextChar());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.nextChar());
		assertEquals('y', source.nextChar());
		assertFalse(source.hasMoreChars());
		source.unget('y');
		assertTrue(source.hasMoreChars());
		assertEquals('y', source.nextChar());
		assertFalse(source.hasMoreChars());
		assertEquals(-1, source.nextChar());
		assertFalse(source.hasMoreChars());
	}

	@Test
	public void lookahead()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.lookahead());
		assertEquals('j', source.nextChar());
		assertEquals('a', source.lookahead());
		assertEquals('a', source.nextChar());
		assertEquals('n', source.lookahead());
		assertEquals('n', source.nextChar());
		assertEquals('u', source.lookahead());
		assertEquals('u', source.nextChar());
		assertEquals('a', source.lookahead());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.lookahead());
		assertEquals('r', source.nextChar());
		assertEquals('y', source.lookahead());
		assertEquals('y', source.nextChar());
		assertEquals(-1, source.lookahead());
		assertEquals(-1, source.nextChar());
	}

	@Test
	public void ungetString()
		throws IOException
	{
		String src="january";
		PushBackSource source=new PushBackSource(new CharSequenceSource(src));
		assertEquals('j', source.lookahead());
		assertEquals('j', source.nextChar());
		assertEquals('a', source.lookahead());
		assertEquals('a', source.nextChar());
		assertEquals('n', source.lookahead());
		assertEquals('n', source.nextChar());
		assertEquals('u', source.lookahead());
		assertEquals('u', source.nextChar());
		assertEquals('a', source.lookahead());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.lookahead());
		assertEquals('r', source.nextChar());
		assertEquals('y', source.lookahead());
		assertEquals('y', source.nextChar());
		assertEquals(7, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(7, source.getCurrentColumn());
		source.unget("ary");
		assertEquals(4, source.getCurrentIndex());
		assertEquals(0, source.getCurrentLine());
		assertEquals(4, source.getCurrentColumn());
		assertEquals('a', source.lookahead());
		assertEquals('a', source.nextChar());
		assertEquals('r', source.lookahead());
		assertEquals('r', source.nextChar());
		assertEquals('y', source.lookahead());
		assertEquals('y', source.nextChar());
		assertEquals(-1, source.lookahead());
		assertEquals(-1, source.nextChar());
	}
}
