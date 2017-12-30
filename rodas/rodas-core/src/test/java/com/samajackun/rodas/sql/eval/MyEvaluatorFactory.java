package com.samajackun.rodas.sql.eval;

public class MyEvaluatorFactory implements EvaluatorFactory
{
	private final MyLogicalEvaluator myLogicalEvaluator=new MyLogicalEvaluator(this);

	private final MyBaseEvaluator myBaseEvaluator=new MyBaseEvaluator(this);

	private final MyFunctionEvaluator myFunctionEvaluator=new MyFunctionEvaluator(this);

	private final MyRelationalEvaluator myRelationalEvaluator=new MyRelationalEvaluator(this);

	private final MyArithmeticEvaluator myArithmeticEvaluator=new MyArithmeticEvaluator(this);

	private final MyCollectionsEvaluator myCollectionsEvaluator=new MyCollectionsEvaluator(this);

	private final TextEvaluator myTextEvaluator=new MyTextEvaluator(this);

	@Override
	public LogicalEvaluator getLogicalEvaluator()
	{
		return this.myLogicalEvaluator;
	}

	@Override
	public RelationalEvaluator getRelationalEvaluator()
	{
		return this.myRelationalEvaluator;
	}

	@Override
	public ArithmeticEvaluator getArithmeticEvaluator()
	{
		return this.myArithmeticEvaluator;
	}

	@Override
	public FunctionEvaluator getFunctionEvaluator()
	{
		return this.myFunctionEvaluator;
	}

	@Override
	public CollectionsEvaluator getCollectionsEvaluator()
	{
		return this.myCollectionsEvaluator;
	}

	@Override
	public BaseEvaluator getBaseEvaluator()
	{
		return this.myBaseEvaluator;
	}

	@Override
	public QueryEvaluator getQueryEvaluator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextEvaluator getTextEvaluator()
	{
		return this.myTextEvaluator;
	}

}
