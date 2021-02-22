package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.samajackun.rodas.core.RodasException;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RodasRuntimeException;
import com.samajackun.rodas.core.model.RowData;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.TableSource;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.parser.SelectSentenceParser;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;

public class EngineAndProviderTest
{
	private final Engine engine=new SqlEngine();

	private final Provider provider=new MyProvider();

	private Context createContext()
	{
		// MapList<String, Source> sources=new MapList<>();
		MyOpenContext context=new MyOpenContext();
		context.setProvider(this.provider);
		context.setVariablesManager(new StrictVariablesManager(new StrictVariablesContext()));
		return context;
	}

	@Test
	public void executeTable()
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Context context=createContext();
		TableSource tableSource=new TableSource("country");
		Cursor cursor=this.engine.execute(tableSource, context);
		while (cursor.hasNext())
		{
			cursor.next();
			RowData rowData=cursor.getRowData();
			cursor.getColumnMap().entrySet().stream().forEach(e -> System.out.printf("%s\t", rowData.get(e.getValue())));
			System.out.printf("\r\n");
		}
		cursor.close();
	}

	private void executeQuery(String sql)
		throws RodasException,
		IOException,
		IOException
	{
		executeQuery(sql, createContext());
	}

	private void executeQuery(String sql, Context context)
		throws RodasException,
		IOException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(sql))));
		ParserContext parserContext=new ParserContext();
		SelectSentence source=SelectSentenceParser.getInstance().parse(tokenizer, parserContext);
		Cursor cursor=this.engine.execute(source, context);
		cursor.getColumnMap().keySet().stream().forEach(s -> System.out.printf("%s\t", s));
		System.out.printf("\r\n");
		while (cursor.hasNext())
		{
			cursor.next();
			RowData rowData=cursor.getRowData();
			cursor.getColumnMap().entrySet().stream().forEach(e -> System.out.printf("%s\t", rowData.get(e.getValue())));
			System.out.printf("\r\n");
		}
	}

	@Test
	public void executeSimpleQuery()
		throws RodasException,
		IOException
	{
		String sql="SELECT idCountry, name, area FROM country";
		Object[][] expected= {
			// @formatter:off
			{ 1, "spain", 121.1d },
			{ 2, "portugal", 122.2d },
			{ 3, "italy", 123.3d },
			// @formatter:on
		};
		executeQuery(sql, createContext(), expected);
	}

	private void executeQuery(String sql, Context context, Object[][] expected)
		throws RodasException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(sql))));
		ParserContext parserContext=new ParserContext();
		SelectSentence source=SelectSentenceParser.getInstance().parse(tokenizer, parserContext);
		Cursor cursor=this.engine.execute(source, context);
		cursor.getColumnMap().keySet().stream().forEach(s -> System.out.printf("%s\t", s));
		int i=0;
		while (cursor.hasNext())
		{
			assertTrue(i < expected.length);
			cursor.next();
			RowData rowData=cursor.getRowData();
			assertEquals(expected[i].length, cursor.getNumberOfColumns());
			for (int j=0; j < cursor.getNumberOfColumns(); j++)
			{
				assertEquals(expected[i][j], rowData.get(j));
			}
			i++;
		}
	}

	@Test
	public void executeQueryWithAliasedColumns()
		throws RodasException,
		IOException
	{
		String sql="SELECT c.idCountry, c.name, 120 FROM country AS c";
		executeQuery(sql);
	}

	@Test
	public void executeWithFormulasInSelect()
		throws RodasException,
		IOException
	{
		String sql="SELECT 1.1E-2 FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithWhere()
		throws RodasException,
		IOException
	{
		String sql="SELECT idCountry, name, 121 FROM country WHERE name='spain' AND idCountry=1";
		executeQuery(sql);
	}

	@Test
	public void executeQueryFromSubquery()
		throws RodasException,
		IOException
	{
		String sql="SELECT name FROM (SELECT idCountry, name, 121 FROM country WHERE name='spain' OR name='portugal')";
		executeQuery(sql);
	}

	@Test
	public void executeComplexQuery01()
		throws RodasException,
		IOException
	{
		String sql="SELECT area, 2*(area+100) uno, 2*area-1 FROM country WHERE name<'spain'";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithSelectFunction()
		throws RodasException,
		IOException
	{
		String sql="SELECT name, area, min(7,len(name),area) FROM country WHERE max(len(name),10)<20";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithAlias()
		throws RodasException,
		IOException
	{
		String sql="SELECT country.name FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryFromNoTable()
		throws RodasException,
		IOException
	{
		String sql="SELECT 120";
		executeQuery(sql);
	}

	@Test
	@Ignore
	public void executeQueryWithAsterisk()
		throws RodasException,
		IOException
	{
		String sql="SELECT * FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithUnexistantParameter()
		throws RodasException,
		IOException
	{
		try
		{
			String sql="SELECT idCountry, name, area FROM country WHERE idCountry=:ID";
			executeQuery(sql);
		}
		catch (RodasRuntimeException e)
		{
			try
			{
				throw e.getCause();
			}
			catch (EvaluationException e3)
			{
				try
				{
					throw e3.getCause();
				}
				catch (VariableNotFoundException e2)
				{
					assertEquals("ID", e2.getName().asString());
				}
				catch (Throwable e2)
				{
					e2.printStackTrace();
					fail(e2.toString());
				}
			}
			catch (Throwable e2)
			{
				e2.printStackTrace();
				fail(e2.toString());
			}
		}
		catch (Exception e)
		{
			fail("Expected RodasRuntimeException/ParameterNotFoundException");
		}
	}

	@Test
	public void executeQueryWithParameter()
		throws RodasException,
		IOException
	{
		String sql="SELECT idCountry, name, area FROM country WHERE idCountry=:ID";
		Context context=createContext();
		context.getVariablesManager().setGlobalVariable(Name.instanceOf("ID"), 2);
		executeQuery(sql, context);
	}

	@Test
	public void executeInnerQueryWithParameter()
		throws RodasException,
		IOException
	{
		String sql="SELECT idCountry, name, len(name) FROM (SELECT idCountry, name, area FROM country WHERE idCountry=:ID)";
		Context context=createContext();
		context.getVariablesManager().setGlobalVariable(Name.instanceOf("ID"), 2);
		executeQuery(sql, context);
	}

	@Test
	public void executeWhereIn()
		throws RodasException,
		IOException
	{
		String sql="SELECT idCountry, name, area FROM country WHERE idCountry IN (1,2)";
		executeQuery(sql);
	}
}
