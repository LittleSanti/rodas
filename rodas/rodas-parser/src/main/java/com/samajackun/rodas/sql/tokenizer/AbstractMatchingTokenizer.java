package com.samajackun.rodas.sql.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.tokenizer.EndOfStreamException;
import com.samajackun.rodas.parsing.tokenizer.Tokenizer;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;

public abstract class AbstractMatchingTokenizer
{
	private final Tokenizer tokenizer;

	private Token lastToken;

	private boolean lastTokenIsNotNull=true;

	private final TokenEvaluator tokenEvaluator;

	public AbstractMatchingTokenizer(Tokenizer tokenizer, TokenEvaluator tokenEvaluator)
	{
		this.tokenizer=tokenizer;
		this.tokenEvaluator=tokenEvaluator;
	}

	public Token nextOptionalUsefulToken()
		throws TokenizerException,
		IOException
	{
		Token found=null;
		Token token;
		do
		{
			token=nextOptionalToken();
			if (token != null && isUseful(token))
			{
				found=token;
			}
		}
		while (token != null && found == null);
		return found;
	}

	protected boolean isUseful(Token token)
	{
		return this.tokenEvaluator.isUseful(token);
	}

	public Token nextToken()
		throws TokenizerException,
		IOException
	{
		Token token=nextOptionalToken();
		// if (token == null)
		// {
		// throw new EndOfStreamException(this.tokenizer.getSource());
		// }
		return token;
	}

	public Token nextOptionalToken()
		throws TokenizerException,
		IOException
	{
		this.lastToken=this.tokenizer.nextToken();
		this.lastTokenIsNotNull=(this.lastToken != null);
		return this.lastToken;
	}

	public Token nextUsefulToken()
		throws TokenizerException,
		IOException
	{
		Token token=nextOptionalUsefulToken();
		if (token == null)
		{
			throw new EndOfStreamException(this.tokenizer.getSource());
		}
		return token;
	}

	public void pushBack(Token token)
		throws IOException
	{
		this.tokenizer.pushBackToken(token);
	}

	public boolean tokenWasRead()
	{
		return this.lastTokenIsNotNull;
	}

	public PushBackSource getSource()
	{
		return this.tokenizer.getSource();
	}

	public Token matchToken(String type)
		throws ParserException,
		IOException
	{
		Token token=nextOptionalUsefulToken();
		if (token == null || !token.getType().equals(type))
		{
			throw new UnexpectedTokenException(token, type);
		}
		return token;
	}

}
