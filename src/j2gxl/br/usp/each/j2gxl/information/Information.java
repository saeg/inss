package br.usp.each.j2gxl.information;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;

/**
 * Represents an information that can be extracted from a class and exported to 
 * a GXL file.
 * 
 * @author Felipe Albuquerque
 */
public interface Information {
	
	/**
	 * Generates DefUse informations omitting call nodes
	 */
	Information SIMPLIFIED_DEF_USE = new SimplifiedDefUseInformation();
	
	/**
	 * Generates DefUse informations with call nodes
	 */
	Information COMPLETE_DEF_USE = new CompleteDefUseInformation();
	
	/**
	 * Generates AllUses informations omitting call nodes
	 */
	Information SIMPLIFIED_ALL_USES = new SimplifiedAllUsesInformation();
	
	/**
	 * Generates AllUses informations with call nodes
	 */
	Information COMPLETE_ALL_USES = new CompleteAllUsesInformation();
	
	/**
	 * Extension of a GXL file
	 */
	public static final String GXL_FILE_EXTENSION = "gxl";
	
	/**
	 * Generates the GXL file
	 * 
	 * @param fromClass the class from which the information will be extracted
	 * @throws IOException when GXL the file cannot be written
	 * @throws BadJavaClassFileException then the class that is being examined 
	 * has a problem
	 */
	void generateFile(JavaClass fromClass) 
			throws IOException, BadJavaClassFileException;
	
	/**
	 * Creates an instance pointing to the directory specified
	 * 
	 * @param dir the directory
	 * @return the instance pointing to the directory
	 */
	Information createInstancePointingToDir(String dir);
}
