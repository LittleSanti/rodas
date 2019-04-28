package com.samajackun.rodas.core.eval.functions;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;

public class DefaultFunctionContext implements FunctionContext
{
	private final Function function;

	private Object result;

	private final List<Object> currentArguments=new ArrayList<>(2);

	public DefaultFunctionContext(Function function)
	{
		super();
		this.function=function;
	}

	@Override
	public Object getResult()
	{
		return this.result;
	}

	@Override
	public void addArgument(Context context, Object argumentValue)
		throws FunctionEvaluationException
	{
		this.currentArguments.set(0, argumentValue);
		this.currentArguments.set(1, this.result);
		this.result=this.function.call(context, this.currentArguments);
	}

}
