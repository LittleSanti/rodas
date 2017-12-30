package com.samajackun.rodas.sql.eval;

import org.junit.Test;

public class MyContextTest
{
	@Test
	public void test()
		throws NameAlreadyBoundException
	{
		MyContext context=new MyContext();
		// Source source=new TableSource("mytable");
		context.putSubcontext("a", context);
	}
}
