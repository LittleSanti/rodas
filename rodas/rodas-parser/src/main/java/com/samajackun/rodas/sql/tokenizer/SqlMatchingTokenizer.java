package com.samajackun.rodas.sql.tokenizer;

public class SqlMatchingTokenizer extends AbstractMatchingTokenizer
{
	public SqlMatchingTokenizer(SqlTokenizer tokenizer)
	{
		super(tokenizer, new TokenEvaluator()
		{
			@Override
			public boolean isUseful(Token token)
			{
				return !token.getType().equals(SqlTokenTypes.COMMENT);
			}
		});
	}
}
