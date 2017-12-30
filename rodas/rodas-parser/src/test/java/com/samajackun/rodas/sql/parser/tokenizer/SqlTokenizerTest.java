package com.samajackun.rodas.sql.parser.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizerSettings.CommentsBehaviour;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizerSettings.WhitespaceBehaviour;

public class SqlTokenizerTest
{
	private void test(String src, SqlToken... expectedTokens)
	{
		test(src, new SqlTokenizerSettings(), expectedTokens);
	}

	private void testWithProducingTokensForComments(String src, SqlToken... expectedTokens)
	{
		SqlTokenizerSettings settings=new SqlTokenizerSettings();
		settings.setCommentsBehaviour(CommentsBehaviour.PRODUCE_TOKENS);
		test(src, settings, expectedTokens);
	}

	private void testWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken(String src, SqlToken... expectedTokens)
	{
		SqlTokenizerSettings settings=new SqlTokenizerSettings();
		settings.setCommentsBehaviour(CommentsBehaviour.PRODUCE_TOKENS);
		settings.setWhitespaceBehaviour(WhitespaceBehaviour.INCLUDE_IN_FOLLOWING_TOKEN);
		test(src, settings, expectedTokens);
	}

	private void test(String src, SqlTokenizerSettings settings, SqlToken... expectedTokens)
	{
		try
		{
			SqlTokenizer tokenizer=new SqlTokenizer(src, settings);
			SqlToken token;

			for (SqlToken expectedToken : expectedTokens)
			{
				assertTrue(tokenizer.hasMoreTokens());
				token=tokenizer.nextToken();
				assertEquals(expectedToken, token);
			}
			assertFalse(tokenizer.hasMoreTokens());
		}
		catch (TokenizerException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void empty()
	{
		String src="";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteral()
	{
		String src="12";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteralPositive()
	{
		String src="+12";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_PLUS, new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void integerNumberLiteralNegative()
	{
		String src="-12";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_MINUS, new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "12") };
		test(src, expectedTokens);
	}

	@Test
	public void decimalNumberLiteral()
	{
		String src="1.2";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.DECIMAL_NUMBER_LITERAL, "1.2") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteral()
	{
		String src="9E4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "9E4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithPlusSign()
	{
		String src="9E+4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "9E+4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithMinusSign()
	{
		String src="9E-4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "9E-4") };
		test(src, expectedTokens);
	}

	@Test
	public void integerExponentialNumberLiteralWithMinusMinusSign()
	{
		String src="9E--4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "9E--4") };
		test(src, expectedTokens);
	}

	@Test
	public void decimalExponentialNumberLiteralWithMinusSign()
	{
		String src="9.2E-4";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.DECIMAL_NUMBER_LITERAL, "9.2E-4") };
		test(src, expectedTokens);
	}

	@Test
	public void commentAsTokenProducingTokensForComments()
	{
		String src="/*enero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, "/*enero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void commentAsToken()
	{
		String src="/*enero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void commentIgnored()
	{
		String src="/*enero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void blanksCommentWithProducingTokensForComments()
	{
		String src=" /*enero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, "/*enero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void blanksCommentWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken()
	{
		String src=" /*enero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, " /*enero*/") };
		testWithProducingTokensForCommentsAndIncludingWhitespaceInFollowingToken(src, expectedTokens);
	}

	@Test
	public void blanksComment()
	{
		String src=" /*enero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void commentWithAsteriskWithProducingTokensForComments()
	{
		String src="/*enero*febrero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, "/*enero*febrero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void commentWithAsterisk()
	{
		String src="/*enero*febrero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void twoCommentsWithProducingTokensForComments()
	{
		String src="/*enero*//*febrero*/";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.COMMENT, "/*enero*/"), new SqlToken(SqlToken.Type.COMMENT, "/*febrero*/") };
		testWithProducingTokensForComments(src, expectedTokens);
	}

	@Test
	public void twoComments()
	{
		String src="/*enero*//*febrero*/";
		SqlToken[] expectedTokens= {};
		test(src, expectedTokens);
	}

	@Test
	public void doubleQuotedTextLiteral()
	{
		String src="\"enero\"";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.DOUBLE_QUOTED_TEXT_LITERAL, "enero") };
		test(src, expectedTokens);
	}

	@Test
	public void textLiteral()
	{
		String src="'enero'";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.TEXT_LITERAL, "'enero'") };
		test(src, expectedTokens);
	}

