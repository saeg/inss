package br.usp.each.j2gxl.command;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.usp.each.commons.logger.Log;
import br.usp.each.j2gxl.JavaClass2GXL;
import br.usp.each.j2gxl.information.Information;

/**
 * Receives command line arguments to generate AllUses informations 
 * 
 * @author Felipe Albuquerque
 */
public class AllUses {
	
	private static final Log log = new Log(AllUses.class);
	private static final Options options;
	private static final HelpFormatter formatter;
	static {
		@SuppressWarnings("static-access")
		Option path = OptionBuilder.withArgName("path")
								   .withLongOpt("file")
								   .hasArg()
								   .withDescription("path where files to read are located " +
								   		"( may be a .class file, .jar file, .zip file or a directory whith classes)")
								   .isRequired()
								   .create("f");
		@SuppressWarnings("static-access")
		Option dest = OptionBuilder.withArgName("dir")
								   .withLongOpt("dest")
		  						   .hasArg()
		  						   .withDescription("specifies the directory where the GXL files will be created")
		  						   .isRequired()
		  						   .create("d");
		@SuppressWarnings("static-access")
		Option simple = OptionBuilder.withLongOpt("simple")
		  						   	 .withDescription("creates graphs without call nodes")
		  						   	 .create("s");
		@SuppressWarnings("static-access")
		Option complete = OptionBuilder.withLongOpt("complete")
		  						   	   .withDescription("creates graphs with call nodes")
		  						   	   .create("c");
		options = new Options();
		options.addOption(path);
		options.addOption(dest);
		options.addOption(simple);
		options.addOption(complete);
		formatter = new HelpFormatter();
	}

	/**
	 * Main method. Receives the arguments from the command line, extracts 
	 * AllUses informations from a file and generates GXL files 
	 *  
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		long n = System.nanoTime();
	
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine line = parser.parse(options, args);
					
			if (line.hasOption("simple") && line.hasOption("complete")) {
				throw new ParseException("Duplicate graph type definition");
			}
			
			Information allUsesType = Information.SIMPLIFIED_ALL_USES;
			
			if (line.hasOption("complete")) {
				allUsesType = Information.COMPLETE_ALL_USES;
			}
			
			String dest = line.getOptionValue("dest");
			String file = line.getOptionValue("file");
			
			try {
				new JavaClass2GXL(file)
						.addInformation(allUsesType, dest)
						.generateFiles();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
		} catch (ParseException exp) {
			log.warn(exp.getMessage());
			formatter.printHelp("AllUses", options);
		}
		
		log.info("Total time: %f", ((float)(System.nanoTime() - n)) / 600000000);
	}
	
}
