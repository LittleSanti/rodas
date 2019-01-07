package com.samajackun.rodas.sql.parser;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.BooleanExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionList;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.WithDeclaration;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken.Type;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public final class SelectSentenceParser extends AbstractParser<SelectSentence>
{
	private static final SelectSentenceParser INSTANCE=new SelectSentenceParser();

	public static SelectSentenceParser getInstance()
	{
		return INSTANCE;
	}

	private SelectSentenceParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, READING_OPTIONS, READING_SELECT_EXPRESSIONS, READ_SELECT_CLAUSE, READING_GROUP_CLAUSE, READING_ORDER_CLAUSE, READ_FROM_CLAUSE, READ_WHERE_CLAUSE, READ_GROUP_CLAUSE, READING_HAVING_CLAUSE, READ_HAVING_CLAUSE, READ_GROUP_EXPECTING_BY, EXPECTING_WITH_ALIAS, READ_WITH_DECLARATION
	};

	@Override
	public SelectSentence parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		State state=State.INITIAL;
		SelectSentence selectSentence=null;
		List<WithDeclaration> withDeclarations=new ArrayList<WithDeclaration>();
		while (state != State.COMPLETE && tokenizer.hasMoreTokens())
		{
			SqlToken token=tokenizer.nextUsefulToken();
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case KEYWORD_SELECT:
							selectSentence=new SelectSentence();
							selectSentence.getWithDeclarations().addAll(withDeclarations);
							state=State.READING_OPTIONS;
							break;
						case KEYWORD_WITH:
							state=State.EXPECTING_WITH_ALIAS;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case EXPECTING_WITH_ALIAS:
					if (token.getType() == SqlToken.Type.IDENTIFIER)
					{
						String alias=token.getImage();
						tokenizer.matchToken(Type.KEYWORD_AS);
						tokenizer.matchToken(Type.PARENTHESIS_START);
						SelectSentence withSentence=parse(tokenizer);
						tokenizer.matchToken(Type.PARENTHESIS_END);
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
						case COMMA:
							state=State.EXPECTING_WITH_ALIAS;
							break;
						case KEYWORD_SELECT:
							selectSentence=new SelectSentence();
							selectSentence.getWithDeclarations().addAll(withDeclarations);
							state=State.READING_OPTIONS;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READING_OPTIONS:
					if (token.isKeyword())
					{
						selectSentence.getOptions().add(token.getImage());
					}
					else
					{
						tokenizer.pushBack();
						List<AliasedExpression> selectExpressions=AliasedExpressionListParser.getInstance().parse(tokenizer);
						selectSentence.addSelectExpressions(selectExpressions);
						state=State.READ_SELECT_CLAUSE;
					}
					break;
				case READ_SELECT_CLAUSE:
					switch (token.getType())
					{
						case KEYWORD_FROM:
							List<Source> sources=SourceListParser.getInstance().parse(tokenizer);
							selectSentence.getSourceDeclarations().addAll(sources);
							state=State.READ_FROM_CLAUSE;
							break;
						case KEYWORD_WHERE:
							Expression expression=ExpressionParser.getInstance().parse(tokenizer);
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
						case KEYWORD_GROUP:
							state=State.READ_GROUP_EXPECTING_BY;
							break;
						case KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READ_FROM_CLAUSE:
					switch (token.getType())
					{
						case KEYWORD_WHERE:
							Expression expression=ExpressionParser.getInstance().parse(tokenizer);
							selectSentence.setWhereExpression(expression);
							state=State.READ_WHERE_CLAUSE;
							break;
						case KEYWORD_GROUP:
							state=State.READ_GROUP_EXPECTING_BY;
							break;
						case KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						case PARENTHESIS_END:
							tokenizer.pushBack();
							state=State.COMPLETE;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READ_WHERE_CLAUSE:
					switch (token.getType())
					{
						case KEYWORD_GROUP:
							state=State.READ_GROUP_EXPECTING_BY;
							break;
						case KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
					}
					break;
				case READ_GROUP_EXPECTING_BY:
					switch (token.getType())
					{
						case KEYWORD_BY:
							ExpressionList groupExpressions=ExpressionParser.getInstance().parseExpressionList(tokenizer);
							selectSentence.getGroupExpressions().addAll(groupExpressions.getExpressions());
							state=State.READ_GROUP_CLAUSE;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READ_GROUP_CLAUSE:
					switch (token.getType())
					{
						case KEYWORD_HAVING:
							Expression expression3=ExpressionParser.getInstance().parse(tokenizer);
							selectSentence.setHavingExpression(expression3);
							state=State.READ_HAVING_CLAUSE;
							break;
						case KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READ_HAVING_CLAUSE:
					switch (token.getType())
					{
						case KEYWORD_ORDER:
							state=State.READING_ORDER_CLAUSE;
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
					break;
				case READING_ORDER_CLAUSE:
					switch (token.getType())
					{
						case KEYWORD_BY:
							ExpressionList orderExpressions=ExpressionParser.getInstance().parseExpressionList(tokenizer);
							selectSentence.getOrderExpressions().addAll(orderExpressions.getExpressions());
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
		return selectSentence;
	}
}
