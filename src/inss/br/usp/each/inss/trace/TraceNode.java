package br.usp.each.inss.trace;

public interface TraceNode {
	
	Thread getThread();

	Object getObject();

	String getClassName();

	int getMethodId();
	
	long getInvokeId();

	int getNodeId();

}
