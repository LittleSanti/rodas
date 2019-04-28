package com.samajackun.rodas.core.eval;

class PrefixedName extends AbstractName
{
	private final String prefix;

	private final Name base;

	public PrefixedName(String prefix, Name base)
	{
		super();
		this.prefix=prefix;
		this.base=base;
	}

	@Override
	public boolean hasPrefix()
	{
		return true;
	}

	@Override
	public String getPrefix()
	{
		return this.prefix;
	}

	@Override
	public Name getBase()
	{
		return this.base;
	}

	@Override
	public String asString()
	{
		return this.prefix + "." + this.base.asString();
	}

}
