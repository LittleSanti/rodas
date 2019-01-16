package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.tokenizer.MatchingSqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;

public class AliasedExpressionListParser extends AbstractParser<List<AliasedExpression>>
{
	private static final AliasedExpressionListParser INSTANCE=new AliasedExpressionListParser();

	public static AliasedExpressionListParser getInstance()
	{
		return AliasedExpressionListParser.INSTANCE;
	}

	private AliasedExpressionListParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, READ_EXPRESSION, EXPECTING_ALIAS, READ_ALIAS,
	};

	@Override
	public List<AliasedExpression> parse(MatchingSqlTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		List<AliasedExpression> expressions=new ArrayList<>();
		Expression expression=null;
		State state=State.INITIAL;
		while (state != State.COMPLETE)
		{
			SqlToken token=tokenizer.nextUsefulToken();
			if (token == null)
			{
				break;
			}
			switch (state)
			{
				case INITIAL:
					tokenizer.pushBack(token);
					expression=ExpressionParser.getInstance().parse(tokenizer);
					// expressions.add(new AliasedExpression(expression, null));
					state=State.READ_EXPRESSION;
					break;
				case READ_EXPRESSION:
					switch (token.getType())
					{
						case COMMA:
							String alias=expression.getName();
							expressions.add(new AliasedExpression(expression, alias));
							state=State.INITIAL;
							break;
						case KEYWORD_AS:
							state=State.EXPECTING_ALIAS;
							break;
						case IDENTIFIER:
						case DOUBLE_QUOTED_TEXT_LITERAL:
							AliasedExpression aliasedExpression=new AliasedExpression(expression, token.getValue());
							expressions.add(aliasedExpression);
							state=State.READ_ALIAS;
							break;
						default:
							tokenizer.pushBack(token);
							state=State.COMPLETE;
					}
					break;
				case EXPECTING_ALIAS:
					switch (token.getType())
					{
						case IDENTIFIER:
						case DOUBLE_QUOTED_TEXT_LITERAL:
							AliasedExpression aliasedExpression=new AliasedExpression(expression, token.getValue());
							expressions.add(aliasedExpression);
							expression=null;
							state=State.READ_ALIAS;
							break;
						default:
							throw new ParserException("Expecting alias after AS");
					}
					break;
				case READ_ALIAS:
					switch (token.getType())
					{
						case COMMA:
							if (expression == null)
							{
								throw new RuntimeException("expression no debería ser null");
							}
							AliasedExpression aliasedExpression=new AliasedExpression(expression, null);
							expressions.add(aliasedExpression);
							expression=null;
							state=State.INITIAL;
							break;
						default:
							tokenizer.pushBack(token);
							state=State.COMPLETE;
					}
					break;
				default:
					// Ignorar.
			}
		}
		if (expression != null)
		{
			AliasedExpression aliasedExpression=new AliasedExpression(expression, null);
			expressions.add(aliasedExpression);
		}
		return expressions;
	}

}
