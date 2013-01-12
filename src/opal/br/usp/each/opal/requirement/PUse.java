package br.usp.each.opal.requirement;

public class PUse extends Use {

	private static final long serialVersionUID = 1462765872498431013L;

	private int originNode;

	private int destNode;

	public PUse(int originNode, int destNode) {
		super(Type.P_USE);
		this.originNode = originNode;
		this.destNode = destNode;
	}

	public int getOriginNode() {
		return originNode;
	}

	public int getDestNode() {
		return destNode;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('(');
		buffer.append(originNode);
		buffer.append(',');
		buffer.append(destNode);
		buffer.append(')');
		return buffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PUse) {
			PUse other = PUse.class.cast(obj);
			return (other.originNode == this.originNode
					&& other.destNode == this.destNode);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + destNode;
		result = prime * result + originNode;
		return result;
	}

}
