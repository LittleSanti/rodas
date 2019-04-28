package com.samajackun.rodas.sql.tokenizer;

public class TokenizerSettings
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

	public TokenizerSettings(WhitespaceBehaviour whitespaceBehaviour, CommentsBehaviour commentsBehaviour, UnexpectedSymbolBehaviour unexpectedSymbolBehaviour)
	{
		super();
		this.whitespaceBehaviour=whitespaceBehaviour;
		this.commentsBehaviour=commentsBehaviour;
		this.unexpectedSymbolBehaviour=unexpectedSymbolBehaviour;
	}

	public TokenizerSettings()
	{
		this(WhitespaceBehaviour.IGNORE, CommentsBehaviour.IGNORE, UnexpectedSymbolBehaviour.THROW_EXCEPTION);
	}

	public WhitespaceBehaviour getWhitespaceBehaviour()
	{
		return this.whitespaceBehaviour;
	}

	public TokenizerSettings setWhitespaceBehaviour(WhitespaceBehaviour whitespaceBehaviour)
	{
		this.whitespaceBehaviour=whitespaceBehaviour;
		return this;
	}

	public CommentsBehaviour getCommentsBehaviour()
	{
		return this.commentsBehaviour;
	}

	public TokenizerSettings setCommentsBehaviour(CommentsBehaviour commentsBehaviour)
	{
		this.commentsBehaviour=commentsBehaviour;
		return this;
	}

	public UnexpectedSymbolBehaviour getUnexpectedSymbolBehaviour()
	{
		return this.unexpectedSymbolBehaviour;
	}

	public TokenizerSettings setUnexpectedSymbolBehaviour(UnexpectedSymbolBehaviour unexpectedSymbolBehaviour)
	{
		this.unexpectedSymbolBehaviour=unexpectedSymbolBehaviour;
		return this;
	}
}
