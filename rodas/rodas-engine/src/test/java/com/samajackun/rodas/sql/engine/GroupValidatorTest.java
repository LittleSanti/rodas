package com.samajackun.rodas.sql.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.parser.SelectSentenceParser;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings;

public class GroupValidatorTest
{
	private final ParserContext parserContext=new ParserContext();

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
			new GroupingValidator().validateGroupSelect(sentence);
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (NotAGroupedException e)
		{
			fail(e.toString());
		}
	}

	@Test
	public void groupAndOneNotGroupedAggregationExpression()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, count(1) FROM b GROUP BY z";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			new GroupingValidator().validateGroupSelect(sentence);
			fail("Expected NotAGroupedException");
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (NotAGroupedException e)
		{
			assertEquals("a", e.getExpression().toCode());
		}
	}

	@Test
	public void groupByIdentifierAndSelectByTransformedIdentifier()
		throws TokenizerException,
		IOException
	{
		String src="SELECT 1+a, count(1) FROM b GROUP BY a";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			new GroupingValidator().validateGroupSelect(sentence);
		}
		catch (ParserException | NotAGroupedException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void groupByTransformedIdentifierAndSelectByIdentifier()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a, count(1) FROM b GROUP BY 1+a";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			new GroupingValidator().validateGroupSelect(sentence);
			fail("Expected NotAGroupedException");
		}
		catch (ParserException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
		catch (NotAGroupedException e)
		{
			assertEquals("a", e.getExpression().toCode());
		}
	}

	@Test
	public void groupByTwoIdentifiers()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a,b, count(1) FROM b GROUP BY a,b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			new GroupingValidator().validateGroupSelect(sentence);
		}
		catch (ParserException | NotAGroupedException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void groupByTwoIdentifiersAndSelectWithComplexExpression()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a+b, count(1) FROM b GROUP BY a,b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			new GroupingValidator().validateGroupSelect(sentence);
		}
		catch (ParserException | NotAGroupedException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void groupByTwoIdentifiersAndSelectWithThreeComplexExpressions()
		throws TokenizerException,
		IOException
	{
		String src="SELECT a+b,a*b+2,a+(b/2)*b, count(1) FROM b GROUP BY a,b";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), new TokenizerSettings(TokenizerSettings.WhitespaceBehaviour.IGNORE, TokenizerSettings.CommentsBehaviour.INCLUDE_IN_FOLLOWING_TOKEN, TokenizerSettings.UnexpectedSymbolBehaviour.THROW_EXCEPTION)));
		try
		{
			SelectSentence sentence=SelectSentenceParser.getInstance().parse(tokenizer, this.parserContext);
			new GroupingValidator().validateGroupSelect(sentence);
		}
		catch (ParserException | NotAGroupedException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
