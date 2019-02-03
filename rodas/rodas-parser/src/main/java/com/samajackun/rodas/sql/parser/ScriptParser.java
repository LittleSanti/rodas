package com.samajackun.rodas.sql.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.Sentence;
import com.samajackun.rodas.core.model.WithDeclaration;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.tokenizer.SqlMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.SqlToken;
import com.samajackun.rodas.sql.tokenizer.SqlToken.Type;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;

public class ScriptParser
{
	private final SqlMatchingTokenizer tokenizer;

	private Sentence fetched;

	public ScriptParser(PushBackSource source) throws ParserException, IOException
	{
		this.tokenizer=new SqlMatchingTokenizer(new SqlTokenizer(source));
		this.fetched=fetch();
	}

	public boolean hasMoreSentences()
	{
		return this.fetched != null;
	}

	public Sentence nextSentence()
		throws ParserException,
		IOException
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
		throws ParserException,
		IOException
	{
		State state=State.INITIAL;
		Sentence sentence=null;
		SelectSentence selectSource;
		List<WithDeclaration> withDeclarations=new ArrayList<>();
		do
		{
			SqlToken token=this.tokenizer.nextToken();
			if (token != null)
			{
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
										selectSource.getOptions().add(token.getValue());
									}
									else
									{
										this.tokenizer.pushBack(token);
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
									WithDeclaration withDeclaration=new WithDeclaration(tokenAlias.getValue(), selectSentence);
									withDeclarations.add(withDeclaration);
									token=matchToken(SqlToken.Type.PARENTHESIS_END);
									token=nextToken();
								}
								while (token.getType() == SqlToken.Type.COMMA);
								this.tokenizer.pushBack(token);
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
		}
		while (state != State.COMPLETE && this.tokenizer.tokenWasRead());
		return sentence;
	}

	private SqlToken nextToken()
		throws TokenizerException,
		IOException
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
		throws ParserException,
		IOException
	{
		SqlToken token=nextToken();
		if (token.getType() != type)
		{
			throw new ParserException("Expected token " + type);
		}
		return token;
	}
}
