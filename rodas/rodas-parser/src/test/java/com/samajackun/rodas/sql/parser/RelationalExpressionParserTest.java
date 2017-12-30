package com.samajackun.rodas.sql.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.samajackun.rodas.sql.model.BinaryExpression;
import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.model.IdentifierExpression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.PushBackTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;

public class RelationalExpressionParserTest
{
	private Expression parse(String src)
		throws ParserException
	{
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		return RelationalExpressionParser.getInstance().parse(tokenizer);
	}

	@Test
	public void parseRelationEquals()
		throws ParserException
	{
		String src="a=b";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("=", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationDistinct1()
		throws ParserException
	{
		String src="a<>b";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("<>", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationDistinct2()
		throws ParserException
	{
		String src="a!=b";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("!=", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationLower()
		throws ParserException
	{
		String src="a<b";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("<", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationLowerOrEquals()
		throws ParserException
	{
		String src="a<=b";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("<=", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationGreaterOrEquals()
		throws ParserException
	{
		String src="a>=b";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals(">=", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseEqualsWithAnAddAsFirstOperator()
		throws ParserException
	{
		String src="a+b=c";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)expression;
		assertEquals("=", binaryExpression1.getOperator());

		assertTrue(binaryExpression1.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)binaryExpression1.getExpression1();
		assertEquals("a", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		assertEquals("+", binaryExpression2.getOperator());
		assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());

		assertTrue(binaryExpression1.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression1.getExpression2()).getIdentifier());
	}

	@Test
	public void parseEqualsWithAnAddAsSecondOperator()
		throws ParserException
	{
		String src="a=b+c";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)expression;
		assertEquals("=", binaryExpression1.getOperator());
		assertTrue(binaryExpression1.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression1.getExpression1()).getIdentifier());

		assertTrue(binaryExpression1.getExpression2() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)binaryExpression1.getExpression2();
		assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		assertEquals("+", binaryExpression2.getOperator());
		assertEquals("c", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());
	}

	@Test
	public void parseEqualsWithAddAsFirstAndSecondOperator()
		throws ParserException
	{
		String src="a+b=c-d";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)expression;
		assertEquals("=", binaryExpression1.getOperator());

		assertTrue(binaryExpression1.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)binaryExpression1.getExpression1();
		assertEquals("a", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		assertEquals("+", binaryExpression2.getOperator());
		assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());

		assertTrue(binaryExpression1.getExpression2() instanceof BinaryExpression);
		BinaryExpression binaryExpression3=(BinaryExpression)binaryExpression1.getExpression2();
		assertEquals("c", ((IdentifierExpression)binaryExpression3.getExpression1()).getIdentifier());
		assertEquals("-", binaryExpression3.getOperator());
		assertEquals("d", ((IdentifierExpression)binaryExpression3.getExpression2()).getIdentifier());
	}
}
