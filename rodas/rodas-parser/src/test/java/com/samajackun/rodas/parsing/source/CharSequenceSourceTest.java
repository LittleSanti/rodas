package com.samajackun.rodas.parsing.source;

import java.io.IOException;

public class CharSequenceSourceTest extends AbstractSourceTest
{

	@Override
	protected AbstractSource createSource(String s)
		throws IOException
	{
		return new CharSequenceSource(s);
	}

}
