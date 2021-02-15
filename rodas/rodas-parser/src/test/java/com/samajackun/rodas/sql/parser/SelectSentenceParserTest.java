package com.samajackun.rodas.sql.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.AsteriskExpression;
import com.samajackun.rodas.core.model.BinaryExpression;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.CrossSource;
import com.samajackun.rodas.core.model.EqualsExpression;
import com.samajackun.rodas.core.model.FunctionCallExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.NamedParameterExpression;
import com.samajackun.rodas.core.model.OrderClause;
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
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings;

public class SelectSentenceParserTest
{
	private final ParserContext parserContext=new ParserContext();

	@Test
	public void selectOneFieldFromOneTable()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT a,b FROM t";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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

			Source source=sentence.getSource();
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT a,b FROM t,s";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);

			Source globalSource=sentence.getSource();
			assertTrue(globalSource instanceof CrossSource);
			Source source0=((CrossSource)globalSource).getSources().get(0);
			assertTrue(source0 instanceof TableSource);
			assertEquals("t", ((TableSource)source0).getTable());
			Source source1=((CrossSource)globalSource).getSources().get(1);
			assertTrue(source1 instanceof TableSource);
			assertEquals("s", ((TableSource)source1).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromInnerSelect()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a,b FROM (SELECT 120 FROM t)";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);

			Source source=sentence.getSource();
			assertTrue(source instanceof ParehentesizedSource);
			assertTrue(((ParehentesizedSource)source).getSource() instanceof SelectSentence);
			SelectSentence selectSentence=(SelectSentence)((ParehentesizedSource)source).getSource();
			assertEquals(1, selectSentence.getSelectExpressions().size());
			assertEquals("120", ((ConstantExpression)selectSentence.getSelectExpressions().get(0).getExpression()).getValue());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectOneAliasedField()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a AS x1 FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
	public void selectTwoUnaliasedFields()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, b FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(2, sentence.getSelectExpressions().size());
			assertEquals("a", sentence.getSelectExpressions().get(0).getAlias());
			assertEquals("b", sentence.getSelectExpressions().get(1).getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectTwoUnaliasedExpressions()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a+b, c+d FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(2, sentence.getSelectExpressions().size());
			assertEquals("COL_0", sentence.getSelectExpressions().get(0).getAlias());
			assertEquals("COL_1", sentence.getSelectExpressions().get(1).getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectOneAliasedExpressionAndOneUnaliased()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a AS x1, c+d FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(2, sentence.getSelectExpressions().size());
			assertEquals("x1", sentence.getSelectExpressions().get(0).getAlias());
			assertEquals("COL_0", sentence.getSelectExpressions().get(1).getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectOneAliasedExpressionWithTrickyAlias()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a+b, c+d AS COL_0 FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(2, sentence.getSelectExpressions().size());
			assertEquals("COL_1", sentence.getSelectExpressions().get(0).getAlias());
			assertEquals("COL_0", sentence.getSelectExpressions().get(1).getAlias());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectOneAliasedWithQuotesField()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a AS \"x1\" FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			Source source=sentence.getSource();
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
	public void selectFromNoTable()
		throws TokenizerException,
		IOException
	{
		String src="SELECT 120";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			Source source=sentence.getSource();
			assertNull(source);
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectAsterisk()
		throws TokenizerException,
		IOException
	{
		String src="SELECT * FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT b.* FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM (SELECT x FROM y)";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			Source source=sentence.getSource();
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM (SELECT x FROM y WHERE z1=z2)";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			Source source=sentence.getSource();
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT a";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT 1+a";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("COL_0", aliasedExpression.getAlias());
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT (1)";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("COL_0", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof ParehentesizedExpression);
			assertTrue(((ParehentesizedExpression)aliasedExpression.getExpression()).getExpression() instanceof ConstantExpression);
			assertEquals("1", ((ParehentesizedExpression)aliasedExpression.getExpression()).getExpression().toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromWhere()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b WHERE c";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT count(1) FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("COL_0", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof FunctionCallExpression);
			FunctionCallExpression functionExpression=(FunctionCallExpression)aliasedExpression.getExpression();
			assertEquals("count", ((IdentifierExpression)functionExpression.getFunctionObject()).getIdentifier());
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT count(*) FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("COL_0", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof FunctionCallExpression);
			FunctionCallExpression functionExpression=(FunctionCallExpression)aliasedExpression.getExpression();
			assertEquals("count", ((IdentifierExpression)functionExpression.getFunctionObject()).getIdentifier());
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT /*1*/a, my_fn(12*b) as z FROM b INNER JOIN (SELECT 120 y FROM t2) AS t3 ON b.x=t3.y, t4 WHERE c=a*b-c>t3.t";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(2, sentence.getSelectExpressions().size());
			AliasedExpression exp=sentence.getSelectExpressions().get(1);
			assertEquals("z", exp.getAlias());
			assertTrue("z", exp.getExpression() instanceof FunctionCallExpression);
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void group()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, count(1) FROM b GROUP BY a";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
	public void groupAndOneAggregationExpression()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, count(1) FROM b GROUP BY a";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getGroupExpressions().size());
			assertTrue(sentence.getGroupExpressions().get(0) instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)sentence.getGroupExpressions().get(0)).getIdentifier());
			assertNull(sentence.getHavingExpression());

			assertEquals(2, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;

			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());

			aliasedExpression=sentence.getSelectExpressions().get(1);
			assertTrue(aliasedExpression.getExpression() instanceof FunctionCallExpression);
			assertEquals("count", ((FunctionCallExpression)aliasedExpression.getExpression()).getFunctionObject().getName().asString());
			assertEquals(1, ((FunctionCallExpression)aliasedExpression.getExpression()).getArguments().size());
			assertEquals("1", ((FunctionCallExpression)aliasedExpression.getExpression()).getArguments().get(0).toCode());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void groupAndOneIllegalExpression()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, b FROM z GROUP BY a";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getGroupExpressions().size());
			assertTrue(sentence.getGroupExpressions().get(0) instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)sentence.getGroupExpressions().get(0)).getIdentifier());
			assertNull(sentence.getHavingExpression());

			assertEquals(2, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;

			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());

			aliasedExpression=sentence.getSelectExpressions().get(1);
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("b", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void countAsterisk()
		throws TokenizerException,
		IOException
	{
		String src="SELECT count(*) FROM b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getSelectExpressions().size());
			assertTrue(sentence.getSelectExpressions().get(0).getExpression() instanceof FunctionCallExpression);
			FunctionCallExpression functionExpression=(FunctionCallExpression)sentence.getSelectExpressions().get(0).getExpression();
			assertEquals("count", ((IdentifierExpression)functionExpression.getFunctionObject()).getIdentifier());
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, count(1) FROM b GROUP BY a HAVING count(1)>0";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertNotNull(sentence.getHavingExpression());
			assertTrue(sentence.getHavingExpression() instanceof BinaryExpression);
			BinaryExpression binaryExpression=(BinaryExpression)sentence.getHavingExpression();
			assertEquals(">", binaryExpression.getOperator());
			assertEquals("count", ((IdentifierExpression)(((FunctionCallExpression)binaryExpression.getExpression1())).getFunctionObject()).getIdentifier());
			assertEquals(1, ((FunctionCallExpression)binaryExpression.getExpression1()).getArguments().size());
			assertEquals("1", ((ConstantExpression)((FunctionCallExpression)binaryExpression.getExpression1()).getArguments().get(0)).getValue());
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
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b ORDER BY c";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getOrderClauses().size());
			OrderClause clause;
			clause=sentence.getOrderClauses().get(0);
			assertTrue(clause.getExpression() instanceof IdentifierExpression);
			assertEquals("c", ((IdentifierExpression)clause.getExpression()).getIdentifier());
			assertTrue(clause.isAscending());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void orderByTwoColumns()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b ORDER BY c,d";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(2, sentence.getOrderClauses().size());
			OrderClause clause;
			clause=sentence.getOrderClauses().get(0);
			assertTrue(clause.getExpression() instanceof IdentifierExpression);
			assertEquals("c", ((IdentifierExpression)clause.getExpression()).getIdentifier());
			assertTrue(clause.isAscending());

			clause=sentence.getOrderClauses().get(1);
			assertTrue(clause.getExpression() instanceof IdentifierExpression);
			assertEquals("d", ((IdentifierExpression)clause.getExpression()).getIdentifier());
			assertTrue(clause.isAscending());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void orderByOneColumnAsc()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b ORDER BY c ASC";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getOrderClauses().size());
			OrderClause clause;
			clause=sentence.getOrderClauses().get(0);
			assertTrue(clause.getExpression() instanceof IdentifierExpression);
			assertEquals("c", ((IdentifierExpression)clause.getExpression()).getIdentifier());
			assertTrue(clause.isAscending());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void orderByOneColumnDesc()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a FROM b ORDER BY c ASC";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getOrderClauses().size());
			OrderClause clause;
			clause=sentence.getOrderClauses().get(0);
			assertTrue(clause.getExpression() instanceof IdentifierExpression);
			assertEquals("c", ((IdentifierExpression)clause.getExpression()).getIdentifier());
			assertTrue(clause.isAscending());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void withOneAlias()
		throws TokenizerException,
		IOException
	{
		String src="WITH t AS (SELECT a FROM b) SELECT * FROM t";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
		throws TokenizerException,
		IOException
	{
		String src="WITH t AS (SELECT a FROM b), s AS (SELECT c FROM d) SELECT * FROM t,s";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
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
		throws ParserException,
		IOException
	{
		String src="SELECT a FROM b WHERE c=:P1";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
		assertTrue(sentence.getWhereExpression() instanceof EqualsExpression);
		EqualsExpression equalsExpression=(EqualsExpression)sentence.getWhereExpression();
		assertEquals("c", ((IdentifierExpression)equalsExpression.getExpression1()).getIdentifier());
		assertEquals("P1", ((NamedParameterExpression)equalsExpression.getExpression2()).getName().asString());
	}

	@Test
	public void selectFromQuoted()
		throws ParserException,
		IOException,
		IOException
	{
		String src="SELECT a FROM \"b\"";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("a", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
			assertTrue(sentence.getSource() instanceof TableSource);
			assertEquals("b", ((TableSource)sentence.getSource()).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void selectFromQuotedWithBlanks()
		throws ParserException,
		IOException,
		IOException
	{
		String src="SELECT a FROM \"c:\\enero\\febrero\\marzo y abril.mayo\"";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			assertEquals(1, sentence.getSelectExpressions().size());
			AliasedExpression aliasedExpression;
			aliasedExpression=sentence.getSelectExpressions().get(0);
			assertEquals("a", aliasedExpression.getAlias());
			assertTrue(aliasedExpression.getExpression() instanceof IdentifierExpression);
			assertEquals("a", ((IdentifierExpression)aliasedExpression.getExpression()).getIdentifier());
			assertTrue(sentence.getSource() instanceof TableSource);
			assertEquals("c:\\enero\\febrero\\marzo y abril.mayo", ((TableSource)sentence.getSource()).getTable());
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
