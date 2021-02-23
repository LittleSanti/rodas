package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.BooleanExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionCollection;
import com.samajackun.rodas.core.model.OrderClause;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.WithDeclaration;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;

public class GenericSelectSentenceParser extends AbstractParser<SelectSentence> implements PartialParser
{
	private enum State {
		INITIAL, COMPLETE, READING_OPTIONS, READING_SELECT_EXPRESSIONS, READ_SELECT_CLAUSE, READING_GROUP_CLAUSE, READING_ORDER_CLAUSE, READ_FROM_CLAUSE, READ_WHERE_CLAUSE, READ_GROUP_CLAUSE, READING_HAVING_CLAUSE, READ_HAVING_CLAUSE, READ_GROUP_EXPECTING_BY, EXPECTING_WITH_ALIAS, READ_WITH_DECLARATION
	};

	private final OrderClausesParser orderClausesParser;

	public GenericSelectSentenceParser(ParserFactory parserFactory)
	{
		super(parserFactory);
		this.orderClausesParser=new OrderClausesParser(parserFactory);
	}

	@Override
	public SelectSentence parse(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		State state=State.INITIAL;
		SelectSentence selectSentence=null;
		List<WithDeclaration> withDeclarations=new ArrayList<>();
		do
		{
			Token token=tokenizer.nextOptionalUsefulToken();
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case SqlTokenTypes.KEYWORD_SELECT:
							selectSentence=new SelectSentence();
							selectSentence.getWithDeclarations().addAll(withDeclarations);
							state=State.READING_OPTIONS;
							break;
						case SqlTokenTypes.KEYWORD_WITH:
							state=State.EXPECTING_WITH_ALIAS;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case EXPECTING_WITH_ALIAS:
					if (token.getType().equals(SqlTokenTypes.IDENTIFIER))
					{
						String alias=token.getValue();
						tokenizer.matchToken(SqlTokenTypes.KEYWORD_AS);
						tokenizer.matchToken(SqlTokenTypes.PARENTHESIS_START);
						SelectSentence withSentence=parse(tokenizer, parserContext);
						tokenizer.matchToken(SqlTokenTypes.PARENTHESIS_END);
						WithDeclaration withDeclaration=new WithDeclaration(alias, withSentence);
						withDeclarations.add(withDeclaration);
						state=State.READ_WITH_DECLARATION;
					}
					else
					{
						throw new UnexpectedTokenException(token);
					}
					break;
				case READ_WITH_DECLARATION:
					switch (token.getType())
					{
						case SqlTokenTypes.COMMA:
							state=State.EXPECTING_WITH_ALIAS;
							break;
						case SqlTokenTypes.KEYWORD_SELECT:
							selectSentence=new SelectSentence();
							selectSentence.getWithDeclarations().addAll(withDeclarations);
							state=State.READING_OPTIONS;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READING_OPTIONS:
					if (SqlTokenTypes.isKeyword(token.getType()))
					{
						selectSentence.getOptions().add(token.getValue());
					}
					else
					{
						tokenizer.pushBack(token);
						List<AliasedExpression> selectExpressions=AliasedExpressionListParser.getInstance().parse(tokenizer, parserContext);
						selectSentence.addSelectExpressions(selectExpressions);
						state=State.READ_SELECT_CLAUSE;
					}
					break;
				case READ_SELECT_CLAUSE:
					switch (token.getType())
					{
						case SqlTokenTypes.KEYWORD_FROM:
							Source source=getParserFactory().getSourceParser().parse(tokenizer, parserContext);
							selectSentence.setSource(source);
							state=State.READ_FROM_CLAUSE;
							break;
						case SqlTokenTypes.KEYWORD_WHERE:
							Expression expression=getParserFactory().getExpressionParser().parse(tokenizer, parserContext);
							if (expression instanceof BooleanExpression)
							{
								selectSentence.setWhereExpression(expression);
							}
							else
							{
								throw new BooleanExpressionExpectedException("in the WHERE clause");
							}
							state=State.READ_WHERE_CLAUSE;
							break;
						case SqlTokenTypes.KEYWORD_GROUP:
							state=State.READ_GROUP_EXPECTING_BY;
							break;
						case SqlTokenTypes.KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						case SqlTokenTypes.SEMICOLON:
							state=State.COMPLETE;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READ_FROM_CLAUSE:
					switch (token.getType())
					{
						case SqlTokenTypes.KEYWORD_WHERE:
							Expression expression=getParserFactory().getExpressionParser().parse(tokenizer, parserContext);
							selectSentence.setWhereExpression(expression);
							state=State.READ_WHERE_CLAUSE;
							break;
						case SqlTokenTypes.KEYWORD_GROUP:
							state=State.READ_GROUP_EXPECTING_BY;
							break;
						case SqlTokenTypes.KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						default:
							tokenizer.pushBack(token);
							state=State.COMPLETE;
							break;
					}
					break;
				case READ_WHERE_CLAUSE:
					switch (token.getType())
					{
						case SqlTokenTypes.KEYWORD_GROUP:
							state=State.READ_GROUP_EXPECTING_BY;
							break;
						case SqlTokenTypes.KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						default:
							tokenizer.pushBack(token);
							state=State.COMPLETE;
					}
					break;
				case READ_GROUP_EXPECTING_BY:
					switch (token.getType())
					{
						case SqlTokenTypes.KEYWORD_BY:
							ExpressionCollection groupExpressions=ExpressionCollectionParser.getInstance().parse(tokenizer, parserContext);
							selectSentence.getGroupExpressions().addAll(groupExpressions.getExpressions());
							state=State.READ_GROUP_CLAUSE;
							break;
						default:
							throw new UnexpectedTokenException(token, SqlTokenTypes.KEYWORD_BY);
					}
					break;
				case READ_GROUP_CLAUSE:
					switch (token.getType())
					{
						case SqlTokenTypes.KEYWORD_HAVING:
							Expression expression3=getParserFactory().getExpressionParser().parse(tokenizer, parserContext);
							selectSentence.setHavingExpression(expression3);
							state=State.READ_HAVING_CLAUSE;
							break;
						case SqlTokenTypes.KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						default:
							tokenizer.pushBack(token);
							state=State.COMPLETE;
					}
					break;
				case READ_HAVING_CLAUSE:
					switch (token.getType())
					{
						case SqlTokenTypes.KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						default:
							tokenizer.pushBack(token);
							state=State.COMPLETE;
					}
					break;
				case READING_ORDER_CLAUSE:
					switch (token.getType())
					{
						case SqlTokenTypes.KEYWORD_BY:
							List<OrderClause> orderClauses=this.orderClausesParser.parse(tokenizer, parserContext);
							selectSentence.getOrderClauses().addAll(orderClauses);
							state=State.COMPLETE;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case COMPLETE:
					// TODO
					break;
				default:
					break;
			}
		}
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		return selectSentence;
	}
}
