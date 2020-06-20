package com.samajackun.rodas.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LevelMap<K, V>
{
	private final List<LevelMap<K, V>> ancestors;

	private final Map<K, V> map=new HashMap<>();

	protected LevelMap(List<LevelMap<K, V>> ancestors)
	{
		super();
		this.ancestors=ancestors;
	}

	public static <K, V> LevelMap<K, V> createRoot()
	{
		return new LevelMap<>(new ArrayList<LevelMap<K, V>>());
	}

	public V put(K key, V value)
	{
		return this.map.put(key, value);
	}

	public V putIfAbsent(K key, Supplier<V> supplier)
	{
		return this.map.computeIfAbsent(key, x -> supplier.get());
	}

	public V remove(K key)
	{
		return this.map.remove(key);
	}

	public void clear()
	{
		this.map.clear();
	}

	public V getAtCurrentLevel(K key)
	{
		return this.map.get(key);
	}

	public boolean containsKey(K key)
	{
		return this.map.containsKey(key);
	}

	public V getAtCurrentOrPreviousLevel(K key)
	{
		LevelMap<K, V> levelMap=this;
		boolean found;
		do
		{
			found=levelMap.containsKey(key);
			if (!found)
			{
				levelMap=levelMap.hasAncestors()
					? levelMap.getAncestor(1)
					: null;
			}
		}
		while (!found && levelMap != null);
		return found
			? levelMap.getAtCurrentLevel(key)
			: null;
	}

	private boolean hasAncestors()
	{
		return this.ancestors.size() > 0;
	}

	public LevelMap<K, V> push()
	{
		List<LevelMap<K, V>> newAncestors=new ArrayList<>(this.ancestors);
		newAncestors.add(this);
		return new LevelMap<>(newAncestors);
	}

	public void pop(LevelMap<K, V> levelMap)
	{
		// Intencionadamente vacío.
	}

	public LevelMap<K, V> getAncestor(int level)
	{
		return level == 0
			? this
			: this.ancestors.get(this.ancestors.size() - level);
	}
}
