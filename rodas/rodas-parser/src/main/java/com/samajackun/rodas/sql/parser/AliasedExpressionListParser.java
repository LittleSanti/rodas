package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;

public class AliasedExpressionListParser extends AbstractParser<List<AliasedExpression>>
{
	private static final AliasedExpressionListParser INSTANCE=new AliasedExpressionListParser();

	public static AliasedExpressionListParser getInstance()
	{
		return AliasedExpressionListParser.INSTANCE;
	}

	private AliasedExpressionListParser()
	{
		super(DefaultParserFactory.getInstance());
	}

	private enum State {
		INITIAL, COMPLETE, READ_EXPRESSION, EXPECTING_ALIAS, READ_ALIAS,
	};

	@Override
	public List<AliasedExpression> parse(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		List<AliasedExpression> expressions=new ArrayList<>();
		Expression expression=null;
		State state=State.INITIAL;
		while (state != State.COMPLETE)
		{
			Token token=tokenizer.nextOptionalUsefulToken();
			if (token == null)
			{
				break;
			}
			switch (state)
			{
				case INITIAL:
					tokenizer.pushBack(token);
					expression=ExpressionParser.getInstance().parse(tokenizer, parserContext);
					// expressions.add(new AliasedExpression(expression, null));
					state=State.READ_EXPRESSION;
					break;
				case READ_EXPRESSION:
					switch (token.getType())
					{
						case SqlTokenTypes.COMMA:
							Name nameAlias=expression.getName();
							String alias=nameAlias == null
								? null
								: nameAlias.asString();
							expressions.add(new AliasedExpression(expression, alias));
							state=State.INITIAL;
							break;
						case SqlTokenTypes.KEYWORD_AS:
							state=State.EXPECTING_ALIAS;
							break;
						case SqlTokenTypes.IDENTIFIER:
						case SqlTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL:
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
						case SqlTokenTypes.IDENTIFIER:
						case SqlTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL:
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
						case SqlTokenTypes.COMMA:
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
		if (expression != null && !(expression instanceof AliasedExpression))
		{
			String alias=buildAutoAlias(expression, expressions.size());
			AliasedExpression aliasedExpression=new AliasedExpression(expression, alias);
			expressions.add(aliasedExpression);
		}
		return expressions;
	}

	private String buildAutoAlias(Expression expression, int n)
	{
		// Hay que construir un alias "al vuelo":
		String alias;
		Name name=expression.getName();
		if (name == null)
		{
			alias=null;
		}
		else
		{
			Name base=name.getBase();
			alias=base.asString();
		}
		if (alias == null)
		{
			alias="COL_" + n;
		}
		return alias;
	}

}
