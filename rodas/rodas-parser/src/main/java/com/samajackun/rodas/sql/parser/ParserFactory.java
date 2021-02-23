package com.samajackun.rodas.sql.parser;

public interface ParserFactory
{
	public PartialParser getExpressionParser();

	public PartialParser getArithmeticExpressionParser();

	public PartialParser getComparisonExpressionParser();

	public GenericExpressionListParser getExpressionListParser();

	public GenericExpressionCollectionParser getExpressionCollectionParser();

	public PartialParser getLogicalExpressionParser();

	public PartialParser getRelationalExpressionParser();

	public PartialParser getSelectSentenceParser();

	public SourceParser getSourceParser();
}
