package br.usp.each.j2gxl.information;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;

/**
 * Generates AllUses informations omitting the call nodes
 * 
 * @author Felipe Albuquerque
 */
public class SimplifiedAllUsesInformation extends AllUsesInformation {

	/**
	 * Constructor
	 */
	public SimplifiedAllUsesInformation() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param dir the directory in which the GXL files will be written
	 */
	public SimplifiedAllUsesInformation(String dir) {
		super(dir);
	}
	
	@Override
	public void generateFile(JavaClass fromClass) 
			throws IOException, BadJavaClassFileException {
		
		super.generateFile(fromClass, true);
	}
	
	@Override
	public Information createInstancePointingToDir(String dir) {
		return new SimplifiedAllUsesInformation(dir);
	}

}
