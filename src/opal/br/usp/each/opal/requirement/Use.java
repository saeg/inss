package br.usp.each.opal.requirement;

import java.io.Serializable;

public abstract class Use implements Serializable {

	private static final long serialVersionUID = -4156418834323589428L;

	public enum Type {
		P_USE, C_USE;
	}

	private Type type;

	public Use(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public int getUseNode() {
		if (this instanceof PUse) {
			return ((PUse) this).getDestNode();
		} else if (this instanceof CUse) {
			return ((CUse) this).getUseNode();
		}
		throw new RuntimeException("I don't know how get use node");
	}
	
	public PUse PUse() throws ClassCastException {
		return (PUse)this;
	}
	
	public CUse CUse() throws ClassCastException {
		return (CUse)this;
	}
}
