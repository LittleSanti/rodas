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
import com.samajackun.rodas.sql.tokenizer.SqlTokenizerSettings.CommentsBehaviour;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizerSettings.WhitespaceBehaviour;

public class SqlTokenizerTest
{
	private void test(String src, SqlToken... expectedTokens)
		throws IOException
	{
		test(src, new SqlTokenizerSettings(), expectedTokens);
	}

	private void testWithProducingTokensForComments(String src, SqlToken... expectedTokens)
		throws IOException
	{
		SqlTokenizerSettings settings=new SqlTokenizerSettings();
		settings.setCommentsBehaviour(CommentsBehaviour.PRODUCE_TOKENS);
		test(src, settings, expectedTokens);
	}

	private void testWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken(String src, SqlToken... expectedTokens)
		throws IOException
	{
		SqlTokenizerSettings settings=new SqlTokenizerSettings();
		settings.setCommentsBehaviour(CommentsBehaviour.PRODUCE_TOKENS);
		settings.setWhitespaceBehaviour(WhitespaceBehaviour.INCLUDE_IN_FOLLOWING_TOKEN);
		test(src, settings, expectedTokens);
	}

	private void test(String src, SqlTokenizerSettings settings, SqlToken... expectedTokens)
		throws IOException
	{
		try
		{
			SqlTokenizer tokenizer=new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), settings);
			SqlToken token;

