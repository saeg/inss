package br.usp.each.inss.trace;


public class ProbedNode implements TraceNode {
	
	private Thread thread;
	private Object object;
	private String className;
	private int methodId;
	private long invokeId;
	private int nodeId;
	
	public ProbedNode(Thread thread, Object object, String className, int methodId, long invokeId, int nodeId) {
		this.thread = thread;
		this.object = object;
		this.className = className;
		this.methodId = methodId;
		this.invokeId = invokeId;
		this.nodeId = nodeId;
	}	

	@Override
	public Thread getThread() {
		return thread;
	}

	@Override
	public Object getObject() {
		return object;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public int getMethodId() {
		return methodId;
	}

	@Override
	public long getInvokeId() {
		return invokeId;
	}

	@Override
	public int getNodeId() {
		return nodeId;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(thread.getName());
		b.append(';');
		if (object != null) {
			b.append(object.getClass().getName());
			b.append(System.identityHashCode(object));
		} else {
			b.append("null");
		}
		b.append(';');
		b.append(className);
		b.append(';');
		b.append(methodId);
		b.append(';');
		b.append(invokeId);
		b.append(';');
		b.append(nodeId);
		return b.toString();
	}

}
