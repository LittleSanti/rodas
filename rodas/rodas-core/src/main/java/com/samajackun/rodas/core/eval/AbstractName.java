package com.samajackun.rodas.core.eval;

public abstract class AbstractName implements Name
{
	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + (getBase().asString().hashCode());
		result=prime * result + ((getPrefix() == null)
			? 0
			: getPrefix().hashCode());
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
		if (!getBase().asString().equals(other.getBase().asString()))
		{
			return false;
		}
		if (getPrefix() == null)
		{
			if (other.getPrefix() != null)
			{
				return false;
			}
		}
		else if (!getPrefix().equals(other.getPrefix()))
		{
			return false;
		}
		return true;
	}

}
