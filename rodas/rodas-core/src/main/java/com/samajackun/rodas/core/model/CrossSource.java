package com.samajackun.rodas.core.model;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.execution.Cursor;

public class CrossSource implements Source
{
	private final List<Source> sources;

	public CrossSource(List<Source> sources)
	{
		super();
		this.sources=sources;
	}

	@Override
	public String toCode()
	{
		StringBuilder stb=new StringBuilder(4096);
		for (Source source : this.sources)
		{
			if (stb.length() > 0)
			{
				stb.append(',');
			}
			stb.append(source.toCode());
		}
		return stb.toString();
	}

	@Override
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		return engine.execute(this, context);
	}

	public List<Source> getSources()
	{
		return this.sources;
	}
}
