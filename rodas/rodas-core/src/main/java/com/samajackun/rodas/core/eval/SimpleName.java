package com.samajackun.rodas.core.eval;

class SimpleName extends AbstractName
{
	private final String base;

	public SimpleName(String base)
	{
		super();
		this.base=base;
	}

	@Override
	public boolean hasPrefix()
	{
		return false;
	}

	@Override
	public String getPrefix()
	{
		return null;
	}

	@Override
	public Name getBase()
	{
		return this;
	}

	@Override
	public String asString()
	{
		return this.base;
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=super.hashCode();
		result=prime * result + ((this.base == null)
			? 0
			: this.base.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!super.equals(obj))
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		SimpleName other=(SimpleName)obj;
		if (this.base == null)
		{
			if (other.base != null)
			{
				return false;
			}
		}
		else if (!this.base.equals(other.base))
		{
			return false;
		}
		return true;
	}
}
