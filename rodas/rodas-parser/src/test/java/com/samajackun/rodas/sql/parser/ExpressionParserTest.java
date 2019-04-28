package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionCollection;
import com.samajackun.rodas.core.model.FunctionCallExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.ParehentesizedExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;

public class ExpressionParserTest
{
	private final ParserContext parserContext=new ParserContext();

	private ExpressionCollection parseExpressionList(String src)
		throws ParserException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		return ExpressionCollectionParser.getInstance().parse(tokenizer, this.parserContext);
	}

	@Test
	public void parseExpressionListEmpty()
		throws ParserException,
		IOException
	{
		String src="";
		ExpressionCollection expressions=parseExpressionList(src);
		Assert.assertEquals(0, expressions.getExpressions().size());

	}

	@Test
	public void parseExpressionListOneExpressions()
		throws ParserException,
		IOException
	{
		String src="a";
		ExpressionCollection expressions=parseExpressionList(src);
		Assert.assertEquals(1, expressions.getExpressions().size());
		Assert.assertTrue(expressions.getExpressions().get(0) instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)expressions.getExpressions().get(0)).getIdentifier());
	}

	@Test
	public void parseExpressionListTwoExpressions()
		throws ParserException,
		IOException
	{
		String src="a,b";
		ExpressionCollection expressions=parseExpressionList(src);
		Assert.assertEquals(2, expressions.getExpressions().size());
		Assert.assertTrue(expressions.getExpressions().get(0) instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)expressions.getExpressions().get(0)).getIdentifier());
		Assert.assertTrue(expressions.getExpressions().get(1) instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)expressions.getExpressions().get(1)).getIdentifier());
	}

	private Expression parseExpression(String src)
		throws ParserException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		return ExpressionParser.getInstance().parse(tokenizer, this.parserContext);
	}

	@Test
	public void parseFunctionWithZeroArguments()
		throws ParserException,
		IOException
	{
		String src="enero()";
		Expression expression=parseExpression(src);
		Assert.assertTrue(expression instanceof FunctionCallExpression);
		FunctionCallExpression functionExpression=(FunctionCallExpression)expression;
		Assert.assertEquals("enero", ((IdentifierExpression)functionExpression.getFunctionObject()).getIdentifier());
		Assert.assertEquals(0, functionExpression.getArguments().size());
	}

	@Test
	public void parseFunctionWithOneArguments()
		throws ParserException,
		IOException
	{
		String src="enero(a)";
		Expression expression=parseExpression(src);
		Assert.assertTrue(expression instanceof FunctionCallExpression);
		FunctionCallExpression functionExpression=(FunctionCallExpression)expression;
		Assert.assertEquals("enero", ((IdentifierExpression)functionExpression.getFunctionObject()).getIdentifier());
		Assert.assertEquals(1, functionExpression.getArguments().size());
		Assert.assertTrue(functionExpression.getArguments().get(0) instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)functionExpression.getArguments().get(0)).getIdentifier());
	}

	@Test
	public void parseFunctionWithTwoArguments()
		throws ParserException,
		IOException
	{
		String src="enero(a,b)";
		Expression expression=parseExpression(src);
		Assert.assertTrue(expression instanceof FunctionCallExpression);
		FunctionCallExpression functionExpression=(FunctionCallExpression)expression;
		Assert.assertEquals("enero", ((IdentifierExpression)functionExpression.getFunctionObject()).getIdentifier());
		Assert.assertEquals(2, functionExpression.getArguments().size());
		Assert.assertTrue(functionExpression.getArguments().get(0) instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)functionExpression.getArguments().get(0)).getIdentifier());
		Assert.assertTrue(functionExpression.getArguments().get(1) instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)functionExpression.getArguments().get(1)).getIdentifier());
	}

	@Test
	public void parseIdentifier()
		throws ParserException,
		IOException
	{
		String src="enero";
		Expression expression=parseExpression(src);
		Assert.assertTrue(expression instanceof IdentifierExpression);
		IdentifierExpression identifierExpression=(IdentifierExpression)expression;
		Assert.assertEquals("enero", identifierExpression.getIdentifier());
	}

	@Test
	public void parseConstant()
		throws ParserException,
		IOException
	{
		String src="120";
		Expression expression=parseExpression(src);
		Assert.assertTrue("Expected ConstantExpression instead of " + expression.getClass().getName(), expression instanceof ConstantExpression);
		ConstantExpression constantExpression=(ConstantExpression)expression;
		Assert.assertEquals("120", constantExpression.getValue());
	}

	@Test
	public void parseParehentesizedExpressionWithOneElement()
		throws ParserException,
		IOException
	{
		String src="(a)";
		Expression expression=parseExpression(src);
		Assert.assertTrue(expression instanceof ParehentesizedExpression);
		ParehentesizedExpression parehentesizedExpression=(ParehentesizedExpression)expression;
		Assert.assertTrue(parehentesizedExpression.getExpression() instanceof IdentifierExpression);
		IdentifierExpression identifierExpression=(IdentifierExpression)parehentesizedExpression.getExpression();
		Assert.assertEquals("a", identifierExpression.getIdentifier());
	}

	@Test
	public void parseParehentesizedExpressionWithTwoElements()
		throws ParserException,
		IOException
	{
		String src="(a,b)";
		Expression expression=parseExpression(src);
		Assert.assertTrue(expression instanceof ParehentesizedExpression);
		ParehentesizedExpression parehentesizedExpression=(ParehentesizedExpression)expression;
		Assert.assertTrue(parehentesizedExpression.getExpression() instanceof ExpressionCollection);
		ExpressionCollection expressionList=(ExpressionCollection)parehentesizedExpression.getExpression();
		Assert.assertEquals(2, expressionList.getExpressions().size());
		Assert.assertEquals("a", ((IdentifierExpression)expressionList.getExpressions().get(0)).getIdentifier());
		Assert.assertEquals("b", ((IdentifierExpression)expressionList.getExpressions().get(1)).getIdentifier());
	}
}
