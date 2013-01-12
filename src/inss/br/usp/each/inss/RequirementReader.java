package br.usp.each.inss;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.usp.each.commons.logger.Log;
import br.usp.each.inss.cache.Requirements;
import br.usp.each.opal.requirement.Requirement;
import java.io.PrintStream;

public class RequirementReader {

    private static final Log log = new Log(RequirementReader.class);
    private static final Options options;
    private static final HelpFormatter formatter;

    static {
        @SuppressWarnings("static-access")
        Option path = OptionBuilder.withArgName("file")
        						   .withLongOpt("read")
        						   .hasArg()
        						   .withDescription("requirement file to read")
        						   .isRequired()
        						   .create("r");

        @SuppressWarnings("static-access")
        Option csv = OptionBuilder.withArgName("file")
        						  .withLongOpt("csv")
        						  .hasArg()
        						  .withDescription("requirement file to print in csv format")
        						  .create("c");

        @SuppressWarnings("static-access")
        Option gxl = OptionBuilder.withArgName("file")
        						  .withLongOpt("gxl")
        						  .hasArg()
        						  .withDescription("requirement file to print in gxl format")
        						  .create("g");

        @SuppressWarnings("static-access")
        Option x = OptionBuilder.withArgName("file")
        						.withLongOpt("xml")
        						.hasArg()
        						.withDescription("requirement file to print in xml format")
        						.create("x");

        @SuppressWarnings("static-access")
        Option xj = OptionBuilder.withArgName("file").withLongOpt("xml-jabuti").hasArg().withDescription("requirement file to print in xml-jabuti format").create("xj");

        options = new Options();
        options.addOption(path);
        options.addOption(csv);
        options.addOption(gxl);
        options.addOption(x);
        options.addOption(xj);
        formatter = new HelpFormatter();
    }
    private static final ParseException INVALID_FILE_EXCEPTION = new ParseException("read <file> may be a valid file.");

    public static void main(String[] args) throws IOException {
        // create the parser
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine line = parser.parse(options, args);
            String[] filesIn = line.getOptionValues("read");
            RequirementWrapper wrapper = new RequirementWrapper();
            try {
                for (String fileName : filesIn) {
                    File file = new File(fileName);
                    if (file.isFile()) {
                        FileInputStream fileIn = new FileInputStream(file);
                        ObjectInputStream in = new ObjectInputStream(fileIn);
                        Requirements requirements = (Requirements) in.readObject();
                        RequirementWrapper.load(requirements, wrapper);
                    } else {
                        throw INVALID_FILE_EXCEPTION;
                    }
                }
                boolean defaultExport = true;
                if (line.hasOption("csv")) {
                    defaultExport = false;
                    byte[] contents = new RequirementExportCSV(wrapper).export();
                    OutputStream os = new FileOutputStream(new File(line.getOptionValue("csv")));
                    os.write(contents);
                    os.close();
                }
                if (line.hasOption("gxl")) {
                    defaultExport = false;
                    byte[] contents = new RequirementExportGXL().export();
                    OutputStream os = new FileOutputStream(new File(line.getOptionValue("gxl")));
                    os.write(contents);
                    os.close();
                }
                if (line.hasOption("xml")) {
                    defaultExport = false;
                    byte[] contents = new RequirementExportXML(wrapper).export();
                    OutputStream os = new FileOutputStream(new File(line.getOptionValue("xml")));
                    os.write(contents);
                    os.close();
                }
                if (line.hasOption("xml-jabuti")) {
                    defaultExport = false;
                    byte[] contents = new RequirementExportXMLJaBUTi(wrapper).export();
                    OutputStream os = new FileOutputStream(new File(line.getOptionValue("xml-jabuti")));
                    os.write(contents);
                    os.close();
                }
                if (defaultExport) {
                    printRequirements(wrapper, System.out);
                }
            } catch (ClassNotFoundException e) {
                throw INVALID_FILE_EXCEPTION;
            }
        } catch (ParseException exp) {
            // oops, something went wrong
            log.info("Failed!  Reason: " + exp.getMessage());
            formatter.printHelp("Requirement", options);
        }
    }

    private static void printRequirements(RequirementWrapper requirementWrapper, PrintStream out) {
        for (ClassRequirementWrapper classRequirementWrapper : requirementWrapper.getClasses()) {
            out.println(classRequirementWrapper.getClassName());
            for (MethodRequirementWrapper methodRequirementWrapper : classRequirementWrapper.getMethods()) {
                out.println(methodRequirementWrapper.getMethodId());
                for (Requirement r : methodRequirementWrapper.getAllRequirements()) {
                    out.println(r.toString());
                }
                out.println();
            }
        }
    }
    
}
