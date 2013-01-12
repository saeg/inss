package br.usp.each.opal.requirement;

public class Edge extends Requirement {
	
	private static final long serialVersionUID = 5407168248926420853L;

	private int from;
	
	private int to;
	
	public Edge(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	public int getFrom() {
		return from;
	}
	
	public int getTo() {
		return to;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('(');
		buffer.append(from);
		buffer.append(',');
		buffer.append(to);
		buffer.append(',');
		buffer.append(isCovered());
		buffer.append(')');
		return buffer.toString();
	}

}
