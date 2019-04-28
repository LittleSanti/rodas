package com.samajackun.rodas.sql.engine;

import java.util.Comparator;

public final class ObjectComparator implements Comparator<Object>
{
	private static final ObjectComparator INSTANCE=new ObjectComparator();

	public static ObjectComparator getInstance()
	{
		return INSTANCE;
	}

	private ObjectComparator()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object o1, Object o2)
	{
		return ((Comparable<Object>)o1).compareTo(o2);
	}
}
