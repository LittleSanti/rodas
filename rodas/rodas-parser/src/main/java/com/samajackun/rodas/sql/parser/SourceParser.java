package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.AliasedSource;
import com.samajackun.rodas.core.model.BooleanExpression;
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
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;
import com.samajackun.rodas.sql.tokenizer.SqlToken.Type;

public class SourceListParser extends AbstractParser<List<Source>>
{
	private static final SourceListParser INSTANCE=new SourceListParser();

	public static SourceListParser getInstance()
	{
		return SourceListParser.INSTANCE;
	}

	private SourceListParser()
	{
		super(DefaultParserFactory.getInstance());
	}

	private enum State {
		INITIAL, EXPECTING_COMMA, EXPECTING_SOURCE, COMPLETE, READ_SOURCE, EXPECTING_ALIAS, EXPECTING_ON_OR_USING, EXPECTING_JOIN, EXPECTING_AS_OR_ALIAS
	};

	@Override
	public List<Source> parse(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		List<Source> sources=new ArrayList<>();
		State state=State.INITIAL;
		do
		{
			switch (state)
			{
				case EXPECTING_SOURCE:
				case INITIAL:
					Source source=parseSource(tokenizer);
					if (source == null)
					{
						state=State.COMPLETE;
					}
					else
					{
						sources.add(source);
						state=State.EXPECTING_COMMA;
					}
					break;
				case EXPECTING_COMMA:
					SqlToken token=tokenizer.nextOptionalUsefulToken();
					if (token != null)
					{
						if (token.getType() == SqlToken.Type.COMMA)
						{
							state=State.EXPECTING_SOURCE;
						}
						else
						{
							tokenizer.pushBack(token);
							state=State.COMPLETE;
						}
					}
					break;
				default: // Ignorar.
			}
		}
		while (state != State.COMPLETE && tokenizer.tokenWasRead());
		switch (state)
		{
			case INITIAL:
				// Lista vacía.
				throw new ParserException("Empty source list not allowed");
			case EXPECTING_SOURCE:
				throw new ParserException("Expecting a source after comma");
			default: // Ignorar.
		}
		return sources;
	}

	public Source parseSource(SqlMatchingTokenizer tokenizer)
		throws ParserException,
		IOException
	{
		Source source=null;
		Source source2=null;
		State state=State.INITIAL;
		OnJoinedSource.Type joinType=null;
		do
		{
			SqlToken token=tokenizer.nextOptionalUsefulToken();
			if (token != null)
			{
				switch (state)
				{
					case INITIAL:
						switch (token.getType())
						{
							case IDENTIFIER:
							case DOUBLE_QUOTED_TEXT_LITERAL:
								source=new TableSource(token.getValue());
								state=State.READ_SOURCE;
								break;
							case PARENTHESIS_START:
								Source source1=SelectSentenceParser.getInstance().parse(tokenizer);
								tokenizer.matchToken(Type.PARENTHESIS_END);
								source=new ParehentesizedSource(source1);
								state=State.READ_SOURCE;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					case READ_SOURCE:
						switch (token.getType())
						{
							case KEYWORD_INNER:
								joinType=OnJoinedSource.Type.INNER;
								state=State.EXPECTING_JOIN;
								break;
							case KEYWORD_OUTER:
								joinType=OnJoinedSource.Type.OUTER;
								state=State.EXPECTING_JOIN;
								break;
							case KEYWORD_LEFT:
								joinType=OnJoinedSource.Type.LEFT;
								state=State.EXPECTING_JOIN;
								break;
							case KEYWORD_RIGHT:
								joinType=OnJoinedSource.Type.RIGHT;
								state=State.EXPECTING_JOIN;
								break;
							case KEYWORD_AS:
								state=State.EXPECTING_ALIAS;
								break;
							case DOUBLE_QUOTED_TEXT_LITERAL:
								source=new AliasedSource(source, token.getValue());
								state=State.COMPLETE;
								break;
							case IDENTIFIER:
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
							case KEYWORD_JOIN:
								source2=parseSource(tokenizer);
								state=State.EXPECTING_ON_OR_USING;
								break;
							default:
								throw new UnexpectedTokenException(token);
						}
						break;
					case EXPECTING_ON_OR_USING:
						switch (token.getType())
						{
							case KEYWORD_ON:
								Expression on=LogicalExpressionParser.getInstance().parse(tokenizer);
								BooleanExpression booleanOnExpression=toBooleanExpression(on);
								source=new OnJoinedSource(source, source2, joinType, booleanOnExpression);
								state=State.COMPLETE;
								break;
							case KEYWORD_USING:
								SqlToken token2=tokenizer.matchToken(Type.IDENTIFIER);
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
							case DOUBLE_QUOTED_TEXT_LITERAL:
							case IDENTIFIER:
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
