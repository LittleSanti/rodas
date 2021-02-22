package com.samajackun.rodas.sql.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.samajackun.rodas.core.model.AliasedExpression;
import com.samajackun.rodas.core.model.AsteriskExpression;
import com.samajackun.rodas.core.model.IdentifierExpression;

public class AliasedExpressionMap
{
	private final List<AliasedExpression> list=new ArrayList<>();

	private final Map<String, AliasedExpression> map=new HashMap<>();

	public void add(AliasedExpression aliasedExpression)
		throws RepeatedAliasException
	{
		this.list.add(aliasedExpression);
		if (aliasedExpression.getAlias() != null)
		{
			if (this.map.containsKey(aliasedExpression.getAlias()))
			{
				throw new RepeatedAliasException(aliasedExpression.getAlias());
			}
			else
			{
				this.map.put(aliasedExpression.getAlias(), aliasedExpression);
			}
		}
	}

	public void completeAlias(Function<Integer, String> aliasBuilder)
	{
		this.list.stream().filter(x -> x.getAlias() == null).filter(x -> !(x.getExpression() instanceof AsteriskExpression)).forEach(x -> x.setAlias(buildUniqueAlias(x, aliasBuilder)));
	}

	private int autoAliasSequential;

	private String buildUniqueAlias(AliasedExpression src, Function<Integer, String> aliasBuilder)
	{
		String autoAlias=(src.getExpression() instanceof IdentifierExpression)
			? ((IdentifierExpression)src.getExpression()).getIdentifier()
			: aliasBuilder.apply(this.autoAliasSequential++);

		while (this.map.containsKey(autoAlias))
		{
			autoAlias=aliasBuilder.apply(this.autoAliasSequential);
			this.autoAliasSequential++;
		}
		return autoAlias;
	}

	public List<AliasedExpression> getList()
	{
		return this.list;
	}

}
