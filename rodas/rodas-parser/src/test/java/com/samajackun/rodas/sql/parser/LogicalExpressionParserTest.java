package com.samajackun.rodas.sql.parser;

import java.io.IOException;

import org.junit.Assert;
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
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.CharSequenceSource;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizerSettings;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizerSettings.WhitespaceBehaviour;

public class LogicalExpressionParserTest
{
	private Expression parse(String src)
		throws TokenizerException,
		ParserException,
		IOException
	{
		SqlTokenizerSettings settings=new SqlTokenizerSettings();
		settings.setWhitespaceBehaviour(WhitespaceBehaviour.IGNORE);
		MatchingSqlTokenizer tokenizer=new MatchingSqlTokenizer(new SqlTokenizer(new PushBackSource(new CharSequenceSource(src)), settings));
		Expression expression=LogicalExpressionParser.getInstance().parse(tokenizer);
		return expression;
	}

	@Test
	public void parseAnd()
		throws ParserException,
		IOException
	{
		String src="a and b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof AndExpression);
		BinaryExpression binaryExpression=(AndExpression)expression;
		Assert.assertEquals("and", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseNot()
		throws ParserException,
		IOException
	{
		String src="not a";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof NotExpression);
		UnitExpression unitExpression=(NotExpression)expression;
		Assert.assertEquals("not", unitExpression.getOperator());
		Assert.assertTrue(unitExpression.getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)unitExpression.getExpression()).getIdentifier());
	}

	@Test
	public void parseNotAnd()
		throws ParserException,
		IOException
	{
		String src="not a and b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof AndExpression);
		BinaryExpression binaryExpression=(AndExpression)expression;
		Assert.assertEquals("and", binaryExpression.getOperator());
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
		UnitExpression expression1=(NotExpression)binaryExpression.getExpression1();
		Assert.assertEquals("not", expression1.getOperator());
		Assert.assertTrue(expression1.getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)expression1.getExpression()).getIdentifier());
	}

	@Test
	public void parseNotOr()
		throws ParserException,
		IOException
	{
		String src="not a or b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof OrExpression);
		BinaryExpression binaryExpression=(OrExpression)expression;
		Assert.assertEquals("or", binaryExpression.getOperator());
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
		UnitExpression expression1=(NotExpression)binaryExpression.getExpression1();
		Assert.assertEquals("not", expression1.getOperator());
		Assert.assertTrue(expression1.getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)expression1.getExpression()).getIdentifier());
	}

	@Test
	public void parseOr()
		throws ParserException,
		IOException
	{
		String src="a or b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof OrExpression);
		BinaryExpression binaryExpression=(OrExpression)expression;
		Assert.assertEquals("or", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseOrAnd()
		throws ParserException,
		IOException
	{
		String src="a or b and c";
		Expression expression=parse(src);

		Assert.assertTrue(expression instanceof OrExpression);
		BinaryExpression binaryExpression=(OrExpression)expression;
		Assert.assertEquals("or", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof AndExpression);
		BinaryExpression expression2=(AndExpression)binaryExpression.getExpression2();
		Assert.assertEquals("and", expression2.getOperator());
		Assert.assertTrue(expression2.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)expression2.getExpression1()).getIdentifier());
		Assert.assertTrue(expression2.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)expression2.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAndOr()
		throws ParserException,
		IOException
	{
		String src="a and b or c";
		Expression expression=parse(src);

		Assert.assertTrue(expression instanceof OrExpression);
		BinaryExpression binaryExpression=(OrExpression)expression;
		Assert.assertEquals("or", binaryExpression.getOperator());

		Assert.assertTrue(binaryExpression.getExpression1() instanceof AndExpression);
		BinaryExpression expression1=(AndExpression)binaryExpression.getExpression1();
		Assert.assertEquals("and", expression1.getOperator());
		Assert.assertTrue(expression1.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)expression1.getExpression1()).getIdentifier());
		Assert.assertTrue(expression1.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)expression1.getExpression2()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseAndAnd()
		throws ParserException,
		IOException
	{
		String src="a and b and c";
		Expression expression=parse(src);

		Assert.assertTrue(expression instanceof AndExpression);
		BinaryExpression binaryExpression=(AndExpression)expression;
		Assert.assertEquals("and", binaryExpression.getOperator());

		Assert.assertTrue(binaryExpression.getExpression1() instanceof AndExpression);
		BinaryExpression expression1=(AndExpression)binaryExpression.getExpression1();
		Assert.assertEquals("and", expression1.getOperator());
		Assert.assertTrue(expression1.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)expression1.getExpression1()).getIdentifier());
		Assert.assertTrue(expression1.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)expression1.getExpression2()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseExists()
		throws ParserException,
		IOException
	{
		String src="exists a";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof ExistsExpression);
		UnitExpression unitExpression=(ExistsExpression)expression;
		Assert.assertEquals("exists", unitExpression.getOperator());
		Assert.assertTrue(unitExpression.getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)unitExpression.getExpression()).getIdentifier());
	}

	@Test
	public void parseNotExists()
		throws ParserException,
		IOException
	{
		String src="not exists a";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof NotExpression);
		UnitExpression unitExpression=(NotExpression)expression;
		Assert.assertEquals("not", unitExpression.getOperator());
		Assert.assertTrue(unitExpression.getExpression() instanceof ExistsExpression);

		UnitExpression unitExpression2=(ExistsExpression)unitExpression.getExpression();
		Assert.assertEquals("exists", unitExpression2.getOperator());
		Assert.assertTrue(unitExpression2.getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)unitExpression2.getExpression()).getIdentifier());
	}

	@Test
	public void parseNotNot()
		throws ParserException,
		IOException
	{
		String src="not not a";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof NotExpression);
		UnitExpression unitExpression=(NotExpression)expression;
		Assert.assertEquals("not", unitExpression.getOperator());
		Assert.assertTrue(unitExpression.getExpression() instanceof NotExpression);

		UnitExpression unitExpression2=(NotExpression)unitExpression.getExpression();
		Assert.assertEquals("not", unitExpression2.getOperator());
		Assert.assertTrue(unitExpression2.getExpression() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)unitExpression2.getExpression()).getIdentifier());
	}

	@Test
	public void parseIsNull()
		throws ParserException,
		IOException
	{
		String src="a is null";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof IsExpression);
		BinaryExpression binaryExpression=(IsExpression)expression;
		Assert.assertEquals("is", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)binaryExpression.getExpression2()).getValue());
	}

	@Test
	public void parseIsNotNull()
		throws ParserException,
		IOException
	{
		String src="a is not null";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof IsExpression);
		BinaryExpression binaryExpression=(IsExpression)expression;
		Assert.assertEquals("is", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());

		Assert.assertTrue(binaryExpression.getExpression2() instanceof NotExpression);
		UnitExpression unitExpression2=(NotExpression)binaryExpression.getExpression2();
		Assert.assertEquals("not", unitExpression2.getOperator());
		Assert.assertTrue(unitExpression2.getExpression() instanceof ConstantExpression);
		Assert.assertEquals("null", ((ConstantExpression)unitExpression2.getExpression()).getValue());
	}

	@Test
	public void parseBetween()
		throws ParserException,
		IOException
	{
		String src="a between b and c";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof BetweenExpression);
		BinaryExpression betweenExpression=(BetweenExpression)expression;
		Assert.assertEquals("between", betweenExpression.getOperator());
		Assert.assertTrue(betweenExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)betweenExpression.getExpression1()).getIdentifier());

		Assert.assertTrue(betweenExpression.getExpression2() instanceof AndExpression);
		BinaryExpression binaryExpression=(AndExpression)betweenExpression.getExpression2();
		Assert.assertEquals("and", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("c", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseLike()
		throws ParserException,
		IOException
	{
		String src="a like b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof LikeExpression);
		BinaryExpression binaryExpression=(LikeExpression)expression;
		Assert.assertEquals("like", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseIn()
		throws ParserException,
		IOException
	{
		String src="a in b";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		Assert.assertEquals("in", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof IdentifierExpression);
		Assert.assertEquals("b", ((IdentifierExpression)binaryExpression.getExpression2()).getIdentifier());
	}

	@Test
	public void parseInParehentesized()
		throws ParserException,
		IOException
	{
		String src="a in (b)";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		Assert.assertEquals("in", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);
		Assert.assertEquals("b", ((IdentifierExpression)((ParehentesizedExpression)binaryExpression.getExpression2()).getExpression()).getIdentifier());
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

	@Test
	public void parseArrayInList()
		throws ParserException,
		IOException
	{
		String src="(a,b) in (a1,a2,a3)";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		Assert.assertEquals("in", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof ParehentesizedExpression);
		Assert.assertTrue(((ParehentesizedExpression)binaryExpression.getExpression1()).getExpression() instanceof ExpressionList);
		ExpressionList expressionList=(ExpressionList)((ParehentesizedExpression)binaryExpression.getExpression1()).getExpression();
		Assert.assertEquals(2, expressionList.getExpressions().size());
		Assert.assertEquals("a", ((IdentifierExpression)expressionList.getExpressions().get(0)).getIdentifier());
		Assert.assertEquals("b", ((IdentifierExpression)expressionList.getExpressions().get(1)).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);
		ParehentesizedExpression set=(ParehentesizedExpression)binaryExpression.getExpression2();
		Assert.assertTrue(set.getExpression() instanceof ExpressionList);
		ExpressionList expressionList2=(ExpressionList)set.getExpression();
		Assert.assertEquals(3, expressionList2.getExpressions().size());
		Assert.assertEquals("a1", ((IdentifierExpression)expressionList2.getExpressions().get(0)).getIdentifier());
		Assert.assertEquals("a2", ((IdentifierExpression)expressionList2.getExpressions().get(1)).getIdentifier());
		Assert.assertEquals("a3", ((IdentifierExpression)expressionList2.getExpressions().get(2)).getIdentifier());
	}

	@Test
	public void parseInParehentesizedSubselect()
		throws ParserException,
		IOException
	{
		String src="a in (SELECT c FROM d)";
		Expression expression=parse(src);
		Assert.assertTrue(expression instanceof InExpression);
		BinaryExpression binaryExpression=(InExpression)expression;
		Assert.assertEquals("in", binaryExpression.getOperator());
		Assert.assertTrue(binaryExpression.getExpression1() instanceof IdentifierExpression);
		Assert.assertEquals("a", ((IdentifierExpression)binaryExpression.getExpression1()).getIdentifier());
		Assert.assertTrue(binaryExpression.getExpression2() instanceof ParehentesizedExpression);
		ParehentesizedExpression set=(ParehentesizedExpression)binaryExpression.getExpression2();
		Assert.assertTrue(set.getExpression() instanceof SelectSentence);
	}
}
