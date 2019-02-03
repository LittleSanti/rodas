package com.samajackun.rodas.core.eval;

public class Name
{
	private final Name prefix;

	private final String base;

	public Name(Name prefix, String base)
	{
		super();
		this.prefix=prefix;
		this.base=base;
	}

	public Name(String base)
	{
		this(null, base);
	}

	public boolean hasPrefix()
	{
		return this.prefix != null;
	}

	public Name getPrefix()
	{
		return this.prefix;
	}

	public String getBase()
	{
		return this.base;
	}

	@Override
	public String toString()
	{
		return (this.prefix == null)
			? this.base
			: this.prefix.toString() + "." + this.base;
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.base == null)
			? 0
			: this.base.hashCode());
		result=prime * result + ((this.prefix == null)
			? 0
			: this.prefix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Name other=(Name)obj;
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
		if (this.prefix == null)
		{
			if (other.prefix != null)
			{
				return false;
			}
		}
		else if (!this.prefix.equals(other.prefix))
		{
			return false;
		}
		return true;
	}
}
