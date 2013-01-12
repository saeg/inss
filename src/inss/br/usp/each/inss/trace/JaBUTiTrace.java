package br.usp.each.inss.trace;

import java.io.InputStream;
import java.util.Scanner;

import br.usp.each.commons.InvalidLineException;
import br.usp.each.inss.trace.JaBUTiTrace.JaBUTiTraceLine;

public class JaBUTiTrace implements Trace<JaBUTiTraceLine> {
	
	private Scanner scanner;
	
	public JaBUTiTrace(InputStream is) {
		scanner = new Scanner(is);
	}

	@Override
	public boolean hasNext() {
		return scanner.hasNext(JaBUTiTraceLine.linePattern);
	}

	@Override
	public JaBUTiTraceLine next() {
		return  new JaBUTiTraceLine(scanner.next());
	}
	
	public boolean haveAnotherUnitTest() {
		if(scanner.hasNext("\\*{22}")) {
			scanner.next();
			return scanner.hasNext(JaBUTiTraceLine.linePattern);
		}
		return false;
	}
	
	public static class JaBUTiTraceLine {
		
		public static final int TEST_NAME = 0;
		public static final int THREAD_NAME = 1;
		public static final int OBJECT_ID = 2;
		public static final int CLASS_NAME = 3;
		public static final int METHOD_ID = 4;
		public static final int INVOKE_ID = 5;
		public static final int NODE_ID = 6;

		// FIXME: Create the correct patterns
		public static final String testNamePattern = ".*";
		public static final String threadNamePattern = "Thread\\[.*,\\d+,.*\\]";
		public static final String objectIdPattern = ".*";
		public static final String classNamePattern = ".*";
		public static final String numberPattern = "\\d+";
		public static final String separator = ":";

		public static final String linePattern = testNamePattern 
				+ separator + threadNamePattern
				+ separator + objectIdPattern
				+ separator + classNamePattern
				+ "(" + separator + numberPattern + "){3}";
		
		private String testName;
		private String threadName;
		private String objectId;
		private String className;
		private int methodId;
		private int invokeId;
		private int nodeId;

		public JaBUTiTraceLine(String line) {
			try {
				// Ignore first char and split
				String[] tokens = line.split(":");
				testName = tokens[TEST_NAME];
				threadName = tokens[THREAD_NAME];
				objectId = tokens[OBJECT_ID];
				className = tokens[CLASS_NAME];
				methodId = Integer.valueOf(tokens[METHOD_ID]);
				invokeId = Integer.valueOf(tokens[INVOKE_ID]);
				nodeId = Integer.valueOf(tokens[NODE_ID]);
			} catch (Exception e) {
				throw new InvalidLineException("Exception:" + e + "\nLine:" + line);
			}
		}
		
		public String getTestName() {
			return testName;
		}

		public String getThreadName() {
			return threadName;
		}

		public String getObjectId() {
			return objectId;
		}

		public String getClassName() {
			return className;
		}

		public int getMethodId() {
			return methodId;
		}

		public int getInvokeId() {
			return invokeId;
		}

		public int getNodeId() {
			return nodeId;
		}
		
		@Override
		public String toString() {
			StringBuilder b = new StringBuilder();
			b.append(testName);
			b.append(';');
			b.append(threadName);
			b.append(';');
			b.append(objectId);
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
}
