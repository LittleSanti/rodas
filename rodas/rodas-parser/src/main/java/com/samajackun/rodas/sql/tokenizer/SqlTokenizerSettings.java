package com.samajackun.rodas.sql.tokenizer;

public class SqlTokenizerSettings
{
	public enum WhitespaceBehaviour {
		IGNORE, PRODUCE_TOKENS, INCLUDE_IN_FOLLOWING_TOKEN
	};

	public enum CommentsBehaviour {
		IGNORE, PRODUCE_TOKENS, INCLUDE_IN_FOLLOWING_TOKEN
	};

	private WhitespaceBehaviour whitespaceBehaviour;

	private CommentsBehaviour commentsBehaviour;

	public SqlTokenizerSettings(WhitespaceBehaviour whitespaceBehaviour, CommentsBehaviour commentsBehaviour)
	{
		super();
		this.whitespaceBehaviour=whitespaceBehaviour;
		this.commentsBehaviour=commentsBehaviour;
	}

	public SqlTokenizerSettings()
	{
		this(WhitespaceBehaviour.IGNORE, CommentsBehaviour.IGNORE);
	}

	public WhitespaceBehaviour getWhitespaceBehaviour()
	{
		return this.whitespaceBehaviour;
	}

	public void setWhitespaceBehaviour(WhitespaceBehaviour whitespaceBehaviour)
	{
		this.whitespaceBehaviour=whitespaceBehaviour;
	}

	public CommentsBehaviour getCommentsBehaviour()
	{
		return this.commentsBehaviour;
	}

	public void setCommentsBehaviour(CommentsBehaviour commentsBehaviour)
	{
		this.commentsBehaviour=commentsBehaviour;
	}

}
