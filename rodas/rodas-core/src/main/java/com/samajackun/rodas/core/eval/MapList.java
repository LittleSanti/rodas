package com.samajackun.rodas.core.eval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class MapList<K, V>
{
	private final Map<K, Entry> map;

	private final List<Entry> list;

	public MapList()
	{
		this(57);
	}

	public MapList(int initialSize)
	{
		super();
		this.map=new HashMap<>((int)(1.7d * initialSize));
		this.list=new ArrayList<>(initialSize);
	}

	public MapList(MapList<K, V> src)
	{
		super();
		this.map=new HashMap<>(src.map);
		this.list=new ArrayList<>(src.list);
	}

	public V put(K key, V value)
	{
		synchronized (this)
		{
			V old=get(key);
			int n=this.list.size();
			Entry entry=new Entry(n, key, value);
			this.list.add(entry);
			this.map.put(key, entry);
			return old;
		}
	}

	public V get(K key)
	{
		synchronized (this)
		{
			Entry entry=this.map.get(key);
			return entry == null
				? null
				: entry.getValue();
		}
	}

	public int indexOf(K key)
	{
		synchronized (this)
		{
			Entry entry=this.map.get(key);
			return entry == null
				? -1
				: entry.getIndex();
		}
	}

	public V get(int index)
	{
		synchronized (this)
		{
			Entry entry=this.list.get(index);
			return entry == null
				? null
				: entry.getValue();
		}
	}

	public List<K> keys()
	{
		return new MyListWrapper();
	}

	public Collection<V> values()
	{
		Collection<V> values=new ArrayList<>(this.map.size());
		for (Entry entry : this.list)
		{
			values.add(entry.getValue());
		}
		return values;
	}

	public int size()
	{
		return this.map.size();
	}

	private class Entry
	{
		private final int index;

		private final K key;

		private final V value;

		public Entry(int index, K key, V value)
		{
			super();
			this.index=index;
			this.key=key;
			this.value=value;
		}

		public int getIndex()
		{
			return this.index;
		}

		public K getKey()
		{
			return this.key;
		}

		public V getValue()
		{
			return this.value;
		}
	}

	private class MyListWrapper implements List<K>
	{

		@Override
		public int size()
		{
			return MapList.this.size();
		}

		@Override
		public boolean isEmpty()
		{
			return MapList.this.map.isEmpty();
		}

		@Override
		public boolean contains(Object key)
		{
			return MapList.this.map.containsKey(key);
		}

		@Override
		public Iterator<K> iterator()
		{
			return new MyIterator();
		}

		@Override
		public Object[] toArray()
		{
			Object[] array=new Object[MapList.this.list.size()];
			int i=0;
			for (Entry entry : MapList.this.list)
			{
				array[i++]=entry.getValue();
			}
			return array;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a)
		{
			int i=0;
			for (Entry entry : MapList.this.list)
			{
				a[i++]=(T)entry.getValue();
			}
			return a;
		}

		@Override
		public boolean add(K e)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public boolean remove(Object o)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public boolean containsAll(Collection<?> c)
		{
			return MapList.this.map.keySet().containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends K> c)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public boolean addAll(int index, Collection<? extends K> c)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public boolean removeAll(Collection<?> c)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public boolean retainAll(Collection<?> c)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public void clear()
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public K get(int index)
		{
			return MapList.this.list.get(index).getKey();
		}

		@Override
		public K set(int index, K element)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public void add(int index, K element)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public K remove(int index)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public int indexOf(Object o)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public int lastIndexOf(Object o)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public ListIterator<K> listIterator()
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public ListIterator<K> listIterator(int index)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		@Override
		public List<K> subList(int fromIndex, int toIndex)
		{
			throw new UnsupportedOperationException("Unmodifiable object");
		}

		public class MyIterator implements Iterator<K>
		{
			private int current=0;

			@Override
			public boolean hasNext()
			{
				return this.current < MapList.this.list.size();
			}

			@Override
			public K next()
			{
				if (!hasNext())
				{
					throw new NoSuchElementException();
				}
				return MapList.this.list.get(this.current++).getKey();
			}
		}
	}
}
