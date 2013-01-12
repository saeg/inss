package br.usp.each.opal.requirement;

public class CUse extends Use {

	private static final long serialVersionUID = -1151038626171779549L;
	
	/**
	 * Node number that uses occurs
	 */
	private int useNode;

	public CUse(int useNode) {
		super(Type.C_USE);
		this.useNode = useNode;
	}

	@Override
	public int getUseNode() {
		return useNode;
	}

	@Override
	public String toString() {
		return String.valueOf(useNode);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CUse) {
			CUse other = CUse.class.cast(obj);
			return other.useNode == this.useNode;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return useNode + 31;
	}
}