			for (SqlToken expectedToken : expectedTokens)
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
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteral()
		throws IOException
	{
		String src="12";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteralPositive()
		throws IOException
	{
		String src="+12";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_PLUS, new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteralNegative()
		throws IOException
	{
		String src="-12";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_MINUS, new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void decimalNumberLiteral()
		throws IOException
	{
		String src="1.2";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.DECIMAL_NUMBER_LITERAL, "1.2") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteral()
		throws IOException
	{
		String src="9E4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "9E4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithPlusSign()
		throws IOException
	{
		String src="9E+4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "9E+4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithMinusSign()
		throws IOException
	{
		String src="9E-4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "9E-4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithMinusMinusSign()
		throws IOException
	{
		String src="9E--4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "9E--4") };
		test(src, expectedTokens);
	}

	@Test
	public void decimalExponentialNumberLiteralWithMinusSign()
		throws IOException
	{
		String src="9.2E-4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.DECIMAL_NUMBER_LITERAL, "9.2E-4") };
		test(src, expectedTokens);
	}

	@Test
	public void commentAsTokenProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, "/*enero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void commentAsToken()
		throws IOException
	{
		String src="/*enero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void commentIgnored()
		throws IOException
	{
		String src="/*enero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void blanksCommentWithProducingTokensForComments()
		throws IOException
	{
		String src=" /*enero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, "/*enero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void blanksCommentWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken()
		throws IOException
	{
		String src=" /*enero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, " /*enero*/") };
		testWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken(src, expectedTokens);
	}

	@Test
	public void blanksComment()
		throws IOException
	{
		String src=" /*enero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void commentWithAsteriskWithProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*febrero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, "/*enero*febrero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void commentWithAsterisk()
		throws IOException
	{
		String src="/*enero*febrero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void twoCommentsWithProducingTokensForComments()
		throws IOException
	{
		String src="/*enero*//*febrero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, "/*enero*/"), new SqlToken(SqlToken.Type.COMMENT, "/*febrero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void twoComments()
		throws IOException
	{
		String src="/*enero*//*febrero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void doubleQuotedTextLiteral()
		throws IOException
	{
		String src="\"enero\"";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.DOUBLE_QUOTED_TEXT_LITERAL, "\"enero\"", "enero") };
		test(src, expectedTokens);
	}

	@Test
	public void textLiteral()
		throws IOException
	{
		String src="'enero'";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.TEXT_LITERAL, "'enero'", "enero") };
		test(src, expectedTokens);
	}

	@Test
	public void textLiteralWithQuoteEscapped()
		throws IOException
	{
		String src="'o''donell'";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.TEXT_LITERAL, "'o''donell'", "o'donell") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithLetter()
		throws IOException
	{
		String src="enero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithUnderscore()
		throws IOException
	{
		String src="_enero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "_enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithDollar()
		throws IOException
	{
		String src="$enero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "$enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierWithUnderscore()
		throws IOException
	{
		String src="enero_febrero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "enero_febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierWithDollar()
		throws IOException
	{
		String src="enero$febrero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "enero$febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void prefixedIdentifier()
		throws IOException
	{
		String src="enero.febrero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "enero"), SqlToken.TOKEN_PERIOD, new SqlToken(SqlToken.Type.IDENTIFIER, "febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordInLowercase()
		throws IOException
	{
		String src="select";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_SELECT, "select") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordSelect()
		throws IOException
	{
		String src="SELECT";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_SELECT, "SELECT") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordFrom()
		throws IOException
	{
		String src="FROM";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_FROM, "FROM") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordWhere()
		throws IOException
	{
		String src="WHERE";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_WHERE, "WHERE") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordAs()
		throws IOException
	{
		String src="AS";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_AS, "AS") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordHaving()
		throws IOException
	{
		String src="HAVING";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_HAVING, "HAVING") };
		test(src, expectedTokens);
	}

	@Test
	public void asterisk()
		throws IOException
	{
		String src="*";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_ASTERISK };
		test(src, expectedTokens);
	}

	@Test
	public void distinct1()
		throws IOException
	{
		String src="<>";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_DISTINCT1 };
		test(src, expectedTokens);
	}

	@Test
	public void distinct2()
		throws IOException
	{
		String src="!=";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_DISTINCT2 };
		test(src, expectedTokens);
	}

	@Test
	public void div()
		throws IOException
	{
		String src="/";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_DIV };
		test(src, expectedTokens);
	}

	@Test
	public void equals()
		throws IOException
	{
		String src="=";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void greater()
		throws IOException
	{
		String src=">";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_GREATER };
		test(src, expectedTokens);
	}

	@Test
	public void greaterOrEquals()
		throws IOException
	{
		String src=">=";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_GREATER_OR_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void lower()
		throws IOException
	{
		String src="<";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_LOWER };
		test(src, expectedTokens);
	}

	@Test
	public void lowerOrEquals()
		throws IOException
	{
		String src="<=";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_LOWER_OR_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void plus()
		throws IOException
	{
		String src="+";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_PLUS };
		test(src, expectedTokens);
	}

	@Test
	public void minus()
		throws IOException
	{
		String src="-";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_MINUS };
		test(src, expectedTokens);
	}

	@Test
	public void parehentesesStart()
		throws IOException
	{
		String src="(";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_PAREHENTESES_START };
		test(src, expectedTokens);
	}

	@Test
	public void parehentesesEnd()
		throws IOException
	{
		String src=")";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_PAREHENTESES_END };
		test(src, expectedTokens);
	}

	// @Test
	// public void period() throws IOException
	// {
	// String src=".";
	// Token[] expectedTokens= { Token.TOKEN_PERIOD };
	// test(src, expectedTokens);
	// }

	@Test
	public void semicolon()
		throws IOException
	{
		String src=";";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_SEMICOLON };
		test(src, expectedTokens);
	}

	@Test
	public void comma()
		throws IOException
	{
		String src=",";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_COMMA };
		test(src, expectedTokens);
	}

	@Test
	public void select()
		throws IOException
	{
		String src="SELECT a, b FROM c WHERE z=10+x";
		SqlToken[] expectedTokens= {
						new SqlToken(SqlToken.Type.KEYWORD_SELECT, "SELECT"),
						new SqlToken(SqlToken.Type.IDENTIFIER, "a"),
						SqlToken.TOKEN_COMMA,
						new SqlToken(SqlToken.Type.IDENTIFIER, "b"),
						new SqlToken(SqlToken.Type.KEYWORD_FROM, "FROM"),
						new SqlToken(SqlToken.Type.IDENTIFIER, "c"),
						new SqlToken(SqlToken.Type.KEYWORD_WHERE, "WHERE"),
						new SqlToken(SqlToken.Type.IDENTIFIER, "z"),
						SqlToken.TOKEN_EQUALS,
						new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "10"),
						SqlToken.TOKEN_PLUS,
						new SqlToken(SqlToken.Type.IDENTIFIER, "x"), };
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
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.NAMED_PARAMETER, ":enero", "enero"), };
		test(src, expectedTokens);
	}

	@Test
	public void namedParameterPlusNumberLiteral()
		throws IOException
	{
		String src=":enero+12";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.NAMED_PARAMETER, ":enero", "enero"), new SqlToken(SqlToken.Type.OPERATOR_PLUS, "+"), new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "12"), };
		test(src, expectedTokens);
	}

	@Test
	public void unnamedParameter()
		throws IOException
	{
		String src="?";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.UNNAMED_PARAMETER, "?"), };
		test(src, expectedTokens);
	}

	@Test
	public void blanksKeywordSelect()
		throws IOException
	{
		String src=" SELECT";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_SELECT, "SELECT") };
		test(src, expectedTokens);
	}

	@Test
	public void operatorIs()
		throws IOException
	{
		String src="IS";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.OPERATOR_IS, "IS") };
		test(src, expectedTokens);
	}

	@Test
	public void operatorIsNot()
		throws IOException
	{
		String src="IS NOT";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.OPERATOR_IS, "IS"), new SqlToken(SqlToken.Type.OPERATOR_NOT, "NOT") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordTrue()
		throws IOException
	{
		String src="true";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.TRUE, "true") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordFalse()
		throws IOException
	{
		String src="false";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.FALSE, "false") };
		test(src, expectedTokens);
	}
}
