package com.samajackun.rodas.sql.eval;

public interface EvaluatorFactory
{
	public LogicalEvaluator getLogicalEvaluator();

	public RelationalEvaluator getRelationalEvaluator();

	public ArithmeticEvaluator getArithmeticEvaluator();

	public FunctionEvaluator getFunctionEvaluator();

	public CollectionsEvaluator getCollectionsEvaluator();

	public BaseEvaluator getBaseEvaluator();

	public QueryEvaluator getQueryEvaluator();

	public TextEvaluator getTextEvaluator();
}
