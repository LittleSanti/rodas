package com.samajackun.rodas.sql.parser;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.Sentence;
import com.samajackun.rodas.core.model.WithDeclaration;
import com.samajackun.rodas.sql.parser.tokenizer.ParserTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.PushBackTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken.Type;
import com.samajackun.rodas.sql.parser.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.parser.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.parser.tokenizer.UnexpectedTokenException;

public class ScriptParser
{
	private final ParserTokenizer tokenizer;

	private Sentence fetched;

	public ScriptParser(CharSequence src) throws ParserException
	{
		this.tokenizer=new ParserTokenizer(new PushBackTokenizer<SqlToken>(new SqlTokenizer(src)));
		this.fetched=fetch();
	}

	public boolean hasMoreSentences()
	{
		return this.fetched != null;
	}

	public Sentence nextSentence()
		throws ParserException
	{
		if (this.fetched == null)
		{
			throw new ParserException("End of stream");
		}
		Sentence oldFetched=this.fetched;
		this.fetched=fetch();
		return oldFetched;
	}

	private enum State {
		INITIAL, COMPLETE, READING_SELECT_EXPRESSIONS
	};

	private Sentence fetch()
		throws ParserException
	{
		State state=State.INITIAL;
		Sentence sentence=null;
		SelectSentence selectSource;
		List<WithDeclaration> withDeclarations=new ArrayList<WithDeclaration>();
		while (state != State.COMPLETE && this.tokenizer.hasMoreTokens())
		{
			SqlToken token=this.tokenizer.nextToken();
			switch (state)
			{
				case INITIAL:
					switch (token.getType())
					{
						case COMMENT:
							// Ignorar
							break;
						case KEYWORD_SELECT:
							selectSource=new SelectSentence();
							do
							{
								// Recopilar opciones:
								token=nextToken();
								if (token.isKeyword())
								{
									selectSource.getOptions().add(token.getImage());
								}
								else
								{
									pushBack();
								}
							}
							while (token.isKeyword());
							state=State.READING_SELECT_EXPRESSIONS;
							break;
						case KEYWORD_WITH:
							do
							{
								SqlToken tokenAlias=matchToken(SqlToken.Type.IDENTIFIER);
								token=matchToken(SqlToken.Type.KEYWORD_AS);
								token=matchToken(SqlToken.Type.PARENTHESIS_START);
								SelectSentence selectSentence=SelectSentenceParser.getInstance().parse(this.tokenizer);
								WithDeclaration withDeclaration=new WithDeclaration(tokenAlias.getImage(), selectSentence);
								withDeclarations.add(withDeclaration);
								token=matchToken(SqlToken.Type.PARENTHESIS_END);
								token=nextToken();
							}
							while (token.getType() == SqlToken.Type.COMMA);
							pushBack();
							break;
						default:
							throw new UnexpectedTokenException(token);
					}
				case READING_SELECT_EXPRESSIONS:
					// TODO
					break;
				case COMPLETE:
					// TODO
					break;
				default:
					break;
			}
		}
		return sentence;
	}

	private void pushBack()
		throws TokenizerException
	{
		this.tokenizer.pushBack();
	}

	private SqlToken nextToken()
		throws TokenizerException
	{
		SqlToken token;
		do
		{
			token=this.tokenizer.nextToken();
		}
		while (token.getType() == Type.COMMENT);
		return token;
	}

	private SqlToken matchToken(Type type)
		throws ParserException
	{
		SqlToken token=nextToken();
		if (token.getType() != type)
		{
			throw new ParserException("Expected token " + type);
		}
		return token;
	}
}
