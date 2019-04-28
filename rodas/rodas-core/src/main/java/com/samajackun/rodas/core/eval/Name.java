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
		return prefix == null
			? new SimpleName(base)
			: new PrefixedName(prefix, new SimpleName(base));
	}

}
