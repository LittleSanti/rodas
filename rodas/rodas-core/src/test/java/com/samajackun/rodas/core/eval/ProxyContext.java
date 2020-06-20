package com.samajackun.rodas.core.eval;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.Provider;

public class ProxyContext implements Context {

	private Context src;
	
	private VariablesManager variablesManager;

	public ProxyContext(Context src, VariablesContext newVariablesContext) {
		super();
		this.src = src;
		variablesManager=new ProxyVariablesManager(src.getVariablesManager(), newVariablesContext);
	}

	@Override
	public Provider getProvider() {
		return src.getProvider();
	}

	@Override
	public VariablesManager getVariablesManager() {
		return variablesManager;
	}

	@Override
	public Runtime getRuntime() {
		return src.getRuntime();
	}

	@Override
	public Object evaluate(Expression expression, EvaluatorFactory evaluatorFactory) throws EvaluationException {
		// TODO
		return null;
	}

}
