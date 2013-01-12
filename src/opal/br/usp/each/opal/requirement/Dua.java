package br.usp.each.opal.requirement;


public class Dua extends Requirement {

	private static final long serialVersionUID = -799794017000522210L;

	/**
	 * Id of Dua
	 */
	private int id;

	/**
	 * # of definition node
	 */
	private int def;

	/**
	 * node/arc Use
	 */
	private Use use;

	/**
	 * # of variable
	 */
	private int variable;
	
	public Dua(int id, int def, Use use, int variable) {
		this.id = id;
		this.def = def;
		this.use = use;
		this.variable = variable;
	}

	public int getId() {
		return id;
	}

	public int getDef() {
		return def;
	}

	public Use getUse() {
		return use;
	}

	public int getVariable() {
		return variable;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		buffer.append(def);
		buffer.append(',');
		buffer.append(use);
		buffer.append(',');
		buffer.append(variable);
		buffer.append(',');
		buffer.append(isCovered());
		buffer.append(']');
		return buffer.toString();
	}
}
