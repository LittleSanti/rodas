package com.samajackun.rodas.sql.tokenizer;

import java.io.IOException;

import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.parser.UnexpectedTokenException;
import com.samajackun.rodas.sql.tokenizer.SqlToken.Type;

public class SqlMatchingTokenizer extends AbstractMatchingTokenizer<SqlToken>
{
	public SqlMatchingTokenizer(SqlTokenizer tokenizer)
	{
		super(tokenizer);
	}

	public SqlToken matchToken(Type type)
		throws ParserException,
		IOException
	{
		SqlToken token=nextOptionalUsefulToken();
		if (token == null || token.getType() != type)
		{
			throw new UnexpectedTokenException(token, type);
		}
		return token;
	}

	@Override
	protected boolean isUseful(SqlToken token)
	{
		return token.getType() != Type.COMMENT;
	}
}
