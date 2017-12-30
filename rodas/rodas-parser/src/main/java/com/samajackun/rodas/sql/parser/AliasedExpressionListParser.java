package com.samajackun.rodas.sql.parser;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.sql.model.AliasedExpression;
import com.samajackun.rodas.sql.model.Expression;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;

public class AliasedExpressionListParser extends AbstractParser<List<AliasedExpression>>
{
	private static final AliasedExpressionListParser INSTANCE=new AliasedExpressionListParser();

	public static AliasedExpressionListParser getInstance()
	{
		return INSTANCE;
	}

	private AliasedExpressionListParser()
	{
	}

	private enum State {
		INITIAL, COMPLETE, READ_EXPRESSION, EXPECTING_ALIAS, READ_ALIAS,
	};

	@Override
	public List<AliasedExpression> parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		List<AliasedExpression> expressions=new ArrayList<AliasedExpression>();
		Expression expression=null;
		State state=State.INITIAL;
		while (state != State.COMPLETE && tokenizer.hasMoreTokens())
		{
			SqlToken token=tokenizer.nextUsefulToken();
			switch (state)
			{
				case INITIAL:
					tokenizer.pushBack();
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
							AliasedExpression aliasedExpression=new AliasedExpression(expression, token.getImage());
							expressions.add(aliasedExpression);
							state=State.READ_ALIAS;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
					}
					break;
				case EXPECTING_ALIAS:
					switch (token.getType())
					{
						case IDENTIFIER:
						case DOUBLE_QUOTED_TEXT_LITERAL:
							AliasedExpression aliasedExpression=new AliasedExpression(expression, token.getImage());
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
							tokenizer.pushBack();
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
