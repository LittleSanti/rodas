package com.samajackun.rodas.sql.engine;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;

class SingleRowCursor extends DefaultCursor
{
	public SingleRowCursor()
		throws ProviderException
	{
		super(createMetadata(), createIterableTableData());
	}

	private static List<ColumnMetadata> createMetadata()
	{
		return Collections.emptyList();
	}

	private static IterableTableData createIterableTableData()
	{
		return new IterableTableData()
		{
			private final List<RowData> list=Collections.singletonList(new RowData()
			{
				@Override
				public Object get(int column)
				{
					return null;
				}

				@Override
				public long position()
				{
					return 0;
				}
			});

			@Override
			public Iterator<RowData> iterator()
			{
				return this.list.iterator();
			}
		};
	}
}
