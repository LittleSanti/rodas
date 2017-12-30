package com.samajackun.rodas.sql.parser.tokenizer;

class MyTokenizer extends AbstractTokenizer<String, MySettings>
{

	protected MyTokenizer(CharSequence src, MySettings settings) throws TokenizerException
	{
		super(src, settings);
	}

	@Override
	protected String fetch(com.samajackun.rodas.sql.parser.tokenizer.AbstractTokenizer<String, MySettings>.Source source)
		throws TokenizerException
	{
		String s=source.getCurrentIndex() < source.getCharSequence().length()
			? String.valueOf(source.getCharSequence().charAt(source.getCurrentIndex()))
			: null;
		source.incCurrentIndex();
		return s;
	}

}