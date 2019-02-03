package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.samajackun.rodas.core.model.AliasedSource;
import com.samajackun.rodas.core.model.EqualsExpression;
import com.samajackun.rodas.core.model.GreaterThanExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.OnJoinedSource;
import com.samajackun.rodas.core.model.ParehentesizedSource;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableSource;
import com.samajackun.rodas.core.model.UsingJoinedSource;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;

public class SourceListParserTest
{
	@Test
	public void parseSourceForIdentifier()
		throws IOException
	{
		try
		{
			String src="b";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForNonParethesizedSubselect()
		throws IOException
	{
		try
		{
			String src="SELECT x FROM y";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			SourceListParser.getInstance().parseSource(tokenizer);
			Assert.fail("Expected UnexpectedTokenException");
		}
		catch (UnexpectedTokenException e)
		{
			Assert.assertEquals(SqlToken.Type.KEYWORD_SELECT, e.getToken().getType());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForParenthesizedSubselect()
		throws IOException
	{
		try
		{
			String src="(SELECT x FROM y)";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source.getClass().getName(), source instanceof ParehentesizedSource);
			Assert.assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)source).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForAliasedParenthesizedSubselect()
		throws IOException
	{
		try
		{
			String src="(SELECT x FROM y) AS z";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source instanceof AliasedSource);
			Assert.assertEquals("z", ((AliasedSource)source).getAlias());
			Assert.assertTrue(((AliasedSource)source).getSource() instanceof ParehentesizedSource);
			Assert.assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)((AliasedSource)source).getSource()).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForIncompleteJoinedTables()
		throws IOException
	{
		try
		{
			String src="a INNER JOIN b";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			SourceListParser.getInstance().parseSource(tokenizer);
			Assert.fail("Expecting IncompleteJoinException");
		}
		catch (IncompleteJoinException e)
		{
			// OK
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForInnerJoinedTablesWithOnEquals()
		throws IOException
	{
		try
		{
			String src="a INNER JOIN b ON c=d";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			Assert.assertEquals(OnJoinedSource.Type.INNER, onJoinedSource.getType());
			Source leftSource=onJoinedSource.getLeftSource();
			Assert.assertTrue(leftSource instanceof TableSource);
			Assert.assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=onJoinedSource.getRightSource();
			Assert.assertTrue(rightSource instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)rightSource).getTable());
			Assert.assertTrue(onJoinedSource.getOn() instanceof EqualsExpression);
			Assert.assertEquals("c", ((IdentifierExpression)((EqualsExpression)onJoinedSource.getOn()).getExpression1()).getIdentifier());
			Assert.assertEquals("d", ((IdentifierExpression)((EqualsExpression)onJoinedSource.getOn()).getExpression2()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForInnerJoinedTablesWithOnGreater()
		throws IOException
	{
		try
		{
			String src="a INNER JOIN b ON c>d";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			Assert.assertEquals(OnJoinedSource.Type.INNER, onJoinedSource.getType());
			Source leftSource=onJoinedSource.getLeftSource();
			Assert.assertTrue(leftSource instanceof TableSource);
			Assert.assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=onJoinedSource.getRightSource();
			Assert.assertTrue(rightSource instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)rightSource).getTable());
			Assert.assertTrue(onJoinedSource.getOn() instanceof GreaterThanExpression);
			Assert.assertEquals("c", ((IdentifierExpression)((GreaterThanExpression)onJoinedSource.getOn()).getExpression1()).getIdentifier());
			Assert.assertEquals("d", ((IdentifierExpression)((GreaterThanExpression)onJoinedSource.getOn()).getExpression2()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForInnerJoinedTablesWithUsing()
		throws IOException
	{
		try
		{
			String src="a INNER JOIN b USING c";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source instanceof UsingJoinedSource);
			UsingJoinedSource usingJoinedSource=(UsingJoinedSource)source;
			Assert.assertEquals(UsingJoinedSource.Type.INNER, usingJoinedSource.getType());
			Source leftSource=usingJoinedSource.getLeftSource();
			Assert.assertTrue(leftSource instanceof TableSource);
			Assert.assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=usingJoinedSource.getRightSource();
			Assert.assertTrue(rightSource instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)rightSource).getTable());
			Assert.assertTrue(usingJoinedSource.getUsing() instanceof IdentifierExpression);
			Assert.assertEquals("c", ((IdentifierExpression)usingJoinedSource.getUsing()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForOuterJoinedTablesWithOn()
		throws IOException
	{
		try
		{
			String src="a OUTER JOIN b ON c=d";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			Assert.assertEquals(OnJoinedSource.Type.OUTER, onJoinedSource.getType());
			Source leftSource=onJoinedSource.getLeftSource();
			Assert.assertTrue(leftSource instanceof TableSource);
			Assert.assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=onJoinedSource.getRightSource();
			Assert.assertTrue(rightSource instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)rightSource).getTable());
			Assert.assertTrue(onJoinedSource.getOn() instanceof EqualsExpression);
			Assert.assertEquals("c", ((IdentifierExpression)((EqualsExpression)onJoinedSource.getOn()).getExpression1()).getIdentifier());
			Assert.assertEquals("d", ((IdentifierExpression)((EqualsExpression)onJoinedSource.getOn()).getExpression2()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForLeftJoinedTablesWithOn()
		throws IOException
	{
		try
		{
			String src="a LEFT JOIN b ON c=d";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			Assert.assertEquals(OnJoinedSource.Type.LEFT, onJoinedSource.getType());
			Source leftSource=onJoinedSource.getLeftSource();
			Assert.assertTrue(leftSource instanceof TableSource);
			Assert.assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=onJoinedSource.getRightSource();
			Assert.assertTrue(rightSource instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)rightSource).getTable());
			Assert.assertTrue(onJoinedSource.getOn() instanceof EqualsExpression);
			Assert.assertEquals("c", ((IdentifierExpression)((EqualsExpression)onJoinedSource.getOn()).getExpression1()).getIdentifier());
			Assert.assertEquals("d", ((IdentifierExpression)((EqualsExpression)onJoinedSource.getOn()).getExpression2()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForRightJoinedTablesWithOn()
		throws IOException
	{
		try
		{
			String src="a RIGHT JOIN b ON c=d";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			Source source=SourceListParser.getInstance().parseSource(tokenizer);
			Assert.assertTrue(source instanceof OnJoinedSource);
			OnJoinedSource onJoinedSource=(OnJoinedSource)source;
			Assert.assertEquals(OnJoinedSource.Type.RIGHT, onJoinedSource.getType());
			Source leftSource=onJoinedSource.getLeftSource();
			Assert.assertTrue(leftSource instanceof TableSource);
			Assert.assertEquals("a", ((TableSource)leftSource).getTable());
			Source rightSource=onJoinedSource.getRightSource();
			Assert.assertTrue(rightSource instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)rightSource).getTable());
			Assert.assertTrue(onJoinedSource.getOn() instanceof EqualsExpression);
			Assert.assertEquals("c", ((IdentifierExpression)((EqualsExpression)onJoinedSource.getOn()).getExpression1()).getIdentifier());
			Assert.assertEquals("d", ((IdentifierExpression)((EqualsExpression)onJoinedSource.getOn()).getExpression2()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForMissingJoin()
		throws IOException
	{
		try
		{
			String src="a INNER";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			SourceListParser.getInstance().parseSource(tokenizer);
			Assert.fail("Expected IncompleteJoinException");
		}
		catch (IncompleteJoinException e)
		{
			// OK.
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForJoinMissingRightSource()
		throws IOException
	{
		try
		{
			String src="a INNER JOIN";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			SourceListParser.getInstance().parseSource(tokenizer);
			Assert.fail("Expected IncompleteJoinException");
		}
		catch (IncompleteJoinException e)
		{
			// OK.
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseSourceForJoinMissingOn()
		throws IOException
	{
		try
		{
			String src="a INNER JOIN b";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			SourceListParser.getInstance().parseSource(tokenizer);
			Assert.fail("Expected IncompleteJoinException");
		}
		catch (IncompleteJoinException e)
		{
			// OK.
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseIdentifier()
		throws IOException
	{
		try
		{
			String src="b";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sources.size());
			Source source=sources.get(0);
			Assert.assertTrue(source instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseParenthesizedSubselect()
		throws IOException
	{
		try
		{
			String src="(SELECT x FROM y)";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sources.size());
			Source source=sources.get(0);
			Assert.assertSame(ParehentesizedSource.class, source.getClass());
			Assert.assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)source).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseTwoIdentifiers()
		throws IOException
	{
		try
		{
			String src="b,c";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			Assert.assertEquals(2, sources.size());
			Source source=sources.get(0);
			Assert.assertTrue(source instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)source).getTable());
			source=sources.get(1);
			Assert.assertTrue(source instanceof TableSource);
			Assert.assertEquals("c", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseAliasedIdentifierWithAs()
		throws IOException
	{
		try
		{
			String src="mytable AS m1";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sources.size());
			Source source=sources.get(0);
			Assert.assertTrue(source instanceof AliasedSource);
			AliasedSource aliasedSource=(AliasedSource)source;
			Assert.assertTrue(aliasedSource.getSource() instanceof TableSource);
			Assert.assertEquals("mytable", ((TableSource)aliasedSource.getSource()).getTable());
			Assert.assertEquals("m1", aliasedSource.getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseAliasedIdentifierWithoutAs()
		throws IOException
	{
		try
		{
			String src="mytable m1";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sources.size());
			Source source=sources.get(0);
			Assert.assertTrue(source instanceof AliasedSource);
			AliasedSource aliasedSource=(AliasedSource)source;
			Assert.assertTrue(aliasedSource.getSource() instanceof TableSource);
			Assert.assertEquals("mytable", ((TableSource)aliasedSource.getSource()).getTable());
			Assert.assertEquals("m1", aliasedSource.getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseQuotedAliasedIdentifierWithoutAs()
		throws IOException
	{
		try
		{
			String src="mytable \"m1 m2\"";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sources.size());
			Source source=sources.get(0);
			Assert.assertTrue(source instanceof AliasedSource);
			AliasedSource aliasedSource=(AliasedSource)source;
			Assert.assertTrue(aliasedSource.getSource() instanceof TableSource);
			Assert.assertEquals("mytable", ((TableSource)aliasedSource.getSource()).getTable());
			Assert.assertEquals("m1 m2", aliasedSource.getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseAliasedIdentifierAndAliasedParenthesizedSubselect()
		throws IOException
	{
		try
		{
			String src="mytable AS m1, (SELECT x FROM y) AS m2";
			SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
			List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
			Assert.assertEquals(2, sources.size());
			Source source=sources.get(0);
			Assert.assertTrue(source instanceof AliasedSource);
			AliasedSource aliasedSource=(AliasedSource)source;
			Assert.assertTrue(aliasedSource.getSource() instanceof TableSource);
			Assert.assertEquals("mytable", ((TableSource)aliasedSource.getSource()).getTable());
			Assert.assertEquals("m1", aliasedSource.getAlias());

			source=sources.get(1);
			Assert.assertTrue(source instanceof AliasedSource);
			Assert.assertEquals("m2", ((AliasedSource)source).getAlias());
			Assert.assertTrue(((AliasedSource)source).getSource() instanceof ParehentesizedSource);
			Assert.assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)((AliasedSource)source).getSource()).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}
}
