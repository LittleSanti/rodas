package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;

public class GenericExpressionListParser extends AbstractParser<List<Expression>>
{
	private enum State {
		INITIAL, COMPLETE, READ_EXPRESSION,
	};

	public GenericExpressionListParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	@Override
	public List<Expression> parse(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		List<Expression> expressions=new ArrayList<>();
		State state=State.INITIAL;
		do
		{
			Token token=tokenizer.nextOptionalUsefulToken();
			if (token != null)
			{
				switch (state)
				{
					case INITIAL:
						switch (token.getType())
						{
							case SqlTokenTypes.PARENTHESIS_START:
								Token token2=tokenizer.nextOptionalUsefulToken();
								if (token2.getType().equals(SqlTokenTypes.PARENTHESIS_END))
								{
									tokenizer.pushBack(token2);
									state=State.COMPLETE;
								}
								else
								{
									tokenizer.pushBack(token2);

									Expression expression=ExpressionParser.getInstance().parse(tokenizer, parserContext);
									if (expression != null)
									{
										expressions.add(expression);
									}
								}
								state=State.READ_EXPRESSION;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					case READ_EXPRESSION:
						switch (token.getType())
						{
							case SqlTokenTypes.COMMA:
								Expression expression=ExpressionParser.getInstance().parse(tokenizer, parserContext);
								if (expression == null)
								{
									throw new ParserException("An expression was expected after a comma");
								}
								expressions.add(expression);
								state=State.READ_EXPRESSION;
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
		}
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		return expressions;
	}

}