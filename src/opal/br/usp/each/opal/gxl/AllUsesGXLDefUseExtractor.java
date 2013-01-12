package br.usp.each.opal.gxl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;

import br.usp.each.commons.logger.Log;
import br.usp.each.commons.template.Template;
import br.usp.each.commons.template.Templater;
import br.usp.each.j2gxl.gxl.GXLAllUsesBuilder;
import br.usp.each.j2gxl.gxl.GXLWriter;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.requirement.CUse;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.DuaDetermination;
import br.usp.each.opal.requirement.PUse;
import br.usp.each.opal.requirement.Use.Type;

public class AllUsesGXLDefUseExtractor {
	
	private static final Log log = new Log(AllUsesGXLDefUseExtractor.class);
	private static final Options options;
	private static final HelpFormatter formatter;
	private static final Template defUseName;
	private static final AllUsesStat stats = new AllUsesStat();
	private static boolean computeStats = false;
	static {
		@SuppressWarnings("static-access")
		Option path = OptionBuilder.withArgName("path")
								   .withLongOpt("file")
								   .hasArg()
								   .withDescription("path where files to read are located " +
								   		"( may be a def-use GXL file or a directory whith def-use GXL files)")
								   .isRequired()
								   .create("f");
		@SuppressWarnings("static-access")
		Option dest = OptionBuilder.withArgName("dir")
								   .withLongOpt("dest")
		  						   .hasArg()
		  						   .withDescription("specifies the directory where the GXL files will be created")
		  						   .isRequired()
		  						   .create("d");
		Option debug = new Option("verbose", "print all log messages");
		Option statsOption = new Option("stats", "print stats messages");
		options = new Options();
		options.addOption(path);
		options.addOption(dest);
		options.addOption(debug);
		options.addOption(statsOption);
		formatter = new HelpFormatter();
		try {
			defUseName = new Templater().template("template.inss.defuse_name");
		} catch (IOException e) {
			log.error("Error getting template for def-use gxl files names");
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		long n = System.nanoTime();
		// create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			if (line.hasOption("verbose")) {
				log.setLevel(Level.DEBUG);
				log.debug("---VERBOSE MODE---");
			}
			if (line.hasOption("stats")) {
				log.info("---STATS ENABLED---");
				computeStats = true;
			}

			File file = new File(line.getOptionValue("file"));
			File dest = new File(line.getOptionValue("dest"));
			
			if(!dest.isDirectory()) {
				throw new ParseException("destination may be a valid directory.");
			}
			
			if (file.isDirectory()) {
				File[] files = file.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.getName().matches(defUseName.getRegex());
					}
				});

				if (files.length > 0)
					log.info("Found %d def-use GXL file(s). Computing requirements...", files.length);
				else
					log.info("None def-use GXL file found.");

				for (File defUseFile : files) {
					exportDuasForDefUseGXLFile(defUseFile, dest);
				}
			} else if (file.isFile()) {
				if(!file.getName().matches(defUseName.getRegex())) {
					log.error("%s does not match with GXL def-use file name pattern", file);
				} else {
					exportDuasForDefUseGXLFile(file, dest);
				}
			} else {
				throw new ParseException("file <path> may be a def-use GXL file, "
						+ "or a valid directory whith some def-use GXL files.");
			}
		} catch (ParseException exp) {
			// oops, something went wrong
			log.warn(exp.getMessage());
			formatter.printHelp("AllUsesExtractor", options);
		}

		if (computeStats)
			stats.print(System.out);

		float t = ((float)(System.nanoTime() - n))/600000000;
		log.info("Total time: %f", t);
		
	}

	private static void exportDuasForDefUseGXLFile(File defUseFile, File dest) {
		String fileName = defUseFile.getName();
		String methodName = defUseName.get("methodName", fileName).replace('[', '<').replace(']', '>');
		String className = defUseName.get("className", fileName).replace('[', '<').replace(']', '>');
		log.info("Extracting def-use association (dua) from %s", fileName);

		DFGraph graph;
		try {
			graph = new GXLDefUseReader(defUseFile, methodName).getDFGraph("anyClass", 0);
		} catch (Exception e) {
			log.error("Error reading gxl file %s", defUseFile);
			return;
		}

		Dua[] duas = new DuaDetermination().requirement(graph);
		log.debug("%s.%s has %d dua(s)", className, methodName, duas.length);
		if (computeStats)
			stats.add(className, methodName, duas);

		GXLAllUsesBuilder builder = new GXLAllUsesBuilder(methodName);
		for (int i = 0; i < duas.length; i++) {
			Dua dua = duas[i];
			
			if (dua.getUse().getType() == Type.P_USE) {
				PUse use = dua.getUse().PUse();
				builder.createPUseNode("dua_" + i, 
						graph.getVar(dua.getVariable()).getName(), 
						Integer.valueOf(dua.getDef()), 
						Integer.valueOf(use.getOriginNode()),
						Integer.valueOf(use.getDestNode()));
			} else if (dua.getUse().getType() == Type.C_USE) {
				CUse use = dua.getUse().CUse();
				builder.createCUseNode("dua_" + i,
						graph.getVar(dua.getVariable()).getName(), 
						Integer.valueOf(dua.getDef()), 
						Integer.valueOf(use.getUseNode()));
			}
		}
		
		try {
			new GXLWriter(builder.getGraph()).write(dest.getPath(), fileName.replace("defuse", "dua"));
		} catch (IOException e) {
			log.error("Error writing duas for gxl file %s", fileName);
			return;
		}
	}

	private static class AllUsesStat {

		private Map<String, Integer> classesDuas = new HashMap<String, Integer>();
		private LinkedList<Integer> methodsDuas = new LinkedList<Integer>();

		public void add(String className, String methodName, Dua[] duas) {
			Integer nDuas;
			nDuas = classesDuas.get(className);
			if (nDuas == null)
				nDuas = 0;
			nDuas += duas.length;
			classesDuas.put(className, nDuas);
			methodsDuas.add(duas.length);
		}

		public void print(PrintStream out) {
			out.println("\nStats:");
			out.println(String.format("Total of methods: %d", methodsDuas.size()));
			out.println(String.format("Total of classes: %d", classesDuas.size()));
			out.println(String.format("Total of dua: %d", numOfDuas()));
			out.println(String.format("mean of dua per method: %f", ((double) numOfDuas()) / methodsDuas.size()));
			out.println(String.format("mean of dua per class: %f", ((double) numOfDuas()) / classesDuas.size()));
			out.println(String.format("max of dua per method: %d", maxMethodDuas()));
			out.println(String.format("max of dua per class: %d", maxClassDuas()));
		}
		
		public int numOfDuas() {
			int total = 0;
			for (Integer nDuas : methodsDuas) {
				total += nDuas;
			}
			return total;
		}
		
		public int maxMethodDuas() {
			Collections.sort(methodsDuas);
			return methodsDuas.getLast();
		}
		
		public int maxClassDuas() {
			LinkedList<Integer> values = new LinkedList<Integer>(classesDuas.values());
			Collections.sort(values);
			return values.getLast();
		}

	}

}
