package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.samajackun.rodas.sql.RodasSqlException;
import com.samajackun.rodas.sql.eval.Context;
import com.samajackun.rodas.sql.eval.DefaultContext;
import com.samajackun.rodas.sql.eval.EvaluationException;
import com.samajackun.rodas.sql.eval.ParameterNotFoundException;
import com.samajackun.rodas.sql.model.Cursor;
import com.samajackun.rodas.sql.model.Engine;
import com.samajackun.rodas.sql.model.EngineException;
import com.samajackun.rodas.sql.model.Provider;
import com.samajackun.rodas.sql.model.ProviderException;
import com.samajackun.rodas.sql.model.RowData;
import com.samajackun.rodas.sql.model.SelectSentence;
import com.samajackun.rodas.sql.model.TableSource;
import com.samajackun.rodas.sql.parser.SelectSentenceParser;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;

public class EngineAndProviderTest
{
	private final Engine engine=new MyEngine();

	private final Provider provider=new MyProvider();

	@Test
	public void executeTable()
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Context context=new DefaultContext(Collections.emptyMap());
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
		throws RodasSqlException
	{
		executeQuery(sql, new DefaultContext(Collections.emptyMap()));
	}

	private void executeQuery(String sql, Context context)
		throws RodasSqlException
	{
		SelectSentence source=SelectSentenceParser.getInstance().parse(new ParserTokenizer(new SqlTokenizer(sql)));
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
		throws RodasSqlException
	{
		String sql="SELECT idCountry, name, 120 FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithAliasedColumns()
		throws RodasSqlException
	{
		String sql="SELECT c.idCountry, c.name, 120 FROM country AS c";
		executeQuery(sql);
	}

	@Test
	public void executeWithFormulasInSelect()
		throws RodasSqlException
	{
		String sql="SELECT 1.1E-2 FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithWhere()
		throws RodasSqlException
	{
		String sql="SELECT idCountry, name, 121 FROM country WHERE name='spain' AND idCountry=1";
		executeQuery(sql);
	}

	@Test
	public void executeQueryFromSubquery()
		throws RodasSqlException
	{
		String sql="SELECT name FROM (SELECT idCountry, name, 121 FROM country WHERE name='spain' OR name='portugal')";
		executeQuery(sql);
	}

	@Test
	public void executeComplexQuery01()
		throws RodasSqlException
	{
		String sql="SELECT area, 2*(area+100) uno, 2*area-1 FROM country WHERE name<'spain'";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithSelectFunction()
		throws RodasSqlException
	{
		String sql="SELECT name, area, min(7,len(name),area) FROM country WHERE max(len(name),10)<20";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithAlias()
		throws RodasSqlException
	{
		String sql="SELECT country.name FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithAsterisk()
		throws RodasSqlException
	{
		String sql="SELECT * FROM country";
		executeQuery(sql);
	}

	@Test
	public void executeQueryWithUnexistantParameter()
		throws RodasSqlException
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
		throws RodasSqlException
	{
		String sql="SELECT idCountry, name, area FROM country WHERE idCountry=:ID";
		DefaultContext context=new DefaultContext(Collections.emptyMap());
		context.setParameter("ID", 2);
		executeQuery(sql, context);
	}

	@Test
	public void executeInnerQueryWithParameter()
		throws RodasSqlException
	{
		String sql="SELECT idCountry, name, len(name) FROM (SELECT idCountry, name, area FROM country WHERE idCountry=:ID)";
		DefaultContext context=new DefaultContext(Collections.emptyMap());
		context.setParameter("ID", 2);
		executeQuery(sql, context);
	}

	@Test
	public void executeWhereIn()
		throws RodasSqlException
	{
		String sql="SELECT idCountry, name, area FROM country WHERE idCountry IN (1,2)";
		executeQuery(sql);
	}
}
