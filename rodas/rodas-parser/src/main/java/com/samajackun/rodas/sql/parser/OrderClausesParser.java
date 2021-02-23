package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.OrderClause;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;

public class OrderClausesParser extends AbstractParser<List<OrderClause>>
{
	public OrderClausesParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	private enum State {
		INITIAL, EXPECTING_CLAUSE, COMPLETE, READ_CLAUSE,
	};

	@Override
	public List<OrderClause> parse(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		List<OrderClause> clauses=new ArrayList<>();
		State state=State.EXPECTING_CLAUSE;
		do
		{
			switch (state)
			{
				case EXPECTING_CLAUSE:
					Expression expression=ExpressionParser.getInstance().parse(tokenizer, parserContext);
					Token token=tokenizer.nextOptionalUsefulToken();
					if (token != null)
					{
						switch (token.getType())
						{
							case SqlTokenTypes.KEYWORD_ASC:
								OrderClause clause=new OrderClause(expression, OrderClause.Direction.ASCENDING);
								clauses.add(clause);
								state=State.READ_CLAUSE;
								break;
							case SqlTokenTypes.COMMA:
								OrderClause clause2=new OrderClause(expression, OrderClause.Direction.ASCENDING);
								clauses.add(clause2);
								state=State.EXPECTING_CLAUSE;
								break;
							case SqlTokenTypes.KEYWORD_DESC:
								OrderClause clause3=new OrderClause(expression, OrderClause.Direction.DESCENDING);
								clauses.add(clause3);
								state=State.READ_CLAUSE;
								break;
							case SqlTokenTypes.PARENTHESIS_END:
								state=State.COMPLETE;
								break;
							default:
								throw new UnexpectedTokenException(token, SqlTokenTypes.KEYWORD_ASC, SqlTokenTypes.KEYWORD_DESC, SqlTokenTypes.COMMA);
						}
					}
					else
					{
						OrderClause clause=new OrderClause(expression, OrderClause.Direction.ASCENDING);
						clauses.add(clause);
					}
					break;
				case READ_CLAUSE:
					Token token2=tokenizer.nextOptionalUsefulToken();
					if (token2 == null)
					{
						state=State.COMPLETE;
					}
					else
					{
						switch (token2.getType())
						{
							case SqlTokenTypes.COMMA:
								state=State.EXPECTING_CLAUSE;
								break;
							case SqlTokenTypes.PARENTHESIS_END:
								state=State.COMPLETE;
								break;
							default:
								throw new UnexpectedTokenException(token2, SqlTokenTypes.KEYWORD_ASC, SqlTokenTypes.KEYWORD_DESC, SqlTokenTypes.COMMA);
						}
					}
					break;
				default:
			}
		}
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		switch (state)
		{
			case INITIAL:
				throw new ParserException("Expecting order clause");
			case EXPECTING_CLAUSE:
				if (tokenizer.tokenWasRead())
				{
					throw new ParserException("Expecting order clause");
				}
			default:
		}
		return clauses;
	}

}
