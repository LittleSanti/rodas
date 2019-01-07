package com.samajackun.rodas.sql.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.AsteriskExpression;
import com.samajackun.rodas.core.model.BinaryExpression;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.EqualsExpression;
import com.samajackun.rodas.core.model.FunctionExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.NamedParameterExpression;
import com.samajackun.rodas.core.model.ParehentesizedExpression;
import com.samajackun.rodas.core.model.ParehentesizedSource;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableSource;
import com.samajackun.rodas.core.model.WithDeclaration;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizerSettings;
import com.samajackun.rodas.sql.parser.tokenizer.TokenizerException;

public class SelectSentenceParserTest
{
	@Test
	public void selectOneFieldFromOneTable()
		throws TokenizerException
	{
		String src="SELECT a FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("a", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectTwoFieldsFromOneTable()
		throws TokenizerException
	{
		String src="SELECT a,b FROM t";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(2, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("a", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
			aliasedExpression=sentence.getSelectExpressions().get(1);
			assertEquals("b", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("b", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());

			assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			assertTrue(source instanceof TableSource);
			assertEquals("t", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromTwoTables()
		throws TokenizerException
	{
		String src="SELECT a,b FROM t,s";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);

			assertEquals(2, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			assertTrue(source instanceof TableSource);
			assertEquals("t", ((TableSource)source).getTable());
			source=sentence.getSourceDeclarations().get(1);
			assertTrue(source instanceof TableSource);
			assertEquals("s", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromInnerSelect()
		throws TokenizerException
	{
		String src="SELECT a,b FROM (SELECT 120 FROM t)";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);

			assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			assertTrue(source instanceof ParehentesizedSource);
			assertTrue(((ParehentesizedSource)source).getSource() instanceof SelectSentence);
			SelectSentence selectSentence=((SelectSentence)(((ParehentesizedSource)source).getSource()));
			assertEquals(1, selectSentence.getSelectExpressions().size());
			assertEquals("120", ((ConstantExpression)(selectSentence.getSelectExpressions().get(0).getExpression())).getValue());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectOneAliasedField()
		throws TokenizerException
	{
		String src="SELECT a AS x1 FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("x1", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectOneAliasedWithQuotesField()
		throws TokenizerException
	{
		String src="SELECT a AS \"x1\" FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("x1", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromOneTable()
		throws TokenizerException
	{
		String src="SELECT a FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
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
	public void selectAsterisk()
		throws TokenizerException
	{
		String src="SELECT * FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertNull(aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof AsteriskExpression);
			assertNull(((AsteriskExpression)aliasedExpression.getExpression()).getPrefix());
			assertEquals("*", ((AsteriskExpression)aliasedExpression.getExpression()).getAsterisk());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectPrefixedAsterisk()
		throws TokenizerException
	{
		String src="SELECT b.* FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertNull(aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof AsteriskExpression);
			assertEquals("b", ((AsteriskExpression)aliasedExpression.getExpression()).getPrefix());
			assertEquals("*", ((AsteriskExpression)aliasedExpression.getExpression()).getAsterisk());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromOneSubselect()
		throws TokenizerException
	{
		String src="SELECT a FROM (SELECT x FROM y)";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			assertTrue(source instanceof ParehentesizedSource);
			assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)source).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromOneSubselect2()
		throws TokenizerException
	{
		String src="SELECT a FROM (SELECT x FROM y WHERE z1=z2)";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			assertTrue(source instanceof ParehentesizedSource);
			assertEquals("(SELECT x FROM y WHERE ((z1)=(z2)))", source.toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectOneField()
		throws TokenizerException
	{
		String src="SELECT a";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("a", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectSum()
		throws TokenizerException
	{
		String src="SELECT 1+a";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertNull(aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof BinaryExpression);
			assertEquals("+", ((BinaryExpression)aliasedExpression.getExpression()).getOperator());
			assertTrue(((BinaryExpression)aliasedExpression.getExpression()).getExpression1() instanceof ConstantExpression);
			assertEquals("1", ((ConstantExpression)((BinaryExpression)aliasedExpression.getExpression()).getExpression1()).toCode());
			assertTrue(((BinaryExpression)aliasedExpression.getExpression()).getExpression2() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)((BinaryExpression)aliasedExpression.getExpression()).getExpression2()).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectParenthesizedConstant()
		throws TokenizerException
	{
		String src="SELECT (1)";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertNull(aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof ParehentesizedExpression);
			assertTrue(((ParehentesizedExpression)aliasedExpression.getExpression()).getExpression() instanceof ConstantExpression);
			assertEquals("1", (((ParehentesizedExpression)aliasedExpression.getExpression()).getExpression()).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromWhere()
		throws TokenizerException
	{
		String src="SELECT a FROM b WHERE c";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals("c", sentence.getWhereExpression().toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFunction()
		throws TokenizerException
	{
		String src="SELECT count(1) FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression=sentence.getSelectExpressions().get(0);
			assertNull(aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof FunctionExpression);
			FunctionExpression functionExpression=(FunctionExpression)aliasedExpression.getExpression();
			assertEquals("count", functionExpression.getFunction());
			assertEquals(1, functionExpression.getArguments().size());
			assertTrue(functionExpression.getArguments().get(0) instanceof ConstantExpression);
			assertEquals("1", ((ConstantExpression)functionExpression.getArguments().get(0)).getValue());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectCountAsterisk()
		throws TokenizerException
	{
		String src="SELECT count(*) FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression=sentence.getSelectExpressions().get(0);
			assertNull(aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof FunctionExpression);
			FunctionExpression functionExpression=(FunctionExpression)aliasedExpression.getExpression();
			assertEquals("count", functionExpression.getFunction());
			assertEquals(1, functionExpression.getArguments().size());
			assertTrue(functionExpression.getArguments().get(0) instanceof AsteriskExpression);
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void complex1()
		throws TokenizerException
	{
		String src="SELECT /*1*/a, my_fn(12*b) as z FROM b INNER JOIN (SELECT 120 y FROM t2) AS t3 ON b.x=t3.y, t4 WHERE c=a*b-c>t3.t";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src, new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(2, sentence.getSelectExpressions().size());
			AliasedExpression exp=sentence.getSelectExpressions().get(1);
			assertEquals("z", exp.getAlias());
			assertTrue("z", exp.getExpression() instanceof FunctionExpression);
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void group()
		throws TokenizerException
	{
		String src="SELECT a, count(1) FROM b GROUP BY a";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src, new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getGroupExpressions().size());
			assertTrue(sentence.getGroupExpressions().get(0) instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)sentence.getGroupExpressions().get(0)).getIdentifier());
			assertNull(sentence.getHavingExpression());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void countAsterisk()
		throws TokenizerException
	{
		String src="SELECT count(*) FROM b";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src, new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			assertTrue(sentence.getSelectExpressions().get(0).getExpression() instanceof FunctionExpression);
			FunctionExpression functionExpression=(FunctionExpression)sentence.getSelectExpressions().get(0).getExpression();
			assertEquals("count", functionExpression.getFunction());
			assertEquals(1, functionExpression.getArguments().size());
			assertEquals("*", functionExpression.getArguments().get(0).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void having()
		throws TokenizerException
	{
		String src="SELECT a, count(1) FROM b GROUP BY a HAVING count(1)>0";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src, new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertNotNull(sentence.getHavingExpression());
			assertTrue(sentence.getHavingExpression() instanceof BinaryExpression);
			BinaryExpression binaryExpression=(BinaryExpression)sentence.getHavingExpression();
			assertEquals(">", binaryExpression.getOperator());
			assertEquals("count", ((FunctionExpression)binaryExpression.getExpression1()).getFunction());
			assertEquals(1, ((FunctionExpression)binaryExpression.getExpression1()).getArguments().size());
			assertEquals("1", ((ConstantExpression)((FunctionExpression)binaryExpression.getExpression1()).getArguments().get(0)).getValue());
			assertEquals("0", ((ConstantExpression)binaryExpression.getExpression2()).getValue());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void orderByOneColumn()
		throws TokenizerException
	{
		String src="SELECT a FROM b ORDER BY c";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src, new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getOrderExpressions().size());
			assertTrue(sentence.getOrderExpressions().get(0) instanceof IdentifierExpression);
			assertEquals("c", ((IdentifierExpression)sentence.getOrderExpressions().get(0)).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void orderByTwoColumns()
		throws TokenizerException
	{
		String src="SELECT a FROM b ORDER BY c,d";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src, new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(2, sentence.getOrderExpressions().size());
			assertEquals("c", ((IdentifierExpression)sentence.getOrderExpressions().get(0)).getIdentifier());
			assertEquals("d", ((IdentifierExpression)sentence.getOrderExpressions().get(1)).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void withOneAlias()
		throws TokenizerException
	{
		String src="WITH t AS (SELECT a FROM b) SELECT * FROM t";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src, new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getWithDeclarations().size());
			WithDeclaration withDeclaration=sentence.getWithDeclarations().get(0);
			assertEquals("t", withDeclaration.getAlias());
			SelectSentence sentence2=withDeclaration.getSelectSentence();
			assertEquals("SELECT a FROM b", sentence2.toCode());
			assertEquals("WITH t AS (SELECT a FROM b) SELECT * FROM t", sentence.toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void withTwoAlias()
		throws TokenizerException
	{
		String src="WITH t AS (SELECT a FROM b), s AS (SELECT c FROM d) SELECT * FROM t,s";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src, new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(2, sentence.getWithDeclarations().size());
			WithDeclaration withDeclaration=sentence.getWithDeclarations().get(0);
			assertEquals("t", withDeclaration.getAlias());
			SelectSentence sentence2=withDeclaration.getSelectSentence();
			assertEquals("SELECT a FROM b", sentence2.toCode());

			withDeclaration=sentence.getWithDeclarations().get(1);
			assertEquals("s", withDeclaration.getAlias());
			sentence2=withDeclaration.getSelectSentence();
			assertEquals("SELECT c FROM d", sentence2.toCode());

			assertEquals("WITH t AS (SELECT a FROM b),s AS (SELECT c FROM d) SELECT * FROM t,s", sentence.toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromWhereParameter01()
		throws ParserException
	{
		String src="SELECT a FROM b WHERE c=:P1";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
		assertTrue(sentence.getWhereExpression() instanceof EqualsExpression);
		EqualsExpression equalsExpression=(EqualsExpression)sentence.getWhereExpression();
		assertEquals("c", ((IdentifierExpression)equalsExpression.getExpression1()).getIdentifier());
		assertEquals("P1", ((NamedParameterExpression)equalsExpression.getExpression2()).getName());
	}

	@Test
	public void selectFromQuoted()
		throws ParserException
	{
		String src="SELECT a FROM \"b\"";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("a", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
			assertTrue(sentence.getSourceDeclarations().get(0) instanceof TableSource);
			assertEquals("b", ((TableSource)(sentence.getSourceDeclarations().get(0))).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromQuotedWithBlanks()
		throws ParserException
	{
		String src="SELECT a FROM \"c:\\enero\\febrero\\marzo y abril.mayo\"";
		ParserTokenizer tokenizer=new ParserTokenizer(new SqlTokenizer(src));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("a", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
			assertTrue(sentence.getSourceDeclarations().get(0) instanceof TableSource);
			assertEquals("c:\\enero\\febrero\\marzo y abril.mayo", ((TableSource)sentence.getSourceDeclarations().get(0)).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
