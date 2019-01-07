package com.samajackun.rodas.sql.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;
import com.samajackun.rodas.core.model.AddExpression;
import com.samajackun.rodas.core.model.AsteriskExpression;
import com.samajackun.rodas.core.model.BinaryExpression;
import com.samajackun.rodas.core.model.BooleanConstantExpression;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.MultiplyExpression;
import com.samajackun.rodas.core.model.NumericConstantExpression;
import com.samajackun.rodas.core.model.ParehentesizedExpression;
import com.samajackun.rodas.core.model.UnitExpression;
import com.samajackun.rodas.core.model.UnitMinusExpression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.PushBackTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;

public class ArithmeticExpressionParserTest
{
	private Expression parseTerminal(String src)
		throws ParserException
	{
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		return ArithmeticExpressionParser.getInstance().parseTerminal(tokenizer);
	}

	@Test(expected=ParserException.class)
	public void parseTerminalForEmpty()
		throws ParserException
	{
		String src="";
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		ArithmeticExpressionParser.getInstance().parseTerminal(tokenizer);
	}

	@Test
	public void parseTerminalForNumberLiteral()
		throws ParserException
	{
		String src="120";
		Expression expression=parseTerminal(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("120", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseTerminalForTextLiteral()
		throws ParserException
	{
		String src="'enero'";
		Expression expression=parseTerminal(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("enero", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseTerminalForIdentifier()
		throws ParserException
	{
		String src="enero";
		Expression expression=parseTerminal(src);
		assertTrue(expression instanceof IdentifierExpression);
		assertEquals("enero", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void parseTerminalForPrefixedIdentifier()
		throws ParserException
	{
		String src="x.enero";
		Expression expression=parseTerminal(src);
		assertTrue(expression instanceof IdentifierExpression);
		assertEquals("x", ((IdentifierExpression)expression).getPrefix());
		assertEquals("enero", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void parseTerminalForPrefixedAsterisk()
		throws ParserException
	{
		String src="x.*";
		Expression expression=parseTerminal(src);
		assertTrue(expression instanceof AsteriskExpression);
		assertEquals("x", ((AsteriskExpression)expression).getPrefix());
		assertEquals("*", ((AsteriskExpression)expression).getAsterisk());
	}

	@Test
	public void parseTerminalForNull()
		throws ParserException
	{
		String src="null";
		Expression expression=parseTerminal(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseTerminalForParehentesis()
		throws ParserException
	{
		String src="(a)";
		Expression expression=parseTerminal(src);
		assertTrue(expression instanceof ParehentesizedExpression);
		assertTrue(((ParehentesizedExpression)expression).getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)((ParehentesizedExpression)expression).getExpression()).getIdentifier());
	}

	private Expression parseSignExpression(String src)
		throws ParserException
	{
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		return ArithmeticExpressionParser.getInstance().parseSignExpression(tokenizer);
	}

	@Test
	public void parseMinusExpressionForNumberLiteral()
		throws ParserException
	{
		String src="-120";
		Expression expression=parseSignExpression(src);
		assertTrue(expression instanceof UnitExpression);
		UnitExpression unitExpression=(UnitExpression)expression;
		assertEquals("-", unitExpression.getOperator());
		assertTrue(unitExpression.getExpression() instanceof ConstantExpression);
		assertEquals("120", ((ConstantExpression)unitExpression.getExpression()).getValue());
	}

	@Test
	public void parseMultiplyMinusExpressionForNumberLiteral()
		throws ParserException
	{
		String src="a*-120";
		Expression expression=parseMultiplyingExpression(src);
		assertTrue(expression instanceof MultiplyExpression);
		MultiplyExpression multiplyExpression=(MultiplyExpression)expression;
		assertTrue(multiplyExpression.getExpression2() instanceof UnitMinusExpression);
		UnitExpression unitExpression=(UnitExpression)multiplyExpression.getExpression2();
		assertEquals("-", unitExpression.getOperator());
		assertTrue(unitExpression.getExpression() instanceof ConstantExpression);
		assertEquals("120", ((ConstantExpression)unitExpression.getExpression()).getValue());
	}

	@Test
	public void parsePlusExpressionForNumberLiteral()
		throws ParserException
	{
		String src="+120";
		Expression expression=parseSignExpression(src);
		assertTrue(expression instanceof UnitExpression);
		UnitExpression unitExpression=(UnitExpression)expression;
		assertEquals("+", unitExpression.getOperator());
		assertTrue(unitExpression.getExpression() instanceof ConstantExpression);
		assertEquals("120", ((ConstantExpression)unitExpression.getExpression()).getValue());
	}

	@Test
	public void parseSignExpressionForIdentifier()
		throws ParserException
	{
		String src="-enero";
		Expression expression=parseSignExpression(src);
		assertTrue(expression instanceof UnitExpression);
		UnitExpression unitExpression=(UnitExpression)expression;
		assertEquals("-", unitExpression.getOperator());
		assertTrue(unitExpression.getExpression() instanceof IdentifierExpression);
		assertEquals("enero", ((IdentifierExpression)unitExpression.getExpression()).getIdentifier());
	}

	private Expression parseMultiplyingExpression(String src)
		throws ParserException
	{
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		return ArithmeticExpressionParser.getInstance().parseMultiplyingExpression(tokenizer);
	}

	@Test
	public void parseMultiplyingExpressionForNumberLiteral()
		throws ParserException
	{
		String src="120";
		Expression expression=parseMultiplyingExpression(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("120", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseMultiplyingExpressionForTextLiteral()
		throws ParserException
	{
		String src="'enero'";
		Expression expression=parseMultiplyingExpression(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("enero", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseMultiplyingExpressionForIdentifier()
		throws ParserException
	{
		String src="enero";
		Expression expression=parseMultiplyingExpression(src);
		assertTrue(expression instanceof IdentifierExpression);
		assertEquals("enero", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void parseMultiplyingExpressionForNull()
		throws ParserException
	{
		String src="null";
		Expression expression=parseMultiplyingExpression(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseMultiplyingExpressionForParehentesis()
		throws ParserException
	{
		String src="(a)";
		Expression expression=parseMultiplyingExpression(src);
		assertTrue(expression instanceof ParehentesizedExpression);
		assertTrue(((ParehentesizedExpression)expression).getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)((ParehentesizedExpression)expression).getExpression()).getIdentifier());
	}

	@Test
	public void parseMultiplyingExpressionForTwoOperands()
		throws ParserException
	{
		String src="a*b";
		Expression expression=parseMultiplyingExpression(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("*", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseMultiplyingExpressionForThreeOperands()
		throws ParserException
	{
		String src="a*b*c";
		Expression expression=parseMultiplyingExpression(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("*", binaryExpression.getOperator());

		assertTrue(binaryExpression.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)binaryExpression.getExpression1();
		assertEquals("*", binaryExpression.getOperator());
		assertTrue(binaryExpression1.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression1.getExpression1()).getIdentifier());
		assertTrue(binaryExpression1.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression1.getExpression2()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	private Expression parseAddingExpression(String src)
		throws ParserException
	{
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		return ArithmeticExpressionParser.getInstance().parseAddingExpression(tokenizer);
	}

	@Test
	public void parseAddingExpressionForNumberLiteral()
		throws ParserException
	{
		String src="120";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("120", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseAddingExpressionForTextLiteral()
		throws ParserException
	{
		String src="'enero'";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("enero", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseAddingExpressionForIdentifier()
		throws ParserException
	{
		String src="enero";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof IdentifierExpression);
		assertEquals("enero", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForNull()
		throws ParserException
	{
		String src="null";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseAddingExpressionForParehentesis()
		throws ParserException
	{
		String src="(a)";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof ParehentesizedExpression);
		assertTrue(((ParehentesizedExpression)expression).getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)((ParehentesizedExpression)expression).getExpression()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForTwoOperands()
		throws ParserException
	{
		String src="a+b";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("+", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void reduceAddExpressionForTwoNumericConstants()
		throws ParserException
	{
		String src="12+1";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof AddExpression);
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		try
		{
			expression=expression.reduce(evaluatorFactory);
			assertTrue(expression instanceof NumericConstantExpression);
			NumericConstantExpression numericConstantExpression=(NumericConstantExpression)expression;
			assertEquals(13, numericConstantExpression.getNumericValue().intValue());
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void parseAddingExpressionForThreeOperands()
		throws ParserException
	{
		String src="a+b+c";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("+", binaryExpression.getOperator());

		assertTrue(binaryExpression.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)binaryExpression.getExpression1();
		assertEquals("+", binaryExpression1.getOperator());
		assertTrue(binaryExpression1.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression1.getExpression1()).getIdentifier());
		assertTrue(binaryExpression1.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression1.getExpression2()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForThreeOperandsMultiplyAndAdd()
		throws ParserException
	{
		String src="a*b+c";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("+", binaryExpression.getOperator());

		assertTrue(binaryExpression.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)binaryExpression.getExpression1();
		assertEquals("*", binaryExpression1.getOperator());
		assertTrue(binaryExpression1.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression1.getExpression1()).getIdentifier());
		assertTrue(binaryExpression1.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression1.getExpression2()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForThreeOperandsAddAndMultiply()
		throws ParserException
	{
		String src="a+b*c";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("+", binaryExpression.getOperator());

		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)binaryExpression.getExpression2();
		assertEquals("*", binaryExpression2.getOperator());
		assertTrue(binaryExpression2.getExpression1() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		assertTrue(binaryExpression2.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForThreeOperandsMultiplyAndAddBetweenParehentesis()
		throws ParserException
	{
		String src="a*(b+c)";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		assertEquals("*", binaryExpression.getOperator());

		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);

		assertTrue(((ParehentesizedExpression)binaryExpression.getExpression2()).getExpression() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)((ParehentesizedExpression)binaryExpression.getExpression2()).getExpression();
		assertEquals("+", binaryExpression2.getOperator());
		assertTrue(binaryExpression2.getExpression1() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		assertTrue(binaryExpression2.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForIncongruentOperands()
		throws ParserException
	{
		String src="12+true";
		Expression expression=parseAddingExpression(src);
		assertTrue(expression instanceof AddExpression);
		AddExpression addExpression=(AddExpression)expression;
		assertTrue(addExpression.getExpression1() instanceof NumericConstantExpression);
		assertTrue(addExpression.getExpression2() instanceof BooleanConstantExpression);
	}
}
