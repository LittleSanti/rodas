package com.samajackun.rodas.sql.parser;

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
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken.Type;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public class SourceListParser extends AbstractParser<List<Source>>
{
	private static final SourceListParser INSTANCE=new SourceListParser();

	public static SourceListParser getInstance()
	{
		return INSTANCE;
	}

	private SourceListParser()
	{
	}

	private enum State {
		INITIAL, EXPECTING_COMMA, EXPECTING_SOURCE, COMPLETE, READ_SOURCE, EXPECTING_ALIAS, EXPECTING_ON_OR_USING, EXPECTING_JOIN, EXPECTING_AS_OR_ALIAS
	};

	@Override
	public List<Source> parse(ParserTokenizer tokenizer)
		throws ParserException
	{
		List<Source> sources=new ArrayList<Source>();
		State state=State.INITIAL;
		while (state != State.COMPLETE && tokenizer.hasMoreTokens())
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
					SqlToken token=tokenizer.nextUsefulToken();
					if (token.getType() == SqlToken.Type.COMMA)
					{
						state=State.EXPECTING_SOURCE;
					}
					else
					{
						tokenizer.pushBack();
						state=State.COMPLETE;
					}
					break;
				default: // Ignorar.
			}
		}
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

	public Source parseSource(ParserTokenizer tokenizer)
		throws ParserException
	{
		Source source=null;
		Source source2=null;
		State state=State.INITIAL;
		OnJoinedSource.Type joinType=null;
		while (state != State.COMPLETE && tokenizer.hasMoreTokens())
		{
			SqlToken token=tokenizer.nextUsefulToken();
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case IDENTIFIER:
						case DOUBLE_QUOTED_TEXT_LITERAL:
							source=new TableSource(token.getImage());
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
						case IDENTIFIER:
							source=new AliasedSource(source, token.getImage());
							state=State.COMPLETE;
							break;
						default:
							tokenizer.pushBack();
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
							IdentifierExpression using=new IdentifierExpression(token2.getImage());
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
							source=new AliasedSource(source, token.getImage());
							state=State.COMPLETE;
							break;
						default:
							tokenizer.pushBack();
							state=State.COMPLETE;
							break;
					}
					break;
				default:
					break;
			}
		}
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
