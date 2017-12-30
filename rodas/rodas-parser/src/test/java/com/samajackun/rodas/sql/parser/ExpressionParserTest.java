package com.samajackun.rodas.sql.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.samajackun.rodas.sql.model.ConstantExpression;
import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.model.ExpressionList;
import com.samajackun.rodas.sql.model.FunctionExpression;
import com.samajackun.rodas.sql.model.IdentifierExpression;
import com.samajackun.rodas.sql.model.ParehentesizedExpression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.PushBackTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;

public class ExpressionParserTest
{
	private ExpressionList parseExpressionList(String src)
		throws ParserException
	{
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		return ExpressionParser.getInstance().parseExpressionList(tokenizer);
	}

	@Test
	public void parseExpressionListEmpty()
		throws ParserException
	{
		String src="";
		ExpressionList expressions=parseExpressionList(src);
		assertEquals(0, expressions.getExpressions().size());

	}

	@Test
	public void parseExpressionListOneExpressions()
		throws ParserException
	{
		String src="a";
		ExpressionList expressions=parseExpressionList(src);
		assertEquals(1, expressions.getExpressions().size());
		assertTrue(expressions.getExpressions().get(0) instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)expressions.getExpressions().get(0)).getIdentifier());
	}

	@Test
	public void parseExpressionListTwoExpressions()
		throws ParserException
	{
		String src="a,b";
		ExpressionList expressions=parseExpressionList(src);
		assertEquals(2, expressions.getExpressions().size());
		assertTrue(expressions.getExpressions().get(0) instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)expressions.getExpressions().get(0)).getIdentifier());
		assertTrue(expressions.getExpressions().get(1) instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)expressions.getExpressions().get(1)).getIdentifier());
	}

	private Expression parseExpression(String src)
		throws ParserException
	{
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		return ExpressionParser.getInstance().parse(tokenizer);
	}

	@Test
	public void parseFunctionWithZeroArguments()
		throws ParserException
	{
		String src="enero()";
		Expression expression=parseExpression(src);
		assertTrue(expression instanceof FunctionExpression);
		FunctionExpression functionExpression=(FunctionExpression)expression;
		assertEquals("enero", functionExpression.getFunction());
		assertEquals(0, functionExpression.getArguments().size());
	}

	@Test
	public void parseFunctionWithOneArguments()
		throws ParserException
	{
		String src="enero(a)";
		Expression expression=parseExpression(src);
		assertTrue(expression instanceof FunctionExpression);
		FunctionExpression functionExpression=(FunctionExpression)expression;
		assertEquals("enero", functionExpression.getFunction());
		assertEquals(1, functionExpression.getArguments().size());
		assertTrue(functionExpression.getArguments().get(0) instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)functionExpression.getArguments().get(0)).getIdentifier());
	}

	@Test
	public void parseFunctionWithTwoArguments()
		throws ParserException
	{
		String src="enero(a,b)";
		Expression expression=parseExpression(src);
		assertTrue(expression instanceof FunctionExpression);
		FunctionExpression functionExpression=(FunctionExpression)expression;
		assertEquals("enero", functionExpression.getFunction());
		assertEquals(2, functionExpression.getArguments().size());
		assertTrue(functionExpression.getArguments().get(0) instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)functionExpression.getArguments().get(0)).getIdentifier());
		assertTrue(functionExpression.getArguments().get(1) instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)functionExpression.getArguments().get(1)).getIdentifier());
	}

	@Test
	public void parseIdentifier()
		throws ParserException
	{
		String src="enero";
		Expression expression=parseExpression(src);
		assertTrue(expression instanceof IdentifierExpression);
		IdentifierExpression identifierExpression=(IdentifierExpression)expression;
		assertEquals("enero", identifierExpression.getIdentifier());
	}

	@Test
	public void parseConstant()
		throws ParserException
	{
		String src="120";
		Expression expression=parseExpression(src);
		assertTrue(expression instanceof ConstantExpression);
		ConstantExpression constantExpression=(ConstantExpression)expression;
		assertEquals("120", constantExpression.getValue());
	}

	@Test
	public void parseParehentesizedExpressionWithOneElement()
		throws ParserException
	{
		String src="(a)";
		Expression expression=parseExpression(src);
		assertTrue(expression instanceof ParehentesizedExpression);
		ParehentesizedExpression parehentesizedExpression=(ParehentesizedExpression)expression;
		assertTrue(parehentesizedExpression.getExpression() instanceof IdentifierExpression);
		IdentifierExpression identifierExpression=(IdentifierExpression)parehentesizedExpression.getExpression();
		assertEquals("a", identifierExpression.getIdentifier());
	}

	@Test
	public void parseParehentesizedExpressionWithTwoElements()
		throws ParserException
	{
		String src="(a,b)";
		Expression expression=parseExpression(src);
		assertTrue(expression instanceof ParehentesizedExpression);
		ParehentesizedExpression parehentesizedExpression=(ParehentesizedExpression)expression;
		assertTrue(parehentesizedExpression.getExpression() instanceof ExpressionList);
		ExpressionList expressionList=(ExpressionList)parehentesizedExpression.getExpression();
		assertEquals(2, expressionList.getExpressions().size());
		assertEquals("a", ((IdentifierExpression)expressionList.getExpressions().get(0)).getIdentifier());
		assertEquals("b", ((IdentifierExpression)expressionList.getExpressions().get(1)).getIdentifier());
	}
}
