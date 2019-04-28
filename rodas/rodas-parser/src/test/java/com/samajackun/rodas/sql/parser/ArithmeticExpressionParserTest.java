package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import org.junit.Assert;
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
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;

public class ArithmeticExpressionParserTest
{
	private final ParserContext parserContext=new ParserContext();

	private Expression parseTerminal(String src)
		throws ParserException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		return ArithmeticExpressionParser.getInstance().parseTerminal(tokenizer, this.parserContext);
	}

	@Test(expected=ParserException.class)
	public void parseTerminalForEmpty()
		throws ParserException,
		IOException
	{
		String src="";
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		ArithmeticExpressionParser.getInstance().parseTerminal(tokenizer, this.parserContext);
	}

	@Test
	public void parseTerminalForNumberLiteral()
		throws ParserException,
		IOException
	{
		String src="120";
		Expression expression=parseTerminal(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("120", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseTerminalForTextLiteral()
		throws ParserException,
		IOException
	{
		String src="'enero'";
		Expression expression=parseTerminal(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("enero", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseTerminalForIdentifier()
		throws ParserException,
		IOException
	{
		String src="enero";
		Expression expression=parseTerminal(src);
		Assert.assertTrue(expression instanceof IdentifierExpression);
		Assert.assertEquals("enero", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void parseTerminalForPrefixedIdentifier()
		throws ParserException,
		IOException
	{
		String src="x.enero";
		Expression expression=parseTerminal(src);
		Assert.assertTrue(expression instanceof IdentifierExpression);
		Assert.assertEquals("x", ((IdentifierExpression)expression).getPrefix());
		Assert.assertEquals("enero", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void parseTerminalForPrefixedAsterisk()
		throws ParserException,
		IOException
	{
		String src="x.*";
		Expression expression=parseTerminal(src);
		Assert.assertTrue(expression instanceof AsteriskExpression);
		Assert.assertEquals("x", ((AsteriskExpression)expression).getPrefix());
		Assert.assertEquals("*", ((AsteriskExpression)expression).getAsterisk());
	}

	@Test
	public void parseTerminalForNull()
		throws ParserException,
		IOException
	{
		String src="null";
		Expression expression=parseTerminal(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseTerminalForParehentesis()
		throws ParserException,
		IOException
	{
		String src="(a)";
		Expression expression=parseTerminal(src);
		Assert.assertTrue(expression instanceof ParehentesizedExpression);
		Assert.assertTrue(((ParehentesizedExpression)expression).getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)((ParehentesizedExpression)expression).getExpression()).getIdentifier());
	}

	private Expression parseSignExpression(String src)
		throws ParserException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		return ArithmeticExpressionParser.getInstance().parseSignExpression(tokenizer, this.parserContext);
	}

	@Test
	public void parseMinusExpressionForNumberLiteral()
		throws ParserException,
		IOException
	{
		String src="-120";
		Expression expression=parseSignExpression(src);
		Assert.assertTrue(expression instanceof UnitExpression);
		UnitExpression unitExpression=(UnitExpression)expression;
		Assert.assertEquals("-", unitExpression.getOperator());
		Assert.assertTrue(unitExpression.getExpression() instanceof ConstantExpression);
		Assert.assertEquals("120", ((ConstantExpression)unitExpression.getExpression()).getValue());
	}

	@Test
	public void parseMultiplyMinusExpressionForNumberLiteral()
		throws ParserException,
		IOException
	{
		String src="a*-120";
		Expression expression=parseMultiplyingExpression(src);
		Assert.assertTrue(expression instanceof MultiplyExpression);
		MultiplyExpression multiplyExpression=(MultiplyExpression)expression;
		Assert.assertTrue(multiplyExpression.getExpression2() instanceof UnitMinusExpression);
		UnitExpression unitExpression=(UnitExpression)multiplyExpression.getExpression2();
		Assert.assertEquals("-", unitExpression.getOperator());
		Assert.assertTrue(unitExpression.getExpression() instanceof ConstantExpression);
		Assert.assertEquals("120", ((ConstantExpression)unitExpression.getExpression()).getValue());
	}

	@Test
	public void parsePlusExpressionForNumberLiteral()
		throws ParserException,
		IOException
	{
		String src="+120";
		Expression expression=parseSignExpression(src);
		Assert.assertTrue(expression instanceof UnitExpression);
		UnitExpression unitExpression=(UnitExpression)expression;
		Assert.assertEquals("+", unitExpression.getOperator());
		Assert.assertTrue(unitExpression.getExpression() instanceof ConstantExpression);
		Assert.assertEquals("120", ((ConstantExpression)unitExpression.getExpression()).getValue());
	}

	@Test
	public void parseSignExpressionForIdentifier()
		throws ParserException,
		IOException
	{
		String src="-enero";
		Expression expression=parseSignExpression(src);
		Assert.assertTrue(expression instanceof UnitExpression);
		UnitExpression unitExpression=(UnitExpression)expression;
		Assert.assertEquals("-", unitExpression.getOperator());
		Assert.assertTrue(unitExpression.getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("enero", ((IdentifierExpression)unitExpression.getExpression()).getIdentifier());
	}

	private Expression parseMultiplyingExpression(String src)
		throws ParserException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		return ArithmeticExpressionParser.getInstance().parseMultiplyingExpression(tokenizer, this.parserContext);
	}

	@Test
	public void parseMultiplyingExpressionForNumberLiteral()
		throws ParserException,
		IOException
	{
		String src="120";
		Expression expression=parseMultiplyingExpression(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("120", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseMultiplyingExpressionForTextLiteral()
		throws ParserException,
		IOException
	{
		String src="'enero'";
		Expression expression=parseMultiplyingExpression(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("enero", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseMultiplyingExpressionForIdentifier()
		throws ParserException,
		IOException
	{
		String src="enero";
		Expression expression=parseMultiplyingExpression(src);
		Assert.assertTrue(expression instanceof IdentifierExpression);
		Assert.assertEquals("enero", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void parseMultiplyingExpressionForNull()
		throws ParserException,
		IOException
	{
		String src="null";
		Expression expression=parseMultiplyingExpression(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseMultiplyingExpressionForParehentesis()
		throws ParserException,
		IOException
	{
		String src="(a)";
		Expression expression=parseMultiplyingExpression(src);
		Assert.assertTrue(expression instanceof ParehentesizedExpression);
		Assert.assertTrue(((ParehentesizedExpression)expression).getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)((ParehentesizedExpression)expression).getExpression()).getIdentifier());
	}

	@Test
	public void parseMultiplyingExpressionForTwoOperands()
		throws ParserException,
		IOException
	{
		String src="a*b";
		Expression expression=parseMultiplyingExpression(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("*", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseMultiplyingExpressionForThreeOperands()
		throws ParserException,
		IOException
	{
		String src="a*b*c";
		Expression expression=parseMultiplyingExpression(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("*", binaryExpression.getOperator());

		Assert.assertTrue(binaryExpression.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)binaryExpression.getExpression1();
		Assert.assertEquals("*", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression1.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression1.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression1.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression1.getExpression2()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	private Expression parseAddingExpression(String src)
		throws ParserException,
		IOException
	{
		SqlMatchingTokenizer tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src))));
		return ArithmeticExpressionParser.getInstance().parseAddingExpression(tokenizer, this.parserContext);
	}

	@Test
	public void parseAddingExpressionForNumberLiteral()
		throws ParserException,
		IOException
	{
		String src="120";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("120", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseAddingExpressionForTextLiteral()
		throws ParserException,
		IOException
	{
		String src="'enero'";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("enero", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseAddingExpressionForIdentifier()
		throws ParserException,
		IOException
	{
		String src="enero";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof IdentifierExpression);
		Assert.assertEquals("enero", ((IdentifierExpression)expression).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForNull()
		throws ParserException,
		IOException
	{
		String src="null";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)expression).getValue());
	}

	@Test
	public void parseAddingExpressionForParehentesis()
		throws ParserException,
		IOException
	{
		String src="(a)";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof ParehentesizedExpression);
		Assert.assertTrue(((ParehentesizedExpression)expression).getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)((ParehentesizedExpression)expression).getExpression()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForTwoOperands()
		throws ParserException,
		IOException
	{
		String src="a+b";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("+", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void reduceAddExpressionForTwoNumericConstants()
		throws ParserException,
		IOException
	{
		String src="12+1";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof AddExpression);
		EvaluatorFactory evaluatorFactory=new DefaultEvaluatorFactory();
		try
		{
			expression=expression.reduce(evaluatorFactory);
			Assert.assertTrue(expression instanceof NumericConstantExpression);
			NumericConstantExpression numericConstantExpression=(NumericConstantExpression)expression;
			Assert.assertEquals(13, numericConstantExpression.getNumericValue().intValue());
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void parseAddingExpressionForThreeOperands()
		throws ParserException,
		IOException
	{
		String src="a+b+c";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("+", binaryExpression.getOperator());

		Assert.assertTrue(binaryExpression.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)binaryExpression.getExpression1();
		Assert.assertEquals("+", binaryExpression1.getOperator());
		Assert.assertTrue(binaryExpression1.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression1.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression1.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression1.getExpression2()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForThreeOperandsMultiplyAndAdd()
		throws ParserException,
		IOException
	{
		String src="a*b+c";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("+", binaryExpression.getOperator());

		Assert.assertTrue(binaryExpression.getExpression1() instanceof BinaryExpression);
		BinaryExpression binaryExpression1=(BinaryExpression)binaryExpression.getExpression1();
		Assert.assertEquals("*", binaryExpression1.getOperator());
		Assert.assertTrue(binaryExpression1.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression1.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression1.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression1.getExpression2()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForThreeOperandsAddAndMultiply()
		throws ParserException,
		IOException
	{
		String src="a+b*c";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("+", binaryExpression.getOperator());

		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)binaryExpression.getExpression2();
		Assert.assertEquals("*", binaryExpression2.getOperator());
		Assert.assertTrue(binaryExpression2.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression2.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForThreeOperandsMultiplyAndAddBetweenParehentesis()
		throws ParserException,
		IOException
	{
		String src="a*(b+c)";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof BinaryExpression);
		BinaryExpression binaryExpression=(BinaryExpression)expression;
		Assert.assertEquals("*", binaryExpression.getOperator());

		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);

		Assert.assertTrue(((ParehentesizedExpression)binaryExpression.getExpression2()).getExpression() instanceof BinaryExpression);
		BinaryExpression binaryExpression2=(BinaryExpression)((ParehentesizedExpression)binaryExpression.getExpression2()).getExpression();
		Assert.assertEquals("+", binaryExpression2.getOperator());
		Assert.assertTrue(binaryExpression2.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression2.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression2.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression2.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAddingExpressionForIncongruentOperands()
		throws ParserException,
		IOException
	{
		String src="12+true";
		Expression expression=parseAddingExpression(src);
		Assert.assertTrue(expression instanceof AddExpression);
		AddExpression addExpression=(AddExpression)expression;
		Assert.assertTrue(addExpression.getExpression1() instanceof NumericConstantExpression);
		Assert.assertTrue(addExpression.getExpression2() instanceof BooleanConstantExpression);
	}
}
