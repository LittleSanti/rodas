package com.samajackun.rodas.sql.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.parsing.tokenizer.UnclosedCommentException;
import com.samajackun.rodas.parsing.tokenizer.UnclosedTextLiteralException;
import com.samajackun.rodas.parsing.tokenizer.UnexpectedSymbolException;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings.CommentsBehaviour;
import com.samajackun.rodas.sql.tokenizer.TokenizerSettings.WhitespaceBehaviour;

public class SqlTokenizerTest
{
	private void test(String src, Token... expectedTokens)
		throws IOException
	{
		test(src, new TokenizerSettings(), expectedTokens);
	}

	private void testWithProducingTokensForComments(String src, Token... expectedTokens)
		throws IOException
	{
		TokenizerSettings settings=new TokenizerSettings();
		settings.setCommentsBehaviour(CommentsBehaviour.PRODUCE_TOKENS);
		test(src, settings, expectedTokens);
	}

	private void testWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken(String src, Token... expectedTokens)
		throws IOException
	{
		TokenizerSettings settings=new TokenizerSettings();
		settings.setCommentsBehaviour(CommentsBehaviour.PRODUCE_TOKENS);
		settings.setWhitespaceBehaviour(WhitespaceBehaviour.INCLUDE_IN_FOLLOWING_TOKEN);
		test(src, settings, expectedTokens);
	}

