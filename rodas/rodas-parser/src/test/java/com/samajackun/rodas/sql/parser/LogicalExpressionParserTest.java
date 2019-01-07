package com.samajackun.rodas.sql.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.samajackun.rodas.core.model.AndExpression;
import com.samajackun.rodas.core.model.BetweenExpression;
import com.samajackun.rodas.core.model.BinaryExpression;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.ExistsExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionList;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.InExpression;
import com.samajackun.rodas.core.model.IsExpression;
import com.samajackun.rodas.core.model.LikeExpression;
import com.samajackun.rodas.core.model.NotExpression;
import com.samajackun.rodas.core.model.OrExpression;
import com.samajackun.rodas.core.model.ParehentesizedExpression;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.UnitExpression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.PushBackTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizerSettings;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizerSettings.WhitespaceBehaviour;
import com.samajackun.rodas.sql.parser.tokenizer.TokenizerException;

public class LogicalExpressionParserTest
{
	private Expression parse(String src)
		throws TokenizerException,
		ParserException
	{
		SqlTokenizerSettings settings=new SqlTokenizerSettings();
		settings.setWhitespaceBehaviour(WhitespaceBehaviour.IGNORE);
		ParserTokenizer tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src, settings)));
		Expression expression=LogicalExpressionParser.getInstance().parse(tokenizer);
		return expression;
	}

	@Test
	public void parseAnd()
		throws ParserException
	{
		String src="a and b";
		Expression expression=parse(src);
		assertTrue(expression instanceof AndExpression);
		BinaryExpression binaryExpression=(AndExpression)expression;
		assertEquals("and", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseNot()
		throws ParserException
	{
		String src="not a";
		Expression expression=parse(src);
		assertTrue(expression instanceof NotExpression);
		UnitExpression unitExpression=(NotExpression)expression;
		assertEquals("not", unitExpression.getOperator());
		assertTrue(unitExpression.getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)unitExpression.getExpression()).getIdentifier());
	}

	@Test
	public void parseNotAnd()
		throws ParserException
	{
		String src="not a and b";
		Expression expression=parse(src);
		assertTrue(expression instanceof AndExpression);
		BinaryExpression binaryExpression=(AndExpression)expression;
		assertEquals("and", binaryExpression.getOperator());
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
		UnitExpression expression1=(NotExpression)binaryExpression.getExpression1();
		assertEquals("not", expression1.getOperator());
		assertTrue(expression1.getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)expression1.getExpression()).getIdentifier());
	}

	@Test
	public void parseNotOr()
		throws ParserException
	{
		String src="not a or b";
		Expression expression=parse(src);
		assertTrue(expression instanceof OrExpression);
		BinaryExpression binaryExpression=(OrExpression)expression;
		assertEquals("or", binaryExpression.getOperator());
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
		UnitExpression expression1=(NotExpression)binaryExpression.getExpression1();
		assertEquals("not", expression1.getOperator());
		assertTrue(expression1.getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)expression1.getExpression()).getIdentifier());
	}

	@Test
	public void parseOr()
		throws ParserException
	{
		String src="a or b";
		Expression expression=parse(src);
		assertTrue(expression instanceof OrExpression);
		BinaryExpression binaryExpression=(OrExpression)expression;
		assertEquals("or", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseOrAnd()
		throws ParserException
	{
		String src="a or b and c";
		Expression expression=parse(src);

		assertTrue(expression instanceof OrExpression);
		BinaryExpression binaryExpression=(OrExpression)expression;
		assertEquals("or", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof AndExpression);
		BinaryExpression expression2=(AndExpression)binaryExpression.getExpression2();
		assertEquals("and", expression2.getOperator());
		assertTrue(expression2.getExpression1() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)expression2.getExpression1()).getIdentifier());
		assertTrue(expression2.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)expression2.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAndOr()
		throws ParserException
	{
		String src="a and b or c";
		Expression expression=parse(src);

		assertTrue(expression instanceof OrExpression);
		BinaryExpression binaryExpression=(OrExpression)expression;
		assertEquals("or", binaryExpression.getOperator());

		assertTrue(binaryExpression.getExpression1() instanceof AndExpression);
		BinaryExpression expression1=(AndExpression)binaryExpression.getExpression1();
		assertEquals("and", expression1.getOperator());
		assertTrue(expression1.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)expression1.getExpression1()).getIdentifier());
		assertTrue(expression1.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)expression1.getExpression2()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAndAnd()
		throws ParserException
	{
		String src="a and b and c";
		Expression expression=parse(src);

		assertTrue(expression instanceof AndExpression);
		BinaryExpression binaryExpression=(AndExpression)expression;
		assertEquals("and", binaryExpression.getOperator());

		assertTrue(binaryExpression.getExpression1() instanceof AndExpression);
		BinaryExpression expression1=(AndExpression)binaryExpression.getExpression1();
		assertEquals("and", expression1.getOperator());
		assertTrue(expression1.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)expression1.getExpression1()).getIdentifier());
		assertTrue(expression1.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)expression1.getExpression2()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseExists()
		throws ParserException
	{
		String src="exists a";
		Expression expression=parse(src);
		assertTrue(expression instanceof ExistsExpression);
		UnitExpression unitExpression=(ExistsExpression)expression;
		assertEquals("exists", unitExpression.getOperator());
		assertTrue(unitExpression.getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)unitExpression.getExpression()).getIdentifier());
	}

	@Test
	public void parseNotExists()
		throws ParserException
	{
		String src="not exists a";
		Expression expression=parse(src);
		assertTrue(expression instanceof NotExpression);
		UnitExpression unitExpression=(NotExpression)expression;
		assertEquals("not", unitExpression.getOperator());
		assertTrue(unitExpression.getExpression() instanceof ExistsExpression);

		UnitExpression unitExpression2=(ExistsExpression)unitExpression.getExpression();
		assertEquals("exists", unitExpression2.getOperator());
		assertTrue(unitExpression2.getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)unitExpression2.getExpression()).getIdentifier());
	}

	@Test
	public void parseNotNot()
		throws ParserException
	{
		String src="not not a";
		Expression expression=parse(src);
		assertTrue(expression instanceof NotExpression);
		UnitExpression unitExpression=(NotExpression)expression;
		assertEquals("not", unitExpression.getOperator());
		assertTrue(unitExpression.getExpression() instanceof NotExpression);

		UnitExpression unitExpression2=(NotExpression)unitExpression.getExpression();
		assertEquals("not", unitExpression2.getOperator());
		assertTrue(unitExpression2.getExpression() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)unitExpression2.getExpression()).getIdentifier());
	}

	@Test
	public void parseIsNull()
		throws ParserException
	{
		String src="a is null";
		Expression expression=parse(src);
		assertTrue(expression instanceof IsExpression);
		BinaryExpression binaryExpression=(IsExpression)expression;
		assertEquals("is", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)binaryExpression.getExpression2()).getValue());
	}

	@Test
	public void parseIsNotNull()
		throws ParserException
	{
		String src="a is not null";
		Expression expression=parse(src);
		assertTrue(expression instanceof IsExpression);
		BinaryExpression binaryExpression=(IsExpression)expression;
		assertEquals("is", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());

		assertTrue(binaryExpression.getExpression2() instanceof NotExpression);
		UnitExpression unitExpression2=(NotExpression)binaryExpression.getExpression2();
		assertEquals("not", unitExpression2.getOperator());
		assertTrue(unitExpression2.getExpression() instanceof ConstantExpression);
		assertEquals("null", ((ConstantExpression)unitExpression2.getExpression()).getValue());
	}

	@Test
	public void parseBetween()
		throws ParserException
	{
		String src="a between b and c";
		Expression expression=parse(src);
		assertTrue(expression instanceof BetweenExpression);
		BinaryExpression betweenExpression=(BetweenExpression)expression;
		assertEquals("between", betweenExpression.getOperator());
		assertTrue(betweenExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)betweenExpression.getExpression1()).getIdentifier());

		assertTrue(betweenExpression.getExpression2() instanceof AndExpression);
		BinaryExpression binaryExpression=(AndExpression)betweenExpression.getExpression2();
		assertEquals("and", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseLike()
		throws ParserException
	{
		String src="a like b";
		Expression expression=parse(src);
		assertTrue(expression instanceof LikeExpression);
		BinaryExpression binaryExpression=(LikeExpression)expression;
		assertEquals("like", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseIn()
		throws ParserException
	{
		String src="a in b";
		Expression expression=parse(src);
		assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		assertEquals("in", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseInParehentesized()
		throws ParserException
	{
		String src="a in (b)";
		Expression expression=parse(src);
		assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		assertEquals("in", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);
		assertEquals("b", (((IdentifierExpression)((ParehentesizedExpression)binaryExpression.getExpression2()).getExpression())).getIdentifier());
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

	@Test
	public void parseArrayInList()
		throws ParserException
	{
		String src="(a,b) in (a1,a2,a3)";
		Expression expression=parse(src);
		assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		assertEquals("in", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof ParehentesizedExpression);
		assertTrue(((ParehentesizedExpression)binaryExpression.getExpression1()).getExpression() instanceof ExpressionList);
		ExpressionList expressionList=(ExpressionList)(((ParehentesizedExpression)binaryExpression.getExpression1()).getExpression());
		assertEquals(2, expressionList.getExpressions().size());
		assertEquals("a", ((IdentifierExpression)expressionList.getExpressions().get(0)).getIdentifier());
		assertEquals("b", ((IdentifierExpression)expressionList.getExpressions().get(1)).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);
		ParehentesizedExpression set=(ParehentesizedExpression)binaryExpression.getExpression2();
		assertTrue(set.getExpression() instanceof ExpressionList);
		ExpressionList expressionList2=(ExpressionList)set.getExpression();
		assertEquals(3, expressionList2.getExpressions().size());
		assertEquals("a1", ((IdentifierExpression)expressionList2.getExpressions().get(0)).getIdentifier());
		assertEquals("a2", ((IdentifierExpression)expressionList2.getExpressions().get(1)).getIdentifier());
		assertEquals("a3", ((IdentifierExpression)expressionList2.getExpressions().get(2)).getIdentifier());
	}

	@Test
	public void parseInParehentesizedSubselect()
		throws ParserException
	{
		String src="a in (SELECT c FROM d)";
		Expression expression=parse(src);
		assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		assertEquals("in", binaryExpression.getOperator());
		assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);
		ParehentesizedExpression set=(ParehentesizedExpression)binaryExpression.getExpression2();
		assertTrue(set.getExpression() instanceof SelectSentence);
	}
}
