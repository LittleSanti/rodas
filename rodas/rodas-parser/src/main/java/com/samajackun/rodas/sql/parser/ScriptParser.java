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
import com.samajackun.rodas.sql.tokenizer.SqlTokenTypes;
import com.samajackun.rodas.sql.tokenizer.SqlTokenizer;
import com.samajackun.rodas.sql.tokenizer.Token;

public class ScriptParser
{
	private final SqlMatchingTokenizer tokenizer;

	private final ParserContext parserContext;

	private Sentence fetched;

	public ScriptParser(PushBackSource source, ParserContext parserContext) throws ParserException, IOException
	{
		this.parserContext=parserContext;
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
			Token token=this.tokenizer.nextToken();
			if (token != null)
			{
				switch (state)
				{
					case INITIAL:
						switch (token.getType())
						{
							case SqlTokenTypes.COMMENT:
								// Ignorar
								break;
							case SqlTokenTypes.KEYWORD_SELECT:
								selectSource=new SelectSentence();
								do
								{
									// Recopilar opciones:
									token=nextToken();
									if (SqlTokenTypes.isKeyword(token.getType()))
									{
										selectSource.getOptions().add(token.getValue());
									}
									else
									{
										this.tokenizer.pushBack(token);
									}
								}
								while (SqlTokenTypes.isKeyword(token.getType()));
								state=State.READING_SELECT_EXPRESSIONS;
								break;
							case SqlTokenTypes.KEYWORD_WITH:
								do
								{
									Token tokenAlias=matchToken(SqlTokenTypes.IDENTIFIER);
									token=matchToken(SqlTokenTypes.KEYWORD_AS);
									token=matchToken(SqlTokenTypes.PARENTHESIS_START);
									SelectSentence selectSentence=SelectSentenceParser.getInstance().parse(this.tokenizer, this.parserContext);
									WithDeclaration withDeclaration=new WithDeclaration(tokenAlias.getValue(), selectSentence);
									withDeclarations.add(withDeclaration);
									token=matchToken(SqlTokenTypes.PARENTHESIS_END);
									token=nextToken();
								}
								while (token.getType().equals(SqlTokenTypes.COMMA));
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

	private Token nextToken()
		throws TokenizerException,
		IOException
	{
		Token token;
		do
		{
			token=this.tokenizer.nextToken();
		}
		while (!token.getType().equals(SqlTokenTypes.COMMENT));
		return token;
	}

	private Token matchToken(String type)
		throws ParserException,
		IOException
	{
		Token token=nextToken();
		if (!token.getType().equals(type))
		{
			throw new ParserException("Expected token " + type);
		}
		return token;
	}
}
