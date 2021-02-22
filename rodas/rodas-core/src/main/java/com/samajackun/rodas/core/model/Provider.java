package com.samajackun.rodas.core.model;

/**
 * Represents the data container.
 * Knows the whole schema's structure: Tables and columnas and its types.
 *
 * @author SKN
 */
public interface Provider
{
	public TableMetaData getTableMetaData(String table)
		throws ProviderException;
}
