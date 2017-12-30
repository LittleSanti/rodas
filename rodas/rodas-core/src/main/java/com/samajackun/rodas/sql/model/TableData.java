package com.samajackun.rodas.sql.model;

public interface TableData extends IterableTableData
{
	public Object[] getRow(int row)
		throws ProviderException;

	public int countRows()
		throws ProviderException;
}
