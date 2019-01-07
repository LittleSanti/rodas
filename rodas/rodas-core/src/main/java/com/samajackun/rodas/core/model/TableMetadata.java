package com.samajackun.rodas.core.model;

import java.util.List;

public interface TableMetadata
{
	public String getName();

	public ColumnMetadata getColumnMetadata(int column);

	public List<ColumnMetadata> getListOfColumnMetadata();
}
