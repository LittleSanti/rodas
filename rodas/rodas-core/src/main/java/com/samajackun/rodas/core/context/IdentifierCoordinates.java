package com.samajackun.rodas.core.context;

class IdentifierCoordinates
{
	private final int rowDataIndex;

	private final int columnIndex;

	public IdentifierCoordinates(int rowDataIndex, int columnIndex)
	{
		super();
		this.rowDataIndex=rowDataIndex;
		this.columnIndex=columnIndex;
	}

	public int getRowDataIndex()
	{
		return this.rowDataIndex;
	}

	public int getColumnIndex()
	{
		return this.columnIndex;
	}
}