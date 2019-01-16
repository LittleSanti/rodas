package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import org.junit.Assert;
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
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizerSettings;

public class SelectSentenceParserTest
{
	@Test
	public void selectOneFieldFromOneTable()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertEquals("a", aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectTwoFieldsFromOneTable()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a,b FROM t";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(2, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertEquals("a", aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
			aliasedExpression=sentence.getSelectExpressions().get(1);
			Assert.assertEquals("b", aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			Assert.assertEquals("b", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());

			Assert.assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			Assert.assertTrue(source instanceof TableSource);
			Assert.assertEquals("t", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFromTwoTables()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a,b FROM t,s";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);

			Assert.assertEquals(2, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			Assert.assertTrue(source instanceof TableSource);
			Assert.assertEquals("t", ((TableSource)source).getTable());
			source=sentence.getSourceDeclarations().get(1);
			Assert.assertTrue(source instanceof TableSource);
			Assert.assertEquals("s", ((TableSource)source).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFromInnerSelect()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a,b FROM (SELECT 120 FROM t)";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);

			Assert.assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			Assert.assertTrue(source instanceof ParehentesizedSource);
			Assert.assertTrue(((ParehentesizedSource)source).getSource() instanceof SelectSentence);
			SelectSentence selectSentence=(SelectSentence)((ParehentesizedSource)source).getSource();
			Assert.assertEquals(1, selectSentence.getSelectExpressions().size());
			Assert.assertEquals("120", ((ConstantExpression)selectSentence.getSelectExpressions().get(0).getExpression()).getValue());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectOneAliasedField()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a AS x1 FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertEquals("x1", aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectOneAliasedWithQuotesField()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a AS \"x1\" FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertEquals("x1", aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFromOneTable()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
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
	public void selectAsterisk()
		throws TokenizerException,
		IOException
	{
		String src="SELECT * FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertNull(aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof AsteriskExpression);
			Assert.assertNull(((AsteriskExpression)aliasedExpression.getExpression()).getPrefix());
			Assert.assertEquals("*", ((AsteriskExpression)aliasedExpression.getExpression()).getAsterisk());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectPrefixedAsterisk()
		throws TokenizerException,
		IOException
	{
		String src="SELECT b.* FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertNull(aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof AsteriskExpression);
			Assert.assertEquals("b", ((AsteriskExpression)aliasedExpression.getExpression()).getPrefix());
			Assert.assertEquals("*", ((AsteriskExpression)aliasedExpression.getExpression()).getAsterisk());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFromOneSubselect()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM (SELECT x FROM y)";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			Assert.assertTrue(source instanceof ParehentesizedSource);
			Assert.assertEquals("(SELECT x FROM y)", ((ParehentesizedSource)source).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFromOneSubselect2()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM (SELECT x FROM y WHERE z1=z2)";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSourceDeclarations().size());
			Source source=sentence.getSourceDeclarations().get(0);
			Assert.assertTrue(source instanceof ParehentesizedSource);
			Assert.assertEquals("(SELECT x FROM y WHERE ((z1)=(z2)))", source.toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectOneField()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertEquals("a", aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectSum()
		throws TokenizerException,
		IOException
	{
		String src="SELECT 1+a";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertNull(aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof BinaryExpression);
			Assert.assertEquals("+", ((BinaryExpression)aliasedExpression.getExpression()).getOperator());
			Assert.assertTrue(((BinaryExpression)aliasedExpression.getExpression()).getExpression1() instanceof ConstantExpression);
			Assert.assertEquals("1", ((ConstantExpression)((BinaryExpression)aliasedExpression.getExpression()).getExpression1()).toCode());
			Assert.assertTrue(((BinaryExpression)aliasedExpression.getExpression()).getExpression2() instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)((BinaryExpression)aliasedExpression.getExpression()).getExpression2()).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectParenthesizedConstant()
		throws TokenizerException,
		IOException
	{
		String src="SELECT (1)";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertNull(aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof ParehentesizedExpression);
			Assert.assertTrue(((ParehentesizedExpression)aliasedExpression.getExpression()).getExpression() instanceof ConstantExpression);
			Assert.assertEquals("1", ((ParehentesizedExpression)aliasedExpression.getExpression()).getExpression().toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFromWhere()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b WHERE c";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals("c", sentence.getWhereExpression().toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFunction()
		throws TokenizerException,
		IOException
	{
		String src="SELECT count(1) FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertNull(aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof FunctionExpression);
			FunctionExpression functionExpression=(FunctionExpression)aliasedExpression.getExpression();
			Assert.assertEquals("count", functionExpression.getFunction());
			Assert.assertEquals(1, functionExpression.getArguments().size());
			Assert.assertTrue(functionExpression.getArguments().get(0) instanceof ConstantExpression);
			Assert.assertEquals("1", ((ConstantExpression)functionExpression.getArguments().get(0)).getValue());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectCountAsterisk()
		throws TokenizerException,
		IOException
	{
		String src="SELECT count(*) FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertNull(aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof FunctionExpression);
			FunctionExpression functionExpression=(FunctionExpression)aliasedExpression.getExpression();
			Assert.assertEquals("count", functionExpression.getFunction());
			Assert.assertEquals(1, functionExpression.getArguments().size());
			Assert.assertTrue(functionExpression.getArguments().get(0) instanceof AsteriskExpression);
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void complex1()
		throws TokenizerException,
		IOException
	{
		String src="SELECT /*1*/a, my_fn(12*b) as z FROM b INNER JOIN (SELECT 120 y FROM t2) AS t3 ON b.x=t3.y, t4 WHERE c=a*b-c>t3.t";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(2, sentence.getSelectExpressions().size());
			AliasedExpression exp=sentence.getSelectExpressions().get(1);
			Assert.assertEquals("z", exp.getAlias());
			Assert.assertTrue("z", exp.getExpression() instanceof FunctionExpression);
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void group()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, count(1) FROM b GROUP BY a";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getGroupExpressions().size());
			Assert.assertTrue(sentence.getGroupExpressions().get(0) instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)sentence.getGroupExpressions().get(0)).getIdentifier());
			Assert.assertNull(sentence.getHavingExpression());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void countAsterisk()
		throws TokenizerException,
		IOException
	{
		String src="SELECT count(*) FROM b";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			Assert.assertTrue(sentence.getSelectExpressions().get(0).getExpression() instanceof FunctionExpression);
			FunctionExpression functionExpression=(FunctionExpression)sentence.getSelectExpressions().get(0).getExpression();
			Assert.assertEquals("count", functionExpression.getFunction());
			Assert.assertEquals(1, functionExpression.getArguments().size());
			Assert.assertEquals("*", functionExpression.getArguments().get(0).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void having()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, count(1) FROM b GROUP BY a HAVING count(1)>0";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertNotNull(sentence.getHavingExpression());
			Assert.assertTrue(sentence.getHavingExpression() instanceof BinaryExpression);
			BinaryExpression binaryExpression=(BinaryExpression)sentence.getHavingExpression();
			Assert.assertEquals(">", binaryExpression.getOperator());
			Assert.assertEquals("count", ((FunctionExpression)binaryExpression.getExpression1()).getFunction());
			Assert.assertEquals(1, ((FunctionExpression)binaryExpression.getExpression1()).getArguments().size());
			Assert.assertEquals("1", ((ConstantExpression)((FunctionExpression)binaryExpression.getExpression1()).getArguments().get(0)).getValue());
			Assert.assertEquals("0", ((ConstantExpression)binaryExpression.getExpression2()).getValue());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void orderByOneColumn()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b ORDER BY c";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getOrderExpressions().size());
			Assert.assertTrue(sentence.getOrderExpressions().get(0) instanceof IdentifierExpression);
			Assert.assertEquals("c", ((IdentifierExpression)sentence.getOrderExpressions().get(0)).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void orderByTwoColumns()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b ORDER BY c,d";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(2, sentence.getOrderExpressions().size());
			Assert.assertEquals("c", ((IdentifierExpression)sentence.getOrderExpressions().get(0)).getIdentifier());
			Assert.assertEquals("d", ((IdentifierExpression)sentence.getOrderExpressions().get(1)).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void withOneAlias()
		throws TokenizerException,
		IOException
	{
		String src="WITH t AS (SELECT a FROM b) SELECT * FROM t";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getWithDeclarations().size());
			WithDeclaration withDeclaration=sentence.getWithDeclarations().get(0);
			Assert.assertEquals("t", withDeclaration.getAlias());
			SelectSentence sentence2=withDeclaration.getSelectSentence();
			Assert.assertEquals("SELECT a FROM b", sentence2.toCode());
			Assert.assertEquals("WITH t AS (SELECT a FROM b) SELECT * FROM t", sentence.toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void withTwoAlias()
		throws TokenizerException,
		IOException
	{
		String src="WITH t AS (SELECT a FROM b), s AS (SELECT c FROM d) SELECT * FROM t,s";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new SqlTokenizerSettings(SqlTokenizerSettings.WhitespaceBehaviour.IGNORE, SqlTokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(2, sentence.getWithDeclarations().size());
			WithDeclaration withDeclaration=sentence.getWithDeclarations().get(0);
			Assert.assertEquals("t", withDeclaration.getAlias());
			SelectSentence sentence2=withDeclaration.getSelectSentence();
			Assert.assertEquals("SELECT a FROM b", sentence2.toCode());

			withDeclaration=sentence.getWithDeclarations().get(1);
			Assert.assertEquals("s", withDeclaration.getAlias());
			sentence2=withDeclaration.getSelectSentence();
			Assert.assertEquals("SELECT c FROM d", sentence2.toCode());

			Assert.assertEquals("WITH t AS (SELECT a FROM b),s AS (SELECT c FROM d) SELECT * FROM t,s", sentence.toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFromWhereParameter01()
		throws ParserException,
		IOException
	{
		String src="SELECT a FROM b WHERE c=:P1";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
		Assert.assertTrue(sentence.getWhereExpression() instanceof EqualsExpression);
		EqualsExpression equalsExpression=(EqualsExpression)sentence.getWhereExpression();
		Assert.assertEquals("c", ((IdentifierExpression)equalsExpression.getExpression1()).getIdentifier());
		Assert.assertEquals("P1", ((NamedParameterExpression)equalsExpression.getExpression2()).getName());
	}

	@Test
	public void selectFromQuoted()
		throws ParserException,
		IOException,
		IOException
	{
		String src="SELECT a FROM \"b\"";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertEquals("a", aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
			Assert.assertTrue(sentence.getSourceDeclarations().get(0) instanceof TableSource);
			Assert.assertEquals("b", ((TableSource)sentence.getSourceDeclarations().get(0)).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void selectFromQuotedWithBlanks()
		throws ParserException,
		IOException,
		IOException
	{
		String src="SELECT a FROM \"c:\\enero\\febrero\\marzo y abril.mayo\"";
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer);
			Assert.assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			Assert.assertEquals("a", aliasedExpression.getAlias());
			Assert.assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			Assert.assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
			Assert.assertTrue(sentence.getSourceDeclarations().get(0) instanceof TableSource);
			Assert.assertEquals("c:\\enero\\febrero\\marzo y abril.mayo", ((TableSource)sentence.getSourceDeclarations().get(0)).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}
}
