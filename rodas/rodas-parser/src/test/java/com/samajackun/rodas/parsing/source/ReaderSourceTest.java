package com.samajackun.rodas.parsing.source;

import java.io.IOException;
import java.io.StringReader;

public class ReaderSourceTest extends AbstractSourceTest
{

	@Override
	protected AbstractSource createSource(String s)
		throws IOException
	{
		return new ReaderSource(new StringReader(s));
	}

}
