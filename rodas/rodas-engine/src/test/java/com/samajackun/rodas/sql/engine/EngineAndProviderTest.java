package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.core.RodasException;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.MyOpenContext;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.ParameterNotFoundException;
import com.samajackun.rodas.core.eval.StrictVariablesContext;
import com.samajackun.rodas.core.eval.StrictVariablesManager;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.MyProvider;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
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
	private final Engine engine=new MyEngine();

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
	public void executeQuery()
		throws RodasException,
		IOException
	{
		String sql="SELECT idCountry, name, 120 FROM country";
		executeQuery(sql);
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
		catch (ParameterNotFoundException e)
		{
			assertEquals("ID", e.getParameter());
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
