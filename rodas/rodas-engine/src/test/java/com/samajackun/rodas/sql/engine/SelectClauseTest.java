package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.TextConstantExpression;

public class SelectClauseTest
{
	@Test
	public void empty()
	{
		SelectClause selectClause=new SelectClause();
		assertTrue(selectClause.getColumnsInOrder().isEmpty());
		assertTrue(selectClause.getColumnMetadatasInOrder().isEmpty());
		assertTrue(selectClause.getColumnsByBaseOrQualifiedName().isEmpty());
		assertEquals(0, selectClause.countColums());
	}

	@Test
	public void oneColumnWithSimpleName()
	{
		SelectClause selectClause=new SelectClause();
		Expression exp1=new TextConstantExpression("120");
		Name name1=Name.instanceOf("january");
		ColumnMetaData columnMetadata1=new ColumnMetaData(name1.getBase().asString(), Datatype.TEXT, true);
		int n=selectClause.addColumn(name1, exp1, columnMetadata1);
		assertEquals(0, n);
		assertEquals(1, selectClause.countColums());

		assertEquals(exp1, selectClause.getColumnsInOrder().get(0));
		assertEquals(columnMetadata1, selectClause.getColumnMetadatasInOrder().get(0));
		Map<Name, Integer> mapByName;
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(0, mapByName.get(name1).intValue());
	}

