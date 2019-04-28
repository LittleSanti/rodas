package com.samajackun.rodas.core.eval.evaluators;

import java.util.HashMap;
import java.util.Map;

import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.FunctionEvaluator;
import com.samajackun.rodas.core.eval.functions.AbstractMappedFunctionEvaluator;
import com.samajackun.rodas.core.eval.functions.Function;
import com.samajackun.rodas.core.eval.functions.LenFunction;
import com.samajackun.rodas.core.eval.functions.MaxFunction;
import com.samajackun.rodas.core.eval.functions.MinFunction;

public class DefaultFunctionEvaluator extends AbstractMappedFunctionEvaluator implements FunctionEvaluator
{
	public DefaultFunctionEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	protected Map<String, Function> createMap(EvaluatorFactory evaluatorFactory)
	{
		Map<String, Function> map=new HashMap<String, Function>();
		map.put("min", new MinFunction(evaluatorFactory));
		map.put("max", new MaxFunction(evaluatorFactory));
		map.put("len", new LenFunction(evaluatorFactory));
		return map;
	}

	// @Override
	// public Datatype getDatatype(Context context, Expression functionExpression, List<Expression> arguments)
	// throws MetadataException
	// {
	// Datatype datatype;
	// switch (function)
	// {
	// case "len":
	// if (arguments.size() == 1)
	// {
	// if (arguments.get(0) == null)
	// {
	// datatype=Datatype.NULL;
	// }
	// else
	// {
	// datatype=Datatype.INTEGER_NUMBER;
	// }
	// }
	// else
	// {
	// throw new IllegalArgumentException();
	// }
	// break;
	// case "min":
	// case "max":
	// if (arguments.size() == 1)
	// {
	// if (arguments.get(0) == null)
	// {
	// datatype=Datatype.NULL;
	// }
	// else
	// {
	// datatype=super.getEvaluatorFactory().getBaseEvaluator().guessDatatype(context, arguments.get(0));
	// }
	// }
	// else
	// {
	// throw new IllegalArgumentException();
	// }
	// break;
	// default:
	// throw new IllegalArgumentException("Unsupported function " + function);
	// }
	// return datatype;
	// }

	// @Override
	// public Object evaluate(Context context, FunctionExpression expression)
	// throws EvaluationException
	// {
	// Object result;
	// switch (expression.getFunction())
	// {
	// case "min":
	// if (expression.getArguments().size() < 2)
	// {
	// throw new MinimumNumberOfArgumentsRequiredException("MIN function", 2);
	// }
	// else
	// {
	// // FIXME Falta evaluar los argumentos 3º-n.
	// Object value1=expression.getArguments().get(0).evaluate(context, getEvaluatorFactory());
	// Object value2=expression.getArguments().get(1).evaluate(context, getEvaluatorFactory());
	// if (value1 instanceof Number)
	// {
	// if (value2 instanceof Number)
	// {
	// double number1=((Number)value1).doubleValue();
	// double number2=((Number)value2).doubleValue();
	// result=(number1 < number2)
	// ? value1
	// : value2;
	// }
	// else
	// {
	// throw new EvaluationException("MIN parameter types do not match");
	// }
	// }
	// else if (value1 instanceof String)
	// {
	// if (value2 instanceof String)
	// {
	// String string1=((String)value1);
	// String string2=((String)value2);
	// result=(string1.compareTo(string2) < 0)
	// ? value1
	// : value2;
	// }
	// else
	// {
	// throw new EvaluationException("MIN parameter types do not match");
	// }
	// }
	// else
	// {
	// throw new EvaluationException("Unsupported parameter type " + value1.getClass().getName());
	// }
	// }
	// break;
	// case "max":
	// if (expression.getArguments().size() < 2)
	// {
	// throw new MinimumNumberOfArgumentsRequiredException("MIN function", 2);
	// }
	// else
	// {
	// Object value1=expression.getArguments().get(0).evaluate(context, getEvaluatorFactory());
	// Object value2=expression.getArguments().get(1).evaluate(context, getEvaluatorFactory());
	// if (value1 instanceof Number)
	// {
	// if (value2 instanceof Number)
	// {
	// double number1=((Number)value1).doubleValue();
	// double number2=((Number)value2).doubleValue();
	// result=(number1 > number2)
	// ? value1
	// : value2;
	// }
	// else
	// {
	// throw new EvaluationException("MAX parameter types do not match");
	// }
	// }
	// else if (value1 instanceof String)
	// {
	// if (value2 instanceof String)
	// {
	// String string1=((String)value1);
	// String string2=((String)value2);
	// result=(string1.compareTo(string2) > 0)
	// ? value1
	// : value2;
	// }
	// else
	// {
	// throw new EvaluationException("MIN parameter types do not match");
	// }
	// }
	// else
	// {
	// throw new EvaluationException("Unsupported parameter type " + value1.getClass().getName());
	// }
	// }
	// break;
	// case "len":
	// if (expression.getArguments().size() < 1)
	// {
	// throw new MinimumNumberOfArgumentsRequiredException("LEN function", 1);
	// }
	// else
	// {
	// Object value=expression.getArguments().get(0).evaluate(context, getEvaluatorFactory());
	// if (value == null)
	// {
	// result=null;
	// }
	// else if (value instanceof String)
	// {
	// String string=(String)value;
	// result=string.length();
	// }
	// else
	// {
	// throw new EvaluationException("LEN parameter type must be a STRING");
	// }
	// }
	// break;
	// default:
	// throw new EvaluationException("Unknown function '" + expression.getFunction() + "'");
	// }
	// return result;
	//
	// }
}
