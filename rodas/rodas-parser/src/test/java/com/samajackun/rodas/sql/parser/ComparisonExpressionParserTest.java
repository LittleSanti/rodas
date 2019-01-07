package com.samajackun.rodas.sql.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.samajackun.rodas.core.model.BinaryExpression;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionList;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.InExpression;
import com.samajackun.rodas.core.model.ParehentesizedExpression;
import com.samajackun.rodas.core.model.UnitExpression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.PushBackTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public class ComparisonExpressionParserTest
{
	private Expression parse(String src)
		throws ParserException
	{
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		Expression expression=ComparisonExpressionParser.getInstance().parse(tokenizer);
		return expression;
	}

	@Test
	public void parseLike()
		throws ParserException
	{
		String src="a like b";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("like", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseIsNull()
		throws ParserException
	{
		String src="a is null";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("is", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)binaryExpression.getExpression2()).getValue());
	}

	@Test
	public void parseIsConstant()
		throws ParserException
	{
		String src="a is 12";
		try
		{
			parse(src);
		}
		catch (UnexpectedTokenException e)
		{
			assertEquals(SqlToken.Type.INTEGER_NUMBER_LITERAL, e.getToken().getType());
		}
	}

	@Test
	public void parseIsNotNull()
		throws ParserException
	{
		String src="a is not null";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("is", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof UnitExpression);
		UnitExpression expression2=(UnitExpression)binaryExpression.getExpression2();
		assertEquals("not", expression2.getOperator());
		assertTrue(expression2.getExpression() instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)expression2.getExpression()).getValue());
	}

	@Test
	public void parseIsNotNotNull()
		throws ParserException
	{
		String src="a is not not null";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("is", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof UnitExpression);
		UnitExpression expression2=(UnitExpression)binaryExpression.getExpression2();
		assertEquals("not", expression2.getOperator());
		UnitExpression expression3=(UnitExpression)expression2.getExpression();
		assertEquals("not", expression3.getOperator());
		assertTrue(expression3.getExpression() instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)expression3.getExpression()).getValue());
	}

	@Test
	public void parseIsNotNullWithInnerComment()
		throws ParserException
	{
		String src="a is /*x*/ not null";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("is", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof UnitExpression);
		UnitExpression expression2=(UnitExpression)binaryExpression.getExpression2();
		assertEquals("not", expression2.getOperator());
		assertTrue(expression2.getExpression() instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)expression2.getExpression()).getValue());
	}

	@Test
	public void parseIsOfType()
		throws ParserException
	{
		String src="a is of type number";
		Expression expression=parse(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("isoftype", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("number", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseInList()
		throws ParserException
	{
		String src="a in (a1,a2,a3)";
		Expression expression=parse(src);
		assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		assertEquals("in", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);
		ParehentesizedExpression set=(ParehentesizedExpression)binaryExpression.getExpression2();
		assertTrue(set.getExpression() instanceof ExpressionList);
		ExpressionList expressionList=(ExpressionList)set.getExpression();
		assertEquals(3, expressionList.getExpressions().size());
		assertEquals("a1", ((IdentifierExpression)expressionList.getExpressions().get(0)).getIdentifier());
		assertEquals("a2", ((IdentifierExpression)expressionList.getExpressions().get(1)).getIdentifier());
		assertEquals("a3", ((IdentifierExpression)expressionList.getExpressions().get(2)).getIdentifier());
	}

}
