package com.samajackun.rodas.sql.parser.tokenizer;

import com.samajackun.rodas.sql.parser.ParserException;
import com.samajackun.rodas.sql.parser.tokenizer.SqlToken.Type;

public class ParserTokenizer
{
	private final PushBackTokenizer<SqlToken> tokenizer;

	public ParserTokenizer(Tokenizer<SqlToken> tokenizer)
	{
		this.tokenizer=new PushBackTokenizer<SqlToken>(tokenizer);
	}

	public SqlToken nextUsefulToken()
		throws TokenizerException
	{
		SqlToken token=null;
		while (this.tokenizer.hasMoreTokens() && token == null)
		{
			token=this.tokenizer.nextToken();
			if (token.getType() == Type.COMMENT)
			{
				token=null;
			}
		}
		return token;
	}

	public SqlToken matchToken(Type type)
		throws ParserException
	{
		SqlToken token=nextUsefulToken();
		if (token == null || token.getType() != type)
		{
			throw new UnexpectedTokenException(token, type);
		}
		return token;
	}

	public boolean hasMoreTokens()
	{
		return this.tokenizer.hasMoreTokens();
	}

	public SqlToken nextToken()
		throws TokenizerException
	{
		return this.tokenizer.nextToken();
	}

	public void pushBack()
	{
		this.tokenizer.pushBack();
	}
}