	@Test
	public void oneColumnWithComplexName()
	{
		SelectClause selectClause=new SelectClause();
		Expression exp1=new TextConstantExpression("120");
		Name name1=Name.instanceOf("months", "january");
		ColumnMetaData columnMetadata1=new ColumnMetaData(name1.getBase().asString(), Datatype.TEXT, true);
		int n=selectClause.addColumn(name1, exp1, columnMetadata1);
		assertEquals(0, n);

		assertEquals(exp1, selectClause.getColumnsInOrder().get(0));
		assertEquals(columnMetadata1, selectClause.getColumnMetadatasInOrder().get(0));
		Map<Name, Integer> mapByName;

		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(0, mapByName.get(name1).intValue());

		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1.getBase());
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(0, mapByName.get(name1).intValue());
	}

	@Test
	public void twoColumnWithSimpleNames()
	{
		SelectClause selectClause=new SelectClause();

		Expression exp1=new TextConstantExpression("120");
		Name name1=Name.instanceOf("january");
		ColumnMetaData columnMetadata1=new ColumnMetaData(name1.getBase().asString(), Datatype.TEXT, true);
		int n=selectClause.addColumn(name1, exp1, columnMetadata1);
		assertEquals(0, n);

		Expression exp2=new TextConstantExpression("130");
		Name name2=Name.instanceOf("february");
		ColumnMetaData columnMetadata2=new ColumnMetaData(name2.getBase().asString(), Datatype.TEXT, true);
		n=selectClause.addColumn(name2, exp2, columnMetadata2);
		assertEquals(1, n);
		assertEquals(2, selectClause.countColums());

		assertEquals(exp1, selectClause.getColumnsInOrder().get(0));
		assertEquals(columnMetadata1, selectClause.getColumnMetadatasInOrder().get(0));
		Map<Name, Integer> mapByName;
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(0, mapByName.get(name1).intValue());

		assertEquals(exp2, selectClause.getColumnsInOrder().get(1));
		assertEquals(columnMetadata2, selectClause.getColumnMetadatasInOrder().get(1));
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name2);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(1, mapByName.get(name2).intValue());
	}

	@Test
	public void twoColumnsWithComplexNames()
	{
		SelectClause selectClause=new SelectClause();
		Expression exp1=new TextConstantExpression("120");
		Name name1=Name.instanceOf("months", "january");
		ColumnMetaData columnMetadata1=new ColumnMetaData(name1.getBase().asString(), Datatype.TEXT, true);
		int n=selectClause.addColumn(name1, exp1, columnMetadata1);
		assertEquals(0, n);

		Expression exp2=new TextConstantExpression("130");
		Name name2=Name.instanceOf("months", "february");
		ColumnMetaData columnMetadata2=new ColumnMetaData(name2.getBase().asString(), Datatype.TEXT, true);
		n=selectClause.addColumn(name2, exp2, columnMetadata2);
		assertEquals(1, n);

		assertEquals(exp1, selectClause.getColumnsInOrder().get(0));
		assertEquals(columnMetadata1, selectClause.getColumnMetadatasInOrder().get(0));
		Map<Name, Integer> mapByName;

		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(0, mapByName.get(name1).intValue());

		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1.getBase());
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(0, mapByName.get(name1).intValue());

		assertEquals(exp2, selectClause.getColumnsInOrder().get(1));
		assertEquals(columnMetadata2, selectClause.getColumnMetadatasInOrder().get(1));

		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name2);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(1, mapByName.get(name2).intValue());

		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name2.getBase());
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(1, mapByName.get(name2).intValue());
	}

	@Test
	public void twoColumnsWithSameBaseNameAndDifferentPrefixes()
	{
		SelectClause selectClause=new SelectClause();

		// Add 1st column:
		Expression exp1=new TextConstantExpression("120");
		Name name1=Name.instanceOf("months", "june");
		ColumnMetaData columnMetadata1=new ColumnMetaData(name1.getBase().asString(), Datatype.TEXT, true);
		int n=selectClause.addColumn(name1, exp1, columnMetadata1);
		assertEquals(0, n);

		// Add 2nd column:
		Expression exp2=new TextConstantExpression("130");
		Name name2=Name.instanceOf("gods", "june");
		ColumnMetaData columnMetadata2=new ColumnMetaData(name2.getBase().asString(), Datatype.TEXT, true);
		n=selectClause.addColumn(name2, exp2, columnMetadata2);
		assertEquals(1, n);

		Map<Name, Integer> mapByName;

		// Check 1st column data:
		assertEquals(exp1, selectClause.getColumnsInOrder().get(0));
		assertEquals(columnMetadata1, selectClause.getColumnMetadatasInOrder().get(0));

		// Check 1st column mapping by full name:
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(0, mapByName.get(name1).intValue());

		// Check 1st column mapping by simple name:
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1.getBase());
		assertNotNull(mapByName);
		assertEquals(2, mapByName.size());
		assertEquals(0, mapByName.get(name1).intValue());

		// Check 2nd column data:
		assertEquals(exp2, selectClause.getColumnsInOrder().get(1));
		assertEquals(columnMetadata2, selectClause.getColumnMetadatasInOrder().get(1));

		// Check 2nd column mapping by full name:
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name2);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(1, mapByName.get(name2).intValue());

		// Check 1st column mapping by simple name:
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name2.getBase());
		assertNotNull(mapByName);
		assertEquals(2, mapByName.size());
		assertEquals(1, mapByName.get(name2).intValue());
	}

	@Test
	public void twoColumnsWithSameName()
	{
		SelectClause selectClause=new SelectClause();

		// Add 1st column:
		Expression exp1=new TextConstantExpression("120");
		Name name1=Name.instanceOf("june");
		ColumnMetaData columnMetadata1=new ColumnMetaData(name1.getBase().asString(), Datatype.TEXT, true);
		int n=selectClause.addColumn(name1, exp1, columnMetadata1);
		assertEquals(0, n);

		// Add 2nd column:
		Expression exp2=new TextConstantExpression("130");
		Name name2=Name.instanceOf("june");
		ColumnMetaData columnMetadata2=new ColumnMetaData(name2.getBase().asString(), Datatype.TEXT, true);
		n=selectClause.addColumn(name2, exp2, columnMetadata2);
		assertEquals(1, n);

		Map<Name, Integer> mapByName;

		// Check 1st column data:
		assertEquals(exp1, selectClause.getColumnsInOrder().get(0));
		assertEquals(columnMetadata1, selectClause.getColumnMetadatasInOrder().get(0));

		// Check 1st column mapping by full name:
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(1, mapByName.get(name1).intValue());

		// Check 1st column mapping by simple name:
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name1.getBase());
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(1, mapByName.get(name1).intValue());

		// Check 2nd column data:
		assertEquals(exp2, selectClause.getColumnsInOrder().get(1));
		assertEquals(columnMetadata2, selectClause.getColumnMetadatasInOrder().get(1));

		// Check 2nd column mapping by full name:
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name2);
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(1, mapByName.get(name2).intValue());

		// Check 1st column mapping by simple name:
		mapByName=selectClause.getColumnsByBaseOrQualifiedName().get(name2.getBase());
		assertNotNull(mapByName);
		assertEquals(1, mapByName.size());
		assertEquals(1, mapByName.get(name2).intValue());
	}
}
