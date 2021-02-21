package com.samajackun.rodas.core.eval;

public class ProxyVariablesManager implements VariablesManager {
	
	private VariablesManager src;
	private VariablesContext variablesContext;
	public ProxyVariablesManager(VariablesManager src, VariablesContext variablesContext) {
		super();
		this.src = src;
		this.variablesContext = variablesContext;
	}
	public Object getGlobalVariable(Name name) throws VariableNotFoundException {
		return src.getGlobalVariable(name);
	}
	public void setGlobalVariable(Name name, Object value) {
		src.setGlobalVariable(name, value);
	}
	public void removeGlobalVariable(Name name) {
		src.removeGlobalVariable(name);
	}
	public Object getLocalVariable(Name name) throws VariableNotFoundException {
		return src.getLocalVariable(name);
	}
	public void setLocalVariable(Name name, Object value) {
		src.setLocalVariable(name, value);
	}
	public void removeLocalVariable(Name name) {
		src.removeLocalVariable(name);
	}
	public Object getNearestVariable(Name name) throws VariableNotFoundException {
		return src.getNearestVariable(name);
	}
	public void setNearestVariable(Name name, Object value) {
		src.setNearestVariable(name, value);
	}
	public void pushLocalContext(VariablesContext newContext) {
		// TODO
		throw new UnsupportedOperationException();
	}
	public VariablesContext peekLocalContext() {
		return this.variablesContext;
	}
	public VariablesContext popLocalContext() {
		// TODO
		throw new UnsupportedOperationException();
	}
	public VariablesContext getVariablesContext(Name name) throws VariableNotFoundException {
		// TODO
		throw new UnsupportedOperationException();
		//return src.getVariablesContext(name);
	}
	public VariablesContext getLocalVariablesContext() {
		// TODO
		throw new UnsupportedOperationException();
		//return src.getLocalVariablesContext();
	}


}
