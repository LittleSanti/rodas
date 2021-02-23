package com.samajackun.rodas.sql.parser;

public final class DefaultParserFactory implements ParserFactory
{
	private GenericExpressionParser expressionParser;

	private GenericArithmeticExpressionParser arithmeticExpressionParser;

	private GenericComparisonExpressionParser comparisonExpressionParser;

	private GenericExpressionListParser expressionListParser;

	private GenericExpressionCollectionParser expressionCollectionParser;

	private GenericLogicalExpressionParser logicalExpressionParser;

	private GenericRelationalExpressionParser relationalExpressionParser;

	private GenericSelectSentenceParser selectSentenceParser;

	private SourceParser sourceParser;

	private static final DefaultParserFactory INSTANCE=new DefaultParserFactory();

	public static DefaultParserFactory getInstance()
	{
		return INSTANCE;
	}

	private DefaultParserFactory()
	{
	}

	@Override
	public GenericExpressionParser getExpressionParser()
	{
		if (this.expressionParser == null)
		{
			synchronized (this)
			{
				if (this.expressionParser == null)
				{
					this.expressionParser=ExpressionParser.getInstance();
				}
			}
		}
		return this.expressionParser;
	}

	@Override
	public GenericArithmeticExpressionParser getArithmeticExpressionParser()
	{
		if (this.arithmeticExpressionParser == null)
		{
			synchronized (this)
			{
				if (this.arithmeticExpressionParser == null)
				{
					this.arithmeticExpressionParser=ArithmeticExpressionParser.getInstance();
				}
			}
		}
		return this.arithmeticExpressionParser;
	}

	@Override
	public GenericComparisonExpressionParser getComparisonExpressionParser()
	{
		if (this.comparisonExpressionParser == null)
		{
			synchronized (this)
			{
				if (this.comparisonExpressionParser == null)
				{
					this.comparisonExpressionParser=ComparisonExpressionParser.getInstance();
				}
			}
		}
		return this.comparisonExpressionParser;
	}

	@Override
	public GenericExpressionListParser getExpressionListParser()
	{
		if (this.expressionListParser == null)
		{
			synchronized (this)
			{
				if (this.expressionListParser == null)
				{
					this.expressionListParser=ExpressionListParser.getInstance();
				}
			}
		}
		return this.expressionListParser;
	}

	@Override
	public GenericExpressionCollectionParser getExpressionCollectionParser()
	{
		if (this.expressionCollectionParser == null)
		{
			synchronized (this)
			{
				if (this.expressionCollectionParser == null)
				{
					this.expressionCollectionParser=ExpressionCollectionParser.getInstance();
				}
			}
		}
		return this.expressionCollectionParser;
	}

	@Override
	public PartialParser getLogicalExpressionParser()
	{
		if (this.logicalExpressionParser == null)
		{
			synchronized (this)
			{
				if (this.logicalExpressionParser == null)
				{
					this.logicalExpressionParser=LogicalExpressionParser.getInstance();
				}
			}
		}
		return this.logicalExpressionParser;
	}

	@Override
	public PartialParser getRelationalExpressionParser()
	{
		if (this.relationalExpressionParser == null)
		{
			synchronized (this)
			{
				if (this.relationalExpressionParser == null)
				{
					this.relationalExpressionParser=RelationalExpressionParser.getInstance();
				}
			}
		}
		return this.relationalExpressionParser;
	}

	@Override
	public PartialParser getSelectSentenceParser()
	{
		if (this.selectSentenceParser == null)
		{
			synchronized (this)
			{
				if (this.selectSentenceParser == null)
				{
					this.selectSentenceParser=SelectSentenceParser.getInstance();
				}
			}
		}
		return this.selectSentenceParser;
	}

	@Override
	public SourceParser getSourceParser()
	{
		if (this.sourceParser == null)
		{
			synchronized (this)
			{
				if (this.sourceParser == null)
				{
					this.sourceParser=new SourceParser(this);
				}
			}
		}
		return this.sourceParser;

	}

}
