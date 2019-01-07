package com.samajackun.rodas.core.model;

import java.util.Map;

/**
 * Represents the data container.
 * Knows the whole schema's structure: Tables and columnas and its types.
 *
 * @author SKN
 */
public interface Provider
{
	public Map<String, Integer> getColumnMapFromTable(String tableName)
		throws ProviderException;

	public TableData getTableData(String table)
		throws ProviderException;

	public Cursor openCursor(String table)
		throws ProviderException;

	public TableMetadata getColumnsMetadataFromTable(String table)
		throws ProviderException;
}
