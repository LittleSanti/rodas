package com.samajackun.rodas.core.model;

public class WithDeclaration implements Codeable
{
	private static final long serialVersionUID=-3789343636099225876L;

	private final String alias;

	private final SelectSentence selectSentence;

	public WithDeclaration(String alias, SelectSentence selectSentence)
	{
		super();
		this.alias=alias;
		this.selectSentence=selectSentence;
	}

	public String getAlias()
	{
		return this.alias;
	}

	public SelectSentence getSelectSentence()
	{
		return this.selectSentence;
	}

	@Override
	public String toCode()
	{
		return this.alias + " AS (" + this.selectSentence.toCode() + ")";
	}

}
