package com.samajackun.rodas.sql.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.parsing.tokenizer.TokenizerException;
import com.samajackun.rodas.sql.tokenizer.SqlToken.Type;

public class MatchingSqlTokenizer
{
	private final SqlTokenizer tokenizer;

	private SqlToken lastToken;

	private boolean lastTokenIsNotNull=true;

	public MatchingSqlTokenizer(SqlTokenizer tokenizer)
	{
		this.tokenizer=tokenizer;
	}

	public SqlToken nextUsefulToken()
		throws TokenizerException,
		IOException
	{
		SqlToken found=null;
		SqlToken token;
		do
		{
			token=nextToken();
			if (token != null && token.getType() != Type.COMMENT)
			{
				found=token;
			}
		}
		while (token != null && found == null);
		return found;
	}

	public SqlToken matchToken(Type type)
		throws ParserException,
		IOException
	{
		SqlToken token=nextUsefulToken();
		if (token == null || token.getType() != type)
		{
			throw new UnexpectedTokenException(token, type);
		}
		return token;
	}

	public SqlToken nextToken()
		throws TokenizerException,
		IOException
	{
		this.lastToken=this.tokenizer.nextToken();
		this.lastTokenIsNotNull=(this.lastToken != null);
		return this.lastToken;
	}

	public void pushBack(SqlToken token)
		throws IOException
	{
		this.tokenizer.pushBackToken(token);
	}

	public boolean tokenWasRead()
	{
		return this.lastTokenIsNotNull;
	}
}
