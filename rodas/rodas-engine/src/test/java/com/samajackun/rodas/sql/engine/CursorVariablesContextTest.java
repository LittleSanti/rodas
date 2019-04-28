package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.VariablesContext;
import com.samajackun.rodas.core.eval.VariablesManager;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.execution.CursorException;
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.MyIterableTableData;
import com.samajackun.rodas.core.model.ProviderException;

public class CursorVariablesContextTest
{

	private List<ColumnMetadata> createMetadata1()
	{
		List<ColumnMetadata> metadata=new ArrayList<>();
		metadata.add(new ColumnMetadata("id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetadata("name", Datatype.TEXT, false));
		metadata.add(new ColumnMetadata("days", Datatype.INTEGER_NUMBER, true));
		metadata.add(new ColumnMetadata("amount", Datatype.DECIMAL_NUMBER, true));
		return metadata;
	}

	private DefaultCursor createCursor1()
		throws ProviderException
	{
		List<ColumnMetadata> metadata=createMetadata1();
		List<Object[]> data=Arrays.asList(new Object[][] {
				// @formatter:off
				new Object[] {1, "enero", 31, 0.1d},
				new Object[] {2, "febrero", 28, 0.2d},
				new Object[] {3, "marzo", 31, 0.3d},
				new Object[] {4, "abril", 30, 0.4d},
				new Object[] {5, "mayo", 31, 0.5d},
				// @formatter:on
		});
		IterableTableData iterableTableData=new MyIterableTableData(data);
		DefaultCursor cursor=new DefaultCursor(metadata, iterableTableData);
		return cursor;
	}

	private List<ColumnMetadata> createMetadata2()
	{
		List<ColumnMetadata> metadata=new ArrayList<>();
		metadata.add(new ColumnMetadata("day_id", Datatype.INTEGER_NUMBER, false));
		metadata.add(new ColumnMetadata("day_name", Datatype.TEXT, false));
		return metadata;
	}

	private DefaultCursor createCursor2()
		throws ProviderException
	{
		List<ColumnMetadata> metadata=createMetadata2();
		List<Object[]> data=Arrays.asList(new Object[][] {
						// @formatter:off
							new Object[] {1, "lunes"},
							new Object[] {2, "martes"},
							new Object[] {3, "miércoles"},
							new Object[] {4, "jueves"},
							new Object[] {5, "viernes"},
							new Object[] {6, "sábado"},
							new Object[] {7, "domingo"},
							// @formatter:on
		});
		IterableTableData iterableTableData=new MyIterableTableData(data);
		DefaultCursor cursor=new DefaultCursor(metadata, iterableTableData);
		return cursor;
	}

	@Test
	public void getDataFromHierarchicalCursors()
		throws ProviderException,
		CursorException
	{
		MyOpenContext context=new MyOpenContext();
		DefaultCursor cursorUpper=createCursor2();
		DefaultCursor cursor=createCursor1();
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		VariablesContext globalVariablesContext=new StrictVariablesContext();
		globalVariablesContext.set(Name.instanceOf("year"), 2019);
		VariablesManager variablesManager=new StrictVariablesManager(globalVariablesContext);
		context.setVariablesManager(variablesManager);
		variablesManager.pushLocalContext(new CursorVariablesContext(variablesManager.getLocalVariablesContext(), cursorUpper));
		variablesManager.pushLocalContext(new CursorVariablesContext(variablesManager.getLocalVariablesContext(), cursor));
		try
		{
			IdentifierExpression identifierYear=new IdentifierExpression("year");
			IdentifierExpression identifierId=new IdentifierExpression("id");
			IdentifierExpression identifierName=new IdentifierExpression("name");
			IdentifierExpression identifierDayId=new IdentifierExpression("day_id");
			IdentifierExpression identifierDayName=new IdentifierExpression("day_name");

			assertEquals(2019, identifierYear.evaluate(context, evaluatorFactory));
			cursorUpper.next();
			cursor.next();
			assertEquals("enero", identifierName.evaluate(context, evaluatorFactory));
			assertEquals(1, identifierId.evaluate(context, evaluatorFactory));
			assertEquals("lunes", identifierDayName.evaluate(context, evaluatorFactory));
			assertEquals(1, identifierDayId.evaluate(context, evaluatorFactory));
			assertEquals(2019, identifierYear.evaluate(context, evaluatorFactory));
			cursor.next();
			assertEquals("febrero", identifierName.evaluate(context, evaluatorFactory));
			assertEquals(2, identifierId.evaluate(context, evaluatorFactory));
			assertEquals("lunes", identifierDayName.evaluate(context, evaluatorFactory));
			assertEquals(1, identifierDayId.evaluate(context, evaluatorFactory));
			assertEquals(2019, identifierYear.evaluate(context, evaluatorFactory));
			cursor.next();
			assertEquals("marzo", identifierName.evaluate(context, evaluatorFactory));
			assertEquals(3, identifierId.evaluate(context, evaluatorFactory));
			assertEquals("lunes", identifierDayName.evaluate(context, evaluatorFactory));
			assertEquals(1, identifierDayId.evaluate(context, evaluatorFactory));
			assertEquals(2019, identifierYear.evaluate(context, evaluatorFactory));
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		finally
		{
			variablesManager.popLocalContext();
			variablesManager.popLocalContext();
		}
	}
}
