package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.AliasedSource;
import com.samajackun.rodas.core.model.BooleanExpression;
import com.samajackun.rodas.core.model.CrossSource;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.core.model.OnJoinedSource;
import com.samajackun.rodas.core.model.ParehentesizedSource;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.core.model.TableSource;
import com.samajackun.rodas.core.model.UsingJoinedSource;
import com.samajackun.rodas.parsing.parser.AbstractParser;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.Token;

public class SourceParser extends AbstractParser<Source>
{
	private static final SourceParser INSTANCE=new SourceParser();

	private SourceParser(ParserFactory parserFactory)
	{
		super(parserFactory);
	}

	public static SourceParser getInstance()
	{
		return INSTANCE;
	}

	private SourceParser()
	{
		this(DefaultParserFactory.getInstance());
	}

	private enum State {
		INITIAL, EXPECTING_COMMA, EXPECTING_SOURCE, COMPLETE, READ_SOURCE, EXPECTING_ALIAS, EXPECTING_ON_OR_USING, EXPECTING_JOIN, EXPECTING_AS_OR_ALIAS
	};

	@Override
	public Source parse(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		List<Source> sources=new ArrayList<>();
		State state=State.INITIAL;
		do
		{
			switch (state)
			{
				case INITIAL:
				case EXPECTING_SOURCE:
					Source source=parseSingleSource(tokenizer, parserContext);
					if (source == null)
					{
						throw new SourceExpectedException();
					}
					else
					{
						sources.add(source);
						state=State.EXPECTING_COMMA;
					}
					break;
				case EXPECTING_COMMA:
					Token token=tokenizer.nextOptionalUsefulToken();
					if (token != null)
					{
						if (token.getType().equals(SqlTokenTypes.COMMA))
						{
							state=State.EXPECTING_SOURCE;
						}
						else
						{
							tokenizer.pushBack(token);
							state=State.COMPLETE;
						}
					}
					else
					{
						state=State.COMPLETE;
					}
					break;
				default: // Ignorar.
			}
		}
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		Source sourceTotal=sources.size() == 1
			? sources.get(0)
			: new CrossSource(sources);
		return sourceTotal;
	}

	public Source parseSingleSource(AbstractMatchingTokenizer tokenizer, ParserContext parserContext)
		throws ParserException,
		IOException
	{
		Source source=null;
		Source source2=null;
		State state=State.INITIAL;
		OnJoinedSource.Type joinType=null;
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
							case SqlTokenTypes.IDENTIFIER:
							case SqlTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL:
								source=new TableSource(token.getValue());
								state=State.READ_SOURCE;
								break;
							case SqlTokenTypes.PARENTHESIS_START:
								Source source1=parseSingleSource(tokenizer, parserContext);
								tokenizer.matchToken(SqlTokenTypes.PARENTHESIS_END);
								source=new ParehentesizedSource(source1);
								state=State.READ_SOURCE;
								break;
							case SqlTokenTypes.KEYWORD_SELECT:
								tokenizer.pushBack(token);
								source=SelectSentenceParser.getInstance().parse(tokenizer, parserContext);
								state=State.READ_SOURCE;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					case READ_SOURCE:
						switch (token.getType())
						{
							case SqlTokenTypes.KEYWORD_INNER:
								joinType=OnJoinedSource.Type.INNER;
								state=State.EXPECTING_JOIN;
								break;
							case SqlTokenTypes.KEYWORD_OUTER:
								joinType=OnJoinedSource.Type.OUTER;
								state=State.EXPECTING_JOIN;
								break;
							case SqlTokenTypes.KEYWORD_LEFT:
								joinType=OnJoinedSource.Type.LEFT;
								state=State.EXPECTING_JOIN;
								break;
							case SqlTokenTypes.KEYWORD_RIGHT:
								joinType=OnJoinedSource.Type.RIGHT;
								state=State.EXPECTING_JOIN;
								break;
							case SqlTokenTypes.KEYWORD_AS:
								state=State.EXPECTING_ALIAS;
								break;
							case SqlTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL:
							case SqlTokenTypes.IDENTIFIER:
								source=new AliasedSource(source, token.getValue());
								state=State.COMPLETE;
								break;
							default:
								tokenizer.pushBack(token);
								state=State.COMPLETE;
								break;
						}
						break;
					case EXPECTING_JOIN:
						switch (token.getType())
						{
							case SqlTokenTypes.KEYWORD_JOIN:
								source2=parseSingleSource(tokenizer, parserContext);
								state=State.EXPECTING_ON_OR_USING;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					case EXPECTING_ON_OR_USING:
						switch (token.getType())
						{
							case SqlTokenTypes.KEYWORD_ON:
								Expression on=LogicalExpressionParser.getInstance().parse(tokenizer, parserContext);
								BooleanExpression booleanOnExpression=toBooleanExpression(on);
								source=new OnJoinedSource(source, source2, joinType, booleanOnExpression);
								state=State.COMPLETE;
								break;
							case SqlTokenTypes.KEYWORD_USING:
								Token token2=tokenizer.matchToken(SqlTokenTypes.IDENTIFIER);
								if (token2 == null)
								{
									throw new IncompleteExpressionException();
								}
								IdentifierExpression using=new IdentifierExpression(token2.getValue());
								source=new UsingJoinedSource(source, source2, joinType, using);
								state=State.COMPLETE;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					case EXPECTING_ALIAS:
						switch (token.getType())
						{
							case SqlTokenTypes.DOUBLE_QUOTED_TEXT_LITERAL:
							case SqlTokenTypes.IDENTIFIER:
								source=new AliasedSource(source, token.getValue());
								state=State.COMPLETE;
								break;
							default:
								tokenizer.pushBack(token);
								state=State.COMPLETE;
								break;
						}
						break;
					default:
						break;
				}
			}
		}
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		switch (state)
		{
			case EXPECTING_JOIN:
			case EXPECTING_ON_OR_USING:
				throw new IncompleteJoinException();
			case EXPECTING_ALIAS:
				throw new ParserException("Missing alias");
			default:
				break;
		}
		return source;
	}

	private BooleanExpression toBooleanExpression(Expression e)
		throws BooleanExpressionExpectedException
	{
		if (e instanceof BooleanExpression)
		{
			return (BooleanExpression)e;
		}
		else
		{
			throw new BooleanExpressionExpectedException(null);
		}
	}
}
