package com.samajackun.rodas.core.model;

import java.util.List;
import java.util.Map;

public interface TableMetaData
{
	public String getName();

	public ColumnMetaData getColumnMetadata(int column);

	public List<ColumnMetaData> getListOfColumnMetadata();

	public Map<String, Integer> getColumnMap();

	public TableData getTableData();
}
