package br.usp.each.inss.executor;

import java.io.IOException;

public interface ExecutorManager {
	
	void execute(Thread thread, Object object, String clazz, int methodId, long invokeId, int nodeId);

	void exportRequirements(String outputFile) throws IOException;

}