	private void test(String src, TokenizerSettings settings, Token... expectedTokens)
		throws IOException
	{
		try
		{
			SqlTokenizer tokenizer=new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			Token token;

			for (Token expectedToken : expectedTokens)
			{
				token=tokenizer.nextToken();
				assertNotNull(token);
				assertEquals(expectedToken, token);
			}
			assertNull(tokenizer.nextToken());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void empty()
		throws IOException
	{
		String src="";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteral()
		throws IOException
	{
		String src="12";
		Token[] expectedTokens= { new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteralPositive()
		throws IOException
	{
		String src="+12";
		Token[] expectedTokens= { SqlTokens.TOKEN_PLUS, new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteralNegative()
		throws IOException
	{
		String src="-12";
		Token[] expectedTokens= { SqlTokens.TOKEN_MINUS, new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void decimalNumberLiteral()
		throws IOException
	{
		String src="1.2";
		Token[] expectedTokens= { new Token(SqlTokenTypes.DECIMAL_NUMBER_LITERAL, "1.2") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteral()
		throws IOException
	{
		String src="9E4";
		Token[] expectedTokens= { new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "9E4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithPlusSign()
		throws IOException
	{
		String src="9E+4";
		Token[] expectedTokens= { new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "9E+4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithMinusSign()
		throws IOException
	{
		String src="9E-4";
		Token[] expectedTokens= { new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "9E-4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithMinusMinusSign()
		throws IOException
	{
		String src="9E--4";
		Token[] expectedTokens= { new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "9E--4") };
		test(src, expectedTokens);
	}

	@Test
	public void decimalExponentialNumberLiteralWithMinusSign()
		throws IOException
	{
		String src="9.2E-4";
		Token[] expectedTokens= { new Token(SqlTokenTypes.DECIMAL_NUMBER_LITERAL, "9.2E-4") };
		test(src, expectedTokens);
	}

	@Test
	public void commentAsTokenProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*/";
		Token[] expectedTokens= { new Token(SqlTokenTypes.COMMENT, "/*enero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void commentAsToken()
		throws IOException
	{
		String src="/*enero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void commentIgnored()
		throws IOException
	{
		String src="/*enero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void blanksCommentWithProducingTokensForComments()
		throws IOException
	{
		String src=" /*enero*/";
		Token[] expectedTokens= { new Token(SqlTokenTypes.COMMENT, "/*enero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void blanksCommentWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken()
		throws IOException
	{
		String src=" /*enero*/";
		Token[] expectedTokens= { new Token(SqlTokenTypes.COMMENT, " /*enero*/") };
		testWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken(src, expectedTokens);
	}

	@Test
	public void blanksComment()
		throws IOException
	{
		String src=" /*enero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void commentWithAsteriskWithProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*febrero*/";
		Token[] expectedTokens= { new Token(SqlTokenTypes.COMMENT, "/*enero*febrero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void commentWithAsterisk()
		throws IOException
	{
		String src="/*enero*febrero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void twoCommentsWithProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*//*febrero*/";
		Token[] expectedTokens= { new Token(SqlTokenTypes.COMMENT, "/*enero*/"), new Token(SqlTokenTypes.COMMENT, "/*febrero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void twoComments()
		throws IOException
	{
		String src="/*enero*//*febrero*/";
		Token[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void doubleQuotedTextLiteral()
		throws IOException
	{
		String src="\"enero\"";
		Token[] expectedTokens= { new Token(SqlTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL, "\"enero\"", "enero") };
		test(src, expectedTokens);
	}

	@Test
	public void textLiteral()
		throws IOException
	{
		String src="'enero'";
		Token[] expectedTokens= { new Token(SqlTokenTypes.TEXT_LITERAL, "'enero'", "enero") };
		test(src, expectedTokens);
	}

	@Test
	public void textLiteralWithQuoteEscapped()
		throws IOException
	{
		String src="'o''donell'";
		Token[] expectedTokens= { new Token(SqlTokenTypes.TEXT_LITERAL, "'o''donell'", "o'donell") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithLetter()
		throws IOException
	{
		String src="enero";
		Token[] expectedTokens= { new Token(SqlTokenTypes.IDENTIFIER, "enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithUnderscore()
		throws IOException
	{
		String src="_enero";
		Token[] expectedTokens= { new Token(SqlTokenTypes.IDENTIFIER, "_enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithDollar()
		throws IOException
	{
		String src="$enero";
		Token[] expectedTokens= { new Token(SqlTokenTypes.IDENTIFIER, "$enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierWithUnderscore()
		throws IOException
	{
		String src="enero_febrero";
		Token[] expectedTokens= { new Token(SqlTokenTypes.IDENTIFIER, "enero_febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierWithDollar()
		throws IOException
	{
		String src="enero$febrero";
		Token[] expectedTokens= { new Token(SqlTokenTypes.IDENTIFIER, "enero$febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void prefixedIdentifier()
		throws IOException
	{
		String src="enero.febrero";
		Token[] expectedTokens= { new Token(SqlTokenTypes.IDENTIFIER, "enero"), SqlTokens.TOKEN_PERIOD, new Token(SqlTokenTypes.IDENTIFIER, "febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordInLowercase()
		throws IOException
	{
		String src="select";
		Token[] expectedTokens= { new Token(SqlTokenTypes.KEYWORD_SELECT, "select") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordSelect()
		throws IOException
	{
		String src="SELECT";
		Token[] expectedTokens= { new Token(SqlTokenTypes.KEYWORD_SELECT, "SELECT") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordFrom()
		throws IOException
	{
		String src="FROM";
		Token[] expectedTokens= { new Token(SqlTokenTypes.KEYWORD_FROM, "FROM") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordWhere()
		throws IOException
	{
		String src="WHERE";
		Token[] expectedTokens= { new Token(SqlTokenTypes.KEYWORD_WHERE, "WHERE") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordAs()
		throws IOException
	{
		String src="AS";
		Token[] expectedTokens= { new Token(SqlTokenTypes.KEYWORD_AS, "AS") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordHaving()
		throws IOException
	{
		String src="HAVING";
		Token[] expectedTokens= { new Token(SqlTokenTypes.KEYWORD_HAVING, "HAVING") };
		test(src, expectedTokens);
	}

	@Test
	public void asterisk()
		throws IOException
	{
		String src="*";
		Token[] expectedTokens= { SqlTokens.TOKEN_ASTERISK };
		test(src, expectedTokens);
	}

	@Test
	public void distinct1()
		throws IOException
	{
		String src="<>";
		Token[] expectedTokens= { SqlTokens.TOKEN_DISTINCT1 };
		test(src, expectedTokens);
	}

	@Test
	public void distinct2()
		throws IOException
	{
		String src="!=";
		Token[] expectedTokens= { SqlTokens.TOKEN_DISTINCT2 };
		test(src, expectedTokens);
	}

	@Test
	public void div()
		throws IOException
	{
		String src="/";
		Token[] expectedTokens= { SqlTokens.TOKEN_DIV };
		test(src, expectedTokens);
	}

	@Test
	public void equals()
		throws IOException
	{
		String src="=";
		Token[] expectedTokens= { SqlTokens.TOKEN_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void greater()
		throws IOException
	{
		String src=">";
		Token[] expectedTokens= { SqlTokens.TOKEN_GREATER };
		test(src, expectedTokens);
	}

	@Test
	public void greaterOrEquals()
		throws IOException
	{
		String src=">=";
		Token[] expectedTokens= { SqlTokens.TOKEN_GREATER_OR_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void lower()
		throws IOException
	{
		String src="<";
		Token[] expectedTokens= { SqlTokens.TOKEN_LOWER };
		test(src, expectedTokens);
	}

	@Test
	public void lowerOrEquals()
		throws IOException
	{
		String src="<=";
		Token[] expectedTokens= { SqlTokens.TOKEN_LOWER_OR_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void plus()
		throws IOException
	{
		String src="+";
		Token[] expectedTokens= { SqlTokens.TOKEN_PLUS };
		test(src, expectedTokens);
	}

	@Test
	public void minus()
		throws IOException
	{
		String src="-";
		Token[] expectedTokens= { SqlTokens.TOKEN_MINUS };
		test(src, expectedTokens);
	}

	@Test
	public void parehentesesStart()
		throws IOException
	{
		String src="(";
		Token[] expectedTokens= { SqlTokens.TOKEN_PAREHENTESES_START };
		test(src, expectedTokens);
	}

	@Test
	public void parehentesesEnd()
		throws IOException
	{
		String src=")";
		Token[] expectedTokens= { SqlTokens.TOKEN_PAREHENTESES_END };
		test(src, expectedTokens);
	}

	// @Test
	// public void period() throws IOException
	// {
	// String src=".";
	// Token[] expectedTokens= { SqlTokens.TOKEN_PERIOD };
	// test(src, expectedTokens);
	// }

	@Test
	public void semicolon()
		throws IOException
	{
		String src=";";
		Token[] expectedTokens= { SqlTokens.TOKEN_SEMICOLON };
		test(src, expectedTokens);
	}

	@Test
	public void comma()
		throws IOException
	{
		String src=",";
		Token[] expectedTokens= { SqlTokens.TOKEN_COMMA };
		test(src, expectedTokens);
	}

	@Test
	public void select()
		throws IOException
	{
		String src="SELECT a, b FROM c WHERE z=10+x";
		Token[] expectedTokens= {
						new Token(SqlTokenTypes.KEYWORD_SELECT, "SELECT"),
						new Token(SqlTokenTypes.IDENTIFIER, "a"),
						SqlTokens.TOKEN_COMMA,
						new Token(SqlTokenTypes.IDENTIFIER, "b"),
						new Token(SqlTokenTypes.KEYWORD_FROM, "FROM"),
						new Token(SqlTokenTypes.IDENTIFIER, "c"),
						new Token(SqlTokenTypes.KEYWORD_WHERE, "WHERE"),
						new Token(SqlTokenTypes.IDENTIFIER, "z"),
						SqlTokens.TOKEN_EQUALS,
						new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "10"),
						SqlTokens.TOKEN_PLUS,
						new Token(SqlTokenTypes.IDENTIFIER, "x"), };
		test(src, expectedTokens);
	}

	@Test(expected=UnexpectedSymbolException.class)
	public void exclamation()
		throws IOException,
		TokenizerException
	{
		String src="!";
		SqlTokenizer tokenizer=new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	@Test(expected=UnexpectedSymbolException.class)
	public void angle()
		throws IOException,
		TokenizerException
	{
		String src="^";
		SqlTokenizer tokenizer=new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	@Test(expected=UnexpectedSymbolException.class)
	public void ampersand()
		throws IOException,
		TokenizerException
	{
		String src="&";
		SqlTokenizer tokenizer=new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	@Test(expected=UnclosedCommentException.class)
	public void commentUnclosed()
		throws IOException,
		TokenizerException
	{
		String src="/*enero";
		SqlTokenizer tokenizer=new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	@Test(expected=UnclosedTextLiteralException.class)
	public void textLiteralUnclosed()
		throws IOException,
		TokenizerException
	{
		String src="'enero";
		SqlTokenizer tokenizer=new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)));
		tokenizer.nextToken();
	}

	@Test
	public void readAfterLastToken()
		throws IOException,
		TokenizerException
	{
		String src="enero";
		SqlTokenizer tokenizer=new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)));
		assertNotNull(tokenizer.nextToken());
		assertNull(tokenizer.nextToken());
	}

	@Test
	public void namedParameter()
		throws IOException
	{
		String src=":enero";
		Token[] expectedTokens= { new Token(SqlTokenTypes.NAMED_PARAMETER, ":enero", "enero"), };
		test(src, expectedTokens);
	}

	@Test
	public void namedParameterPlusNumberLiteral()
		throws IOException
	{
		String src=":enero+12";
		Token[] expectedTokens= { new Token(SqlTokenTypes.NAMED_PARAMETER, ":enero", "enero"), new Token(SqlTokenTypes.OPERATOR_PLUS, "+"), new Token(SqlTokenTypes.INTEGER_NUMBER_LITERAL, "12"), };
		test(src, expectedTokens);
	}

	@Test
	public void unnamedParameter()
		throws IOException
	{
		String src="?";
		Token[] expectedTokens= { new Token(SqlTokenTypes.UNNAMED_PARAMETER, "?"), };
		test(src, expectedTokens);
	}

	@Test
	public void blanksKeywordSelect()
		throws IOException
	{
		String src=" SELECT";
		Token[] expectedTokens= { new Token(SqlTokenTypes.KEYWORD_SELECT, "SELECT") };
		test(src, expectedTokens);
	}

	@Test
	public void operatorIs()
		throws IOException
	{
		String src="IS";
		Token[] expectedTokens= { new Token(SqlTokenTypes.OPERATOR_IS, "IS") };
		test(src, expectedTokens);
	}

	@Test
	public void operatorIsNot()
		throws IOException
	{
		String src="IS NOT";
		Token[] expectedTokens= { new Token(SqlTokenTypes.OPERATOR_IS, "IS"), new Token(SqlTokenTypes.OPERATOR_NOT, "NOT") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordTrue()
		throws IOException
	{
		String src="true";
		Token[] expectedTokens= { new Token(SqlTokenTypes.TRUE, "true") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordFalse()
		throws IOException
	{
		String src="false";
		Token[] expectedTokens= { new Token(SqlTokenTypes.FALSE, "false") };
		test(src, expectedTokens);
	}
}
