package com.samajackun.rodas.core.model;

import java.util.HashMap;
import java.util.Map;

public class AccumulatorContext
{
	// TODO Esta es sólo una primera versión de la clase.
	private final Map<String, AccumulativeExpression> map=new HashMap<>();

	public Map<String, AccumulativeExpression> getMap()
	{
		return this.map;
	}
}
