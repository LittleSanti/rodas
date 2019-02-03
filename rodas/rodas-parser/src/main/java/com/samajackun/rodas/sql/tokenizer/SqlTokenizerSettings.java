package com.samajackun.rodas.sql.tokenizer;

public class SqlTokenizerSettings
{
	public enum WhitespaceBehaviour {
		IGNORE, PRODUCE_TOKENS, INCLUDE_IN_FOLLOWING_TOKEN
	};

	public enum CommentsBehaviour {
		IGNORE, PRODUCE_TOKENS, INCLUDE_IN_FOLLOWING_TOKEN
	};

	public enum UnexpectedSymbolBehaviour {
		THROW_EXCEPTION, END_PARSING
	};

	private WhitespaceBehaviour whitespaceBehaviour;

	private CommentsBehaviour commentsBehaviour;

	private UnexpectedSymbolBehaviour unexpectedSymbolBehaviour=UnexpectedSymbolBehaviour.THROW_EXCEPTION;

	public SqlTokenizerSettings(WhitespaceBehaviour whitespaceBehaviour, CommentsBehaviour commentsBehaviour, UnexpectedSymbolBehaviour unexpectedSymbolBehaviour)
	{
		super();
		this.whitespaceBehaviour=whitespaceBehaviour;
		this.commentsBehaviour=commentsBehaviour;
		this.unexpectedSymbolBehaviour=unexpectedSymbolBehaviour;
	}

	public SqlTokenizerSettings()
	{
		this(WhitespaceBehaviour.IGNORE, CommentsBehaviour.IGNORE, UnexpectedSymbolBehaviour.THROW_EXCEPTION);
	}

	public WhitespaceBehaviour getWhitespaceBehaviour()
	{
		return this.whitespaceBehaviour;
	}

	public SqlTokenizerSettings setWhitespaceBehaviour(WhitespaceBehaviour whitespaceBehaviour)
	{
		this.whitespaceBehaviour=whitespaceBehaviour;
		return this;
	}

	public CommentsBehaviour getCommentsBehaviour()
	{
		return this.commentsBehaviour;
	}

	public SqlTokenizerSettings setCommentsBehaviour(CommentsBehaviour commentsBehaviour)
	{
		this.commentsBehaviour=commentsBehaviour;
		return this;
	}

	public UnexpectedSymbolBehaviour getUnexpectedSymbolBehaviour()
	{
		return this.unexpectedSymbolBehaviour;
	}

	public SqlTokenizerSettings setUnexpectedSymbolBehaviour(UnexpectedSymbolBehaviour unexpectedSymbolBehaviour)
	{
		this.unexpectedSymbolBehaviour=unexpectedSymbolBehaviour;
		return this;
	}
}
