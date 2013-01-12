package br.usp.each.opal.requirement;

public class Node extends Requirement {
	
	private static final long serialVersionUID = 4961821701230599996L;

	private int node;
	
	public Node(int node) {
		this.node = node;
	}
	
	public int getNode() {
		return node;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('(');
		buffer.append(node);
		buffer.append(',');
		buffer.append(isCovered());
		buffer.append(')');
		return buffer.toString();
	}

}
