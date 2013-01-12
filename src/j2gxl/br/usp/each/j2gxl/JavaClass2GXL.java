package br.usp.each.j2gxl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;

import br.usp.each.commons.logger.Log;
import br.usp.each.j2gxl.information.BadJavaClassFileException;
import br.usp.each.j2gxl.information.Information;
import br.usp.each.j2gxl.parser.JavaClassParser;

/**
 * Extracts the informations from the Java files and generates the GXL files
 * 
 * @author Felipe Albuquerque
 */
public class JavaClass2GXL {
	
	public static final Log log = new Log(JavaClass2GXL.class); 

	private List<JavaClass> classFiles;
	private List<Information> informations;
	
	/**
	 * Constructor
	 * 
	 * @param fileName the file from which the informations will be extracted
	 * @throws IOException if an I/O error has occurred while getting the Java 
	 * classes from the file
	 */
	public JavaClass2GXL(String fileName) throws IOException {
		this.informations = new ArrayList<Information>();
		this.classFiles = new JavaClassParser(fileName).generateJavaClasses();

		log.info("Number of classes: " + this.classFiles.size());
	}
	
	/**
	 * Adds an information to be extracted from the Java class files and 
	 * specifies the directory where the GXL files will be written
	 * 
	 * @param information the information to be extracted
	 * @param dir the directory where the GXL files will be written
	 * @return this instance
	 */
	public JavaClass2GXL addInformation(Information information, String dir) {
		
		if (information == null) {
			throw new IllegalArgumentException(
					"The information specified must not be null");
		}
		
		File fileSpecified = new File(dir);
		if (!fileSpecified.isDirectory()) {
			throw new IllegalArgumentException(
					"The file specified must be a directory");
		}
		
		this.informations.add(
				information.createInstancePointingToDir(dir));

		return this;
	}
	
	/**
	 * Generate the files with the informations added
	 * 
	 * @throws IOException if an I/O error has occurred 
	 * @throws BadJavaClassFileException if some Java class file cannot 
	 * be read
	 */
	public void generateFiles() throws IOException, BadJavaClassFileException {
		
		if (this.informations.isEmpty()) {
			throw new IllegalStateException("At least one information must be added");
		}
		
		for (JavaClass javaClass : this.classFiles) {
			for (Information information : this.informations) {
				information.generateFile(javaClass);
			}
		}
		
	}
	
}
