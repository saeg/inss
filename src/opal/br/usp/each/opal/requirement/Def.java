package br.usp.each.opal.requirement;

public class Def {

	/**
	 * # of definition node
	 */
	private int def;

	/**
	 * # of variable
	 */
	private int variable;
	
	public Def(int def, int variable) {
		this.def = def;
		this.variable = variable;
	}

	public int getDef() {
		return def;
	}

	public int getVariable() {
		return variable;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Def) {
			Def other = Def.class.cast(obj);
			return (other.def == this.def
					&& other.variable == this.variable);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + def;
		result = prime * result + variable;
		return result;
	}
	
}
