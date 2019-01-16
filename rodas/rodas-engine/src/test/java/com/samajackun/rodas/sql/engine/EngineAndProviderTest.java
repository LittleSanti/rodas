package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.samajackun.rodas.core.RodasException;
import com.samajackun.rodas.core.context.DefaultBuildingContext;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.MapList;
import com.samajackun.rodas.core.eval.ParameterNotFoundException;
import com.samajackun.rodas.core.model.Cursor;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableSource;
import com.samajackun.rodas.sql.parser.SelectSentenceParser;
import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;

public class EngineAndProviderTest
{
	private final Engine engine=new MyEngine();

	private final Provider provider=new MyProvider();

	private Context createContext()
	{
		MapList<String, Source> sources=new MapList<>();
		return new DefaultBuildingContext(this.provider, sources);
	}

	@Test
	public void executeTable()
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Context context=createContext();
		TableSource tableSource=new TableSource("country");
		Cursor cursor=this.engine.execute(tableSource, this.provider, context);
		while (cursor.hasNext())
		{
			cursor.next();
			RowData rowData=cursor.getRowData();
			cursor.getColumnMap().entrySet().stream().forEach(e -> System.out.printf("%s\t", rowData.get(e.getValue())));
			System.out.printf("\r\n");
		}
	}

	private void executeQuery(String sql)
		throws RodasException
	{
		executeQuery(sql, createContext());
	}

	private void executeQuery(String sql, Context context)
		throws RodasException
	{
		SelectSentence source=SelectSentenceParser.getInstance().parse(new MatchingSqlTokenizer(new SqlTokenizer(sql)));
		Cursor cursor=this.engine.execute(source, this.provider, context);
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
	public void executeQuery()
		throws RodasException
	{
		String sql="SELECT idCountry, name, 120 FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithAliasedColumns()
		throws RodasException
	{
		String sql="SELECT c.idCountry, c.name, 120 FROM country AS c";
		executeQuery(sql);
	}

	@Test
	public void executeWithFormulasInSelect()
		throws RodasException
	{
		String sql="SELECT 1.1E-2 FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithWhere()
		throws RodasException
	{
		String sql="SELECT idCountry, name, 121 FROM country WHERE name='spain' AND idCountry=1";
		executeQuery(sql);
	}

	@Test
	public void executeQueryFromSubquery()
		throws RodasException
	{
		String sql="SELECT name FROM (SELECT idCountry, name, 121 FROM country WHERE name='spain' OR name='portugal')";
		executeQuery(sql);
	}

	@Test
	public void executeComplexQuery01()
		throws RodasException
	{
		String sql="SELECT area, 2*(area+100) uno, 2*area-1 FROM country WHERE name<'spain'";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithSelectFunction()
		throws RodasException
	{
		String sql="SELECT name, area, min(7,len(name),area) FROM country WHERE max(len(name),10)<20";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithAlias()
		throws RodasException
	{
		String sql="SELECT country.name FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithAsterisk()
		throws RodasException
	{
		String sql="SELECT * FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithUnexistantParameter()
		throws RodasException
	{
		try
		{
			String sql="SELECT idCountry, name, area FROM country WHERE idCountry=:ID";
			executeQuery(sql);
		}
		catch (ParameterNotFoundException e)
		{
			assertEquals("ID", e.getParameter());
		}
	}

	@Test
	public void executeQueryWithParameter()
		throws RodasException
	{
		String sql="SELECT idCountry, name, area FROM country WHERE idCountry=:ID";
		DefaultBuildingContext context=createContext();
		context.setParameter("ID", 2);
		executeQuery(sql, context);
	}

	@Test
	public void executeInnerQueryWithParameter()
		throws RodasException
	{
		String sql="SELECT idCountry, name, len(name) FROM (SELECT idCountry, name, area FROM country WHERE idCountry=:ID)";
		DefaultBuildingContext context=createContext();
		context.setParameter("ID", 2);
		executeQuery(sql, context);
	}

	@Test
	public void executeWhereIn()
		throws RodasException
	{
		String sql="SELECT idCountry, name, area FROM country WHERE idCountry IN (1,2)";
		executeQuery(sql);
	}
}
