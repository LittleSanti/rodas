package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.samajackun.rodas.core.model.BinaryExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;

public class RelationalExpressionParserTest
{
	private final ParserContext parserContext=new ParserContext();

	private Expression parse(String src)
		throws ParserException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		return RelationalExpressionParser.getInstance().parse(tokenizer, this.parserContext);
	}

	@Test
	public void parseRelationEquals()
		throws ParserException,
		IOException
	{
		String src="a=b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("=", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationDistinct1()
		throws ParserException,
		IOException
	{
		String src="a<>b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("<>", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationDistinct2()
		throws ParserException,
		IOException
	{
		String src="a!=b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("!=", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationLower()
		throws ParserException,
		IOException
	{
		String src="a<b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("<", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationLowerOrEquals()
		throws ParserException,
		IOException
	{
		String src="a<=b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("<=", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseRelationGreaterOrEquals()
		throws ParserException,
		IOException
	{
		String src="a>=b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals(">=", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseEqualsWithAnAddAsFirstOperator()
		throws ParserException,
		IOException
	{
		String src="a+b=c";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)expression;
		Assert.assertEquals("=", binaryExpression1.getOperator());

		Assert.assertTrue(binaryExpression1.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)binaryExpression1.getExpression1();
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		Assert.assertEquals("+", binaryExpression2.getOperator());
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());

		Assert.assertTrue(binaryExpression1.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression1.getExpression2()).getIdentifier());
	}

	@Test
	public void parseEqualsWithAnAddAsSecondOperator()
		throws ParserException,
		IOException
	{
		String src="a=b+c";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)expression;
		Assert.assertEquals("=", binaryExpression1.getOperator());
		Assert.assertTrue(binaryExpression1.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression1.getExpression1()).getIdentifier());

		Assert.assertTrue(binaryExpression1.getExpression2() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)binaryExpression1.getExpression2();
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		Assert.assertEquals("+", binaryExpression2.getOperator());
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());
	}

	@Test
	public void parseEqualsWithAddAsFirstAndSecondOperator()
		throws ParserException,
		IOException
	{
		String src="a+b=c-d";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)expression;
		Assert.assertEquals("=", binaryExpression1.getOperator());

		Assert.assertTrue(binaryExpression1.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)binaryExpression1.getExpression1();
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		Assert.assertEquals("+", binaryExpression2.getOperator());
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());

		Assert.assertTrue(binaryExpression1.getExpression2() instanceof BinaryExpression);
		BinaryExpression binaryExpression3=(BinaryExpression)binaryExpression1.getExpression2();
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression3.getExpression1()).getIdentifier());
		Assert.assertEquals("-", binaryExpression3.getOperator());
		Assert.assertEquals("d", ((IdentifierExpression)binaryExpression3.getExpression2()).getIdentifier());
	}
}
