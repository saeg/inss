package br.usp.each.inss;

import java.io.File;
import java.lang.reflect.Constructor;

import br.usp.each.commons.logger.Log;
import br.usp.each.inss.instrumentation.Instrumentator;
import br.usp.each.inss.instrumentation.traverse.DefaultProbeTraverseStrategy;
import br.usp.each.inss.instrumentation.traverse.ProbeTraverseStrategy;
import br.usp.each.opal.requirement.RequirementDetermination;

public final class Properties {
	
	private static final Log logger = new Log(Properties.class);

	private static final Properties instance = new Properties();
	
	public static Properties getInstance() {
		return instance;
	}
	
	private final File workingDirectory = new File(System.getProperty("user.dir"));

	private File gxlDirectory = workingDirectory;
	
	private Class<Instrumentator> instrumenterClass;

	private Class<RequirementDetermination> requirementDeterminationClass;
	
	// Private Constructor, Singleton pattern
	private Properties() {
		defineGXLDirectory();
		defineProbeTraverseStrategy();
		defineInstrumenter();
		defineRequirementDetermination();
	}

	private void defineGXLDirectory() {
		String dirname = System.getProperty("gxl.dir");
		
		// Check if is defined. If NOT use working directory
		if (dirname == null) {
			logger.info("gxl.dir not set, using working dir: '%s'", workingDirectory);
			return;
		}
		
		// Check if directory defined exists. If NOT then stop execution
		gxlDirectory = new File(dirname);
		if (!gxlDirectory.exists()) {
			logger.fatal("gxl.dir '%s' does not exists", gxlDirectory);
			System.exit(1);
		}

		logger.info("gxl.dir: '%s'", gxlDirectory);
	}

	private void defineProbeTraverseStrategy() {
		String clazz = System.getProperty("traverse.strategy");

		// Check if is defined. If NOT use default
		if (clazz == null) {
			logger.info("traverse.strategy not set, using %s",
					DefaultProbeTraverseStrategy.class);
			return;
		}
		
		// Try to defined probe traverse strategy. If ERROR then stop execution
		try {
			Program.setTraverseStrategy(
					(ProbeTraverseStrategy) Class.forName(clazz).newInstance());
		} catch (Exception e) {
			logger.fatal("Error defining traverse.strategy", e);
			System.exit(1);
		}

		logger.info("traverse.strategy: %s", clazz);
	}
	

	@SuppressWarnings("unchecked")
	private void defineInstrumenter() {
		String clazz = System.getProperty("instrumentation.strategy");
		
		// Check if is defined. If NOT then stop execution
		if(clazz == null) {
			logger.fatal("instrumentation.strategy not set");
			System.exit(1);
		} 
		
		// Try to define instrumentation strategy. If ERROR then stop execution
		try {
			instrumenterClass = (Class<Instrumentator>) Class.forName(clazz);
		} catch (Exception e) {
			logger.fatal("Error defining instrumentation.strategy", e);
			System.exit(1);
		}
		
		logger.info("instrumentation.strategy: %s", clazz);
	}
	
	@SuppressWarnings("unchecked")
	private void defineRequirementDetermination() {
		String clazz = System.getProperty("requirement.determination");
		
		// Check if is defined. If NOT then stop execution
		if(clazz == null) {
			logger.fatal("requirement.determination not set");
			System.exit(1);
		}
		
		// Try to define requirement determination. If ERROR then stop execution
		try {
			requirementDeterminationClass = (Class<RequirementDetermination>) Class.forName(clazz);
		} catch (Exception e) {
			logger.fatal("Error defining requirement.determination", e);
			System.exit(1);
		}
			
		logger.info("requirement.determination: %s", clazz);
	}
	
	public Instrumentator getInstrumenter() {
		Instrumentator instrumenter = null;
		try {
			instrumenter = instrumenterClass.newInstance();
		} catch (Exception e) {
			logger.fatal("Error getting Instrumenter instance", e);
			System.exit(1);
		}
		return instrumenter;
	}
	
	public RequirementDetermination getRequirementDetermination() {
		RequirementDetermination instance = null;
		try {
			Constructor<RequirementDetermination> constructor = requirementDeterminationClass.getConstructor(File.class);
			instance = constructor.newInstance(gxlDirectory);
		} catch (NoSuchMethodException e) {
			logger.debug("RequirementDetermination constructor not recive a File argument");
			try {
				logger.debug("Calling RequirementDetermination default constructor");
				instance = requirementDeterminationClass.newInstance();
			} catch (Exception ee) {
				logger.fatal("Error getting RequirementDetermination instance", e);
				System.exit(1);
			}
		} catch (Exception e) {
			logger.fatal("Error getting RequirementDetermination instance", e);
			System.exit(1);
		}
		return instance;
	}
	
	public File getGXLDirectory() {
		return gxlDirectory;
	}

}
