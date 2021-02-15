package com.samajackun.rodas.core.eval;

public interface Name
{
	public boolean hasPrefix();

	public String getPrefix();

	public Name getBase();

	public String asString();

	public static Name instanceOf(String base)
	{
		return new SimpleName(base);
	}

	public static Name instanceOf(String prefix, String base)
	{
		return instanceOf(prefix, new SimpleName(base));
	}

	public static Name instanceOf(String prefix, Name base)
	{
		return prefix == null
			? base
			: new PrefixedName(prefix, base);
	}
}
