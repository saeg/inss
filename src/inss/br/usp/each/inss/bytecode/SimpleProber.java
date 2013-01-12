package br.usp.each.inss.bytecode;

import java.io.IOException;

import br.usp.each.commons.logger.Log;
import br.usp.each.inss.Properties;
import br.usp.each.inss.cache.DefUseCache;
import br.usp.each.inss.dfg.GXLFileDFGLoader;
import br.usp.each.inss.executor.ExecutorManager;
import br.usp.each.inss.executor.Simulator;

public class SimpleProber {

	private static final ExecutorManager manager;
	
	private static final Log logger = new Log(SimpleProber.class);
	
	private static final Properties p = Properties.getInstance();
	static {
		try {
			DefUseCache defUseCache = new DefUseCache(new GXLFileDFGLoader(p.getGXLDirectory()));
			
			manager = new Simulator(
					defUseCache,
					p.getInstrumenter(),
					p.getRequirementDetermination());
			
			Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		} catch (Exception e) {
			logger.error("Static init error", e);
			throw new RuntimeException(e);
		}
	}

	static private long nestlevel = Long.MIN_VALUE;

	public static long nest() {
		if(nestlevel == Long.MAX_VALUE)
			throw new RuntimeException("nest is too long.");
		synchronized (SimpleProber.class) {
			return ++nestlevel;
		}
	}

	public static void probe(Object o, String className, int methodId, long invokeId, int nodeId) {
		synchronized (SimpleProber.class) {
			manager.execute(Thread.currentThread(), o, className, methodId, invokeId, nodeId);
		}
	}
	
	private static class ShutdownHook extends Thread {
		
		@Override
		public void run() {
			logger.info("Invoking shutdown hook");
			final String output = System.getProperty("output.file");
			if(output != null) {
				try {
					manager.exportRequirements(output);
				} catch (IOException e) {
					logger.error("Error exporting requirements", e);
				}
			}
		}
		
	}

}