	@Test
	public void textLiteralWithQuoteEscapped()
	{
		String src="'o''donell'";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.TEXT_LITERAL, "'o''donell'") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithLetter()
	{
		String src="enero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithUnderscore()
	{
		String src="_enero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "_enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierStartingWithDollar()
	{
		String src="$enero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "$enero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierWithUnderscore()
	{
		String src="enero_febrero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "enero_febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void identifierWithDollar()
	{
		String src="enero$febrero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "enero$febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void prefixedIdentifier()
	{
		String src="enero.febrero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.IDENTIFIER, "enero"), SqlToken.TOKEN_PERIOD, new SqlToken(SqlToken.Type.IDENTIFIER, "febrero") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordInLowercase()
	{
		String src="select";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_SELECT, "select") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordSelect()
	{
		String src="SELECT";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_SELECT, "SELECT") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordFrom()
	{
		String src="FROM";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_FROM, "FROM") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordWhere()
	{
		String src="WHERE";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_WHERE, "WHERE") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordAs()
	{
		String src="AS";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_AS, "AS") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordHaving()
	{
		String src="HAVING";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_HAVING, "HAVING") };
		test(src, expectedTokens);
	}

	@Test
	public void asterisk()
	{
		String src="*";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_ASTERISK };
		test(src, expectedTokens);
	}

	@Test
	public void distinct1()
	{
		String src="<>";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_DISTINCT1 };
		test(src, expectedTokens);
	}

	@Test
	public void distinct2()
	{
		String src="!=";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_DISTINCT2 };
		test(src, expectedTokens);
	}

	@Test
	public void div()
	{
		String src="/";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_DIV };
		test(src, expectedTokens);
	}

	@Test
	public void equals()
	{
		String src="=";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void greater()
	{
		String src=">";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_GREATER };
		test(src, expectedTokens);
	}

	@Test
	public void greaterOrEquals()
	{
		String src=">=";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_GREATER_OR_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void lower()
	{
		String src="<";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_LOWER };
		test(src, expectedTokens);
	}

	@Test
	public void lowerOrEquals()
	{
		String src="<=";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_LOWER_OR_EQUALS };
		test(src, expectedTokens);
	}

	@Test
	public void plus()
	{
		String src="+";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_PLUS };
		test(src, expectedTokens);
	}

	@Test
	public void minus()
	{
		String src="-";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_MINUS };
		test(src, expectedTokens);
	}

	@Test
	public void parehentesesStart()
	{
		String src="(";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_PAREHENTESES_START };
		test(src, expectedTokens);
	}

	@Test
	public void parehentesesEnd()
	{
		String src=")";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_PAREHENTESES_END };
		test(src, expectedTokens);
	}

	// @Test
	// public void period()
	// {
	// String src=".";
	// Token[] expectedTokens= { Token.TOKEN_PERIOD };
	// test(src, expectedTokens);
	// }

	@Test
	public void semicolon()
	{
		String src=";";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_SEMICOLON };
		test(src, expectedTokens);
	}

	@Test
	public void comma()
	{
		String src=",";
		SqlToken[] expectedTokens= { SqlToken.TOKEN_COMMA };
		test(src, expectedTokens);
	}

	@Test
	public void select()
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

	@Test(expected=TokenizerException.class)
	public void exclamation()
		throws TokenizerException
	{
		String src="!";
		new SqlTokenizer(src);
	}

	@Test(expected=TokenizerException.class)
	public void angle()
		throws TokenizerException
	{
		String src="^";
		new SqlTokenizer(src);
	}

	@Test(expected=TokenizerException.class)
	public void ampersand()
		throws TokenizerException
	{
		String src="&";
		new SqlTokenizer(src);
	}

	@Test(expected=TokenizerException.class)
	public void commentUnclosed()
		throws TokenizerException
	{
		String src="/*enero";
		new SqlTokenizer(src);
	}

	@Test(expected=TokenizerException.class)
	public void textLiteralUnclosed()
		throws TokenizerException
	{
		String src="'enero";
		new SqlTokenizer(src);
	}

	@Test(expected=TokenizerException.class)
	public void readAfterLastToken()
		throws TokenizerException
	{
		String src="enero";
		SqlTokenizer tokenizer=new SqlTokenizer(src);
		tokenizer.nextToken();
		tokenizer.nextToken();
	}

	@Test
	public void namedParameter()
	{
		String src=":enero";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.NAMED_PARAMETER, "enero"), };
		test(src, expectedTokens);
	}

	@Test
	public void namedParameterPlusNumberLiteral()
	{
		String src=":enero+12";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.NAMED_PARAMETER, "enero"), new SqlToken(SqlToken.Type.OPERATOR_PLUS, "+"), new SqlToken(SqlToken.Type.INTEGER_NUMBER_LITERAL, "12"), };
		test(src, expectedTokens);
	}

	@Test
	public void unnamedParameter()
	{
		String src="?";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.UNNAMED_PARAMETER, "?"), };
		test(src, expectedTokens);
	}

	@Test
	public void blanksKeywordSelect()
	{
		String src=" SELECT";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.KEYWORD_SELECT, "SELECT") };
		test(src, expectedTokens);
	}

	@Test
	public void operatorIs()
	{
		String src="IS";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.OPERATOR_IS, "IS") };
		test(src, expectedTokens);
	}

	@Test
	public void operatorIsNot()
	{
		String src="IS NOT";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.OPERATOR_IS, "IS"), new SqlToken(SqlToken.Type.OPERATOR_NOT, "NOT") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordTrue()
	{
		String src="true";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.TRUE, "true") };
		test(src, expectedTokens);
	}

	@Test
	public void keywordFalse()
	{
		String src="false";
		SqlToken[] expectedTokens= { new SqlToken(SqlToken.Type.FALSE, "false") };
		test(src, expectedTokens);
	}
}
