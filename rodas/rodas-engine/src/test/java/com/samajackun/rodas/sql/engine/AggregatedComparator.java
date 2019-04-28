package com.samajackun.rodas.sql.engine;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

class AggregatedComparator implements Comparator<Collection<Object>>
{
	private static final AggregatedComparator INSTANCE=new AggregatedComparator();

	public static AggregatedComparator getInstance()
	{
		return INSTANCE;
	}

	private AggregatedComparator()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Collection<Object> o1, Collection<Object> o2)
	{
		int x=0;
		Iterator<Object> iterator1=o1.iterator();
		Iterator<Object> iterator2=o2.iterator();
		while (x == 0 && iterator1.hasNext() && iterator2.hasNext())
		{
			Object item1=iterator1.next();
			Object item2=iterator2.next();
			x=((Comparable<Object>)item1).compareTo(item2);
		}
		return x;
	}
}
