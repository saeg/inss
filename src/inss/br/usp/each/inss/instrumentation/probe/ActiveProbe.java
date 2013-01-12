package br.usp.each.inss.instrumentation.probe;

public class ActiveProbe implements Probe {

	private int nodeId;
	
	private int[] lastNode;
	
	public ActiveProbe(int nodeId, int[] lastNode) {
		this.nodeId = nodeId;
		this.lastNode = lastNode;
	}

	@Override
	public void execute() {
		lastNode[0] = nodeId;
	}
	
	public int getNodeId() {
		return nodeId;
	}
	
	public int[] getLastNode() {
		return lastNode;
	}

}
