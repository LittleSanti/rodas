package com.samajackun.rodas.sql.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.samajackun.rodas.sql.model.AliasedSource;
import com.samajackun.rodas.sql.model.EqualsExpression;
import com.samajackun.rodas.sql.model.GreaterThanExpression;
import com.samajackun.rodas.sql.model.IdentifierExpression;
import com.samajackun.rodas.sql.model.OnJoinedSource;
import com.samajackun.rodas.sql.model.ParehentesizedSource;
import com.samajackun.rodas.sql.model.Source;
import com.samajackun.rodas.sql.model.TableSource;
import com.samajackun.rodas.sql.model.UsingJoinedSource;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.PushBackTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public class SourceListParserTest
{
	@Test
	public void parseSourceForIdentifier()
	{
		try
		{
			String src="b";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source instanceof TableSource);
			assertEquals("b", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForNonParethesizedSubselect()
	{
		try
		{
			String src="SELECT x FROM y";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			SourceListParser.getInstance().parseSource(tokenizer);
			fail("Expected UnexpectedTokenException");
		}
		catch (UnexpectedTokenException e)
		{
			assertEquals(SqlToken.Type.KEYWORD_SELECT, e.getToken().getType());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForParenthesizedSubselect()
	{
		try
		{
			String src="(SELECT x FROM y)";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source.getClass().getName(), source instanceof ParehentesizedSource);
			assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)source).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForAliasedParenthesizedSubselect()
	{
		try
		{
			String src="(SELECT x FROM y) AS z";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source instanceof AliasedSource);
			assertEquals("z", ((AliasedSource)source).getAlias());
			assertTrue(((AliasedSource)source).getSource() instanceof ParehentesizedSource);
			assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)(((AliasedSource)source).getSource())).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForIncompleteJoinedTables()
	{
		try
		{
			String src="a INNER JOIN b";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			SourceListParser.getInstance().parseSource(tokenizer);
			fail("Expecting IncompleteJoinException");
		}
		catch (IncompleteJoinException e)
		{
			// OK
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForInnerJoinedTablesWithOnEquals()
	{
		try
		{
			String src="a INNER JOIN b ON c=d";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			assertEquals(OnJoinedSource.Type.INNER, (onJoinedSource).getType());
			Source leftSource=(onJoinedSource).getLeftSource();
			assertTrue(leftSource instanceof TableSource);
			assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=(onJoinedSource).getRightSource();
			assertTrue(rightSource instanceof TableSource);
			assertEquals("b", ((TableSource)rightSource).getTable());
			assertTrue(onJoinedSource.getOn() instanceof EqualsExpression);
			assertEquals("c", ((IdentifierExpression)(((EqualsExpression)onJoinedSource.getOn()).getExpression1())).getIdentifier());
			assertEquals("d", ((IdentifierExpression)(((EqualsExpression)onJoinedSource.getOn()).getExpression2())).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForInnerJoinedTablesWithOnGreater()
	{
		try
		{
			String src="a INNER JOIN b ON c>d";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			assertEquals(OnJoinedSource.Type.INNER, (onJoinedSource).getType());
			Source leftSource=(onJoinedSource).getLeftSource();
			assertTrue(leftSource instanceof TableSource);
			assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=(onJoinedSource).getRightSource();
			assertTrue(rightSource instanceof TableSource);
			assertEquals("b", ((TableSource)rightSource).getTable());
			assertTrue(onJoinedSource.getOn() instanceof GreaterThanExpression);
			assertEquals("c", ((IdentifierExpression)(((GreaterThanExpression)onJoinedSource.getOn()).getExpression1())).getIdentifier());
			assertEquals("d", ((IdentifierExpression)(((GreaterThanExpression)onJoinedSource.getOn()).getExpression2())).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForInnerJoinedTablesWithUsing()
	{
		try
		{
			String src="a INNER JOIN b USING c";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source instanceof UsingJoinedSource);
			UsingJoinedSource usingJoinedSource=(UsingJoinedSource)source;
			assertEquals(UsingJoinedSource.Type.INNER, (usingJoinedSource).getType());
			Source leftSource=(usingJoinedSource).getLeftSource();
			assertTrue(leftSource instanceof TableSource);
			assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=(usingJoinedSource).getRightSource();
			assertTrue(rightSource instanceof TableSource);
			assertEquals("b", ((TableSource)rightSource).getTable());
			assertTrue(usingJoinedSource.getUsing() instanceof IdentifierExpression);
			assertEquals("c", ((IdentifierExpression)usingJoinedSource.getUsing()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForOuterJoinedTablesWithOn()
	{
		try
		{
			String src="a OUTER JOIN b ON c=d";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			assertEquals(OnJoinedSource.Type.OUTER, (onJoinedSource).getType());
			Source leftSource=(onJoinedSource).getLeftSource();
			assertTrue(leftSource instanceof TableSource);
			assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=(onJoinedSource).getRightSource();
			assertTrue(rightSource instanceof TableSource);
			assertEquals("b", ((TableSource)rightSource).getTable());
			assertTrue(onJoinedSource.getOn() instanceof EqualsExpression);
			assertEquals("c", ((IdentifierExpression)(((EqualsExpression)onJoinedSource.getOn()).getExpression1())).getIdentifier());
			assertEquals("d", ((IdentifierExpression)(((EqualsExpression)onJoinedSource.getOn()).getExpression2())).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForLeftJoinedTablesWithOn()
	{
		try
		{
			String src="a LEFT JOIN b ON c=d";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			assertEquals(OnJoinedSource.Type.LEFT, (onJoinedSource).getType());
			Source leftSource=(onJoinedSource).getLeftSource();
			assertTrue(leftSource instanceof TableSource);
			assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=(onJoinedSource).getRightSource();
			assertTrue(rightSource instanceof TableSource);
			assertEquals("b", ((TableSource)rightSource).getTable());
			assertTrue(onJoinedSource.getOn() instanceof EqualsExpression);
			assertEquals("c", ((IdentifierExpression)(((EqualsExpression)onJoinedSource.getOn()).getExpression1())).getIdentifier());
			assertEquals("d", ((IdentifierExpression)(((EqualsExpression)onJoinedSource.getOn()).getExpression2())).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForRightJoinedTablesWithOn()
	{
		try
		{
			String src="a RIGHT JOIN b ON c=d";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			assertEquals(OnJoinedSource.Type.RIGHT, (onJoinedSource).getType());
			Source leftSource=(onJoinedSource).getLeftSource();
			assertTrue(leftSource instanceof TableSource);
			assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=(onJoinedSource).getRightSource();
			assertTrue(rightSource instanceof TableSource);
			assertEquals("b", ((TableSource)rightSource).getTable());
			assertTrue(onJoinedSource.getOn() instanceof EqualsExpression);
			assertEquals("c", ((IdentifierExpression)(((EqualsExpression)onJoinedSource.getOn()).getExpression1())).getIdentifier());
			assertEquals("d", ((IdentifierExpression)(((EqualsExpression)onJoinedSource.getOn()).getExpression2())).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForMissingJoin()
	{
		try
		{
			String src="a INNER";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			SourceListParser.getInstance().parseSource(tokenizer);
			fail("Expected IncompleteJoinException");
		}
		catch (IncompleteJoinException e)
		{
			// OK.
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForJoinMissingRightSource()
	{
		try
		{
			String src="a INNER JOIN";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			SourceListParser.getInstance().parseSource(tokenizer);
			fail("Expected IncompleteJoinException");
		}
		catch (IncompleteJoinException e)
		{
			// OK.
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseSourceForJoinMissingOn()
	{
		try
		{
			String src="a INNER JOIN b";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			SourceListParser.getInstance().parseSource(tokenizer);
			fail("Expected IncompleteJoinException");
		}
		catch (IncompleteJoinException e)
		{
			// OK.
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseIdentifier()
	{
		try
		{
			String src="b";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			assertEquals(1, sources.size());
			Source source=sources.get(0);
			assertTrue(source instanceof TableSource);
			assertEquals("b", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseParenthesizedSubselect()
	{
		try
		{
			String src="(SELECT x FROM y)";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			assertEquals(1, sources.size());
			Source source=sources.get(0);
			assertSame(ParehentesizedSource.class, source.getClass());
			assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)source).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseTwoIdentifiers()
	{
		try
		{
			String src="b,c";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			assertEquals(2, sources.size());
			Source source=sources.get(0);
			assertTrue(source instanceof TableSource);
			assertEquals("b", ((TableSource)source).getTable());
			source=sources.get(1);
			assertTrue(source instanceof TableSource);
			assertEquals("c", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseAliasedIdentifierWithAs()
	{
		try
		{
			String src="mytable AS m1";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			assertEquals(1, sources.size());
			Source source=sources.get(0);
			assertTrue(source instanceof AliasedSource);
			AliasedSource aliasedSource=(AliasedSource)source;
			assertTrue(aliasedSource.getSource() instanceof TableSource);
			assertEquals("mytable", ((TableSource)aliasedSource.getSource()).getTable());
			assertEquals("m1", aliasedSource.getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseAliasedIdentifierWithoutAs()
	{
		try
		{
			String src="mytable m1";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			assertEquals(1, sources.size());
			Source source=sources.get(0);
			assertTrue(source instanceof AliasedSource);
			AliasedSource aliasedSource=(AliasedSource)source;
			assertTrue(aliasedSource.getSource() instanceof TableSource);
			assertEquals("mytable", ((TableSource)aliasedSource.getSource()).getTable());
			assertEquals("m1", aliasedSource.getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseQuotedAliasedIdentifierWithoutAs()
	{
		try
		{
			String src="mytable \"m1 m2\"";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			assertEquals(1, sources.size());
			Source source=sources.get(0);
			assertTrue(source instanceof AliasedSource);
			AliasedSource aliasedSource=(AliasedSource)source;
			assertTrue(aliasedSource.getSource() instanceof TableSource);
			assertEquals("mytable", ((TableSource)aliasedSource.getSource()).getTable());
			assertEquals("m1 m2", aliasedSource.getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseAliasedIdentifierAndAliasedParenthesizedSubselect()
	{
		try
		{
			String src="mytable AS m1, (SELECT x FROM y) AS m2";
			ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			assertEquals(2, sources.size());
			Source source=sources.get(0);
			assertTrue(source instanceof AliasedSource);
			AliasedSource aliasedSource=(AliasedSource)source;
			assertTrue(aliasedSource.getSource() instanceof TableSource);
			assertEquals("mytable", ((TableSource)aliasedSource.getSource()).getTable());
			assertEquals("m1", aliasedSource.getAlias());

			source=sources.get(1);
			assertTrue(source instanceof AliasedSource);
			assertEquals("m2", ((AliasedSource)source).getAlias());
			assertTrue(((AliasedSource)source).getSource() instanceof ParehentesizedSource);
			assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)(((AliasedSource)source).getSource())).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
