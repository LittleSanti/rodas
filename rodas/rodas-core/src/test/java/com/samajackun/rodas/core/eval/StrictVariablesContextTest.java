package com.samajackun.rodas.core.eval;

import static org.junit.Assert.assertEquals;

import java.util.function.Supplier;

import org.junit.Test;
import org.mockito.Mockito;

public class StrictVariablesContextTest
{
	@Test
	public void test()
	{
		StrictVariablesContext context=new StrictVariablesContext();
		Name name=Name.instanceOf("id");
		@SuppressWarnings("unchecked")
		Supplier<Object> supplier1=Mockito.mock(Supplier.class);
		Mockito.when(supplier1.get()).thenReturn(120L);
		Object v1=context.setIfAbsent(name, supplier1);
		Mockito.verify(supplier1, Mockito.times(1)).get();
		assertEquals(120L, v1);
	}

}
