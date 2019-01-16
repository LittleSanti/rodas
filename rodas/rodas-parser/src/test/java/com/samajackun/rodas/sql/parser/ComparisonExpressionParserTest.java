package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.samajackun.rodas.core.model.BinaryExpression;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionList;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.InExpression;
import com.samajackun.rodas.core.model.ParehentesizedExpression;
import com.samajackun.rodas.core.model.UnitExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;

public class ComparisonExpressionParserTest
{
	private Expression parse(String src)
		throws ParserException,
		IOException
	{
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		Expression expression=ComparisonExpressionParser.getInstance().parse(tokenizer);
		return expression;
	}

	@Test
	public void parseLike()
		throws ParserException,
		IOException
	{
		String src="a like b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("like", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseIsNull()
		throws ParserException,
		IOException
	{
		String src="a is null";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("is", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)binaryExpression.getExpression2()).getValue());
	}

	@Test
	public void parseIsConstant()
		throws ParserException,
		IOException
	{
		String src="a is 12";
		try
		{
			parse(src);
		}
		catch (UnexpectedTokenException e)
		{
			Assert.assertEquals(SqlToken.Type.INTEGER_NUMBER_LITERAL, e.getToken().getType());
		}
	}

	@Test
	public void parseIsNotNull()
		throws ParserException,
		IOException
	{
		String src="a is not null";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("is", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof UnitExpression);
		UnitExpression expression2=(UnitExpression)binaryExpression.getExpression2();
		Assert.assertEquals("not", expression2.getOperator());
		Assert.assertTrue(expression2.getExpression() instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)expression2.getExpression()).getValue());
	}

	@Test
	public void parseIsNotNotNull()
		throws ParserException,
		IOException
	{
		String src="a is not not null";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("is", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof UnitExpression);
		UnitExpression expression2=(UnitExpression)binaryExpression.getExpression2();
		Assert.assertEquals("not", expression2.getOperator());
		UnitExpression expression3=(UnitExpression)expression2.getExpression();
		Assert.assertEquals("not", expression3.getOperator());
		Assert.assertTrue(expression3.getExpression() instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)expression3.getExpression()).getValue());
	}

	@Test
	public void parseIsNotNullWithInnerComment()
		throws ParserException,
		IOException
	{
		String src="a is /*x*/ not null";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("is", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof UnitExpression);
		UnitExpression expression2=(UnitExpression)binaryExpression.getExpression2();
		Assert.assertEquals("not", expression2.getOperator());
		Assert.assertTrue(expression2.getExpression() instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)expression2.getExpression()).getValue());
	}

	@Test
	public void parseIsOfType()
		throws ParserException,
		IOException
	{
		String src="a is of type number";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("isoftype", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("number", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseInList()
		throws ParserException,
		IOException
	{
		String src="a in (a1,a2,a3)";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		Assert.assertEquals("in", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);
		ParehentesizedExpression set=(ParehentesizedExpression)binaryExpression.getExpression2();
		Assert.assertTrue(set.getExpression() instanceof ExpressionList);
		ExpressionList expressionList=(ExpressionList)set.getExpression();
		Assert.assertEquals(3, expressionList.getExpressions().size());
		Assert.assertEquals("a1", ((IdentifierExpression)expressionList.getExpressions().get(0)).getIdentifier());
		Assert.assertEquals("a2", ((IdentifierExpression)expressionList.getExpressions().get(1)).getIdentifier());
		Assert.assertEquals("a3", ((IdentifierExpression)expressionList.getExpressions().get(2)).getIdentifier());
	}

}
