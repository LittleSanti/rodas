package com.samajackun.rodas.core.eval.evaluators;

import com.samajackun.rodas.core.eval.ArithmeticEvaluator;
import com.samajackun.rodas.core.eval.BaseEvaluator;
import com.samajackun.rodas.core.eval.CollectionsEvaluator;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.FunctionEvaluator;
import com.samajackun.rodas.core.eval.LogicalEvaluator;
import com.samajackun.rodas.core.eval.QueryEvaluator;
import com.samajackun.rodas.core.eval.RelationalEvaluator;
import com.samajackun.rodas.core.eval.RuntimeEvaluator;
import com.samajackun.rodas.core.eval.TextEvaluator;

public class DefaultEvaluatorFactory implements EvaluatorFactory
{
	private final DefaultLogicalEvaluator myLogicalEvaluator=new DefaultLogicalEvaluator(this);

	private final DefaultBaseEvaluator myBaseEvaluator=new DefaultBaseEvaluator(this);

	private final DefaultFunctionEvaluator myFunctionEvaluator=new DefaultFunctionEvaluator(this);

	private final DefaultRelationalEvaluator myRelationalEvaluator=new DefaultRelationalEvaluator(this);

	private final DefaultArithmeticEvaluator myArithmeticEvaluator=new DefaultArithmeticEvaluator(this);

	private final DefaultCollectionsEvaluator myCollectionsEvaluator=new DefaultCollectionsEvaluator(this);

	private final TextEvaluator myTextEvaluator=new DefaultTextEvaluator(this);

	private final RuntimeEvaluator myRuntimeEvaluator=new DefaultRuntimeEvaluator(this);

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

	@Override
	public RuntimeEvaluator getRuntimeEvaluator()
	{
		return this.myRuntimeEvaluator;
	}
}
