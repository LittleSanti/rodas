package com.samajackun.rodas.core.model;

public interface SourceClause
{
	public Source createSource(Provider provider)
		throws ProviderException;
}
