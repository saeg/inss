package br.usp.each.j2gxl.information;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;

/**
 * Generates DefUse informations omitting the call nodes
 * 
 * @author Felipe Albuquerque
 */
class SimplifiedDefUseInformation extends DefUseInformation {

	/**
	 * Constructor
	 */
	public SimplifiedDefUseInformation() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param dir the directory in which the GXL files will be written
	 */
	public SimplifiedDefUseInformation(String dir) {
		super(dir);
	}
	
	@Override
	public void generateFile(JavaClass fromClass) 
			throws IOException, BadJavaClassFileException {
		
		super.generateFile(fromClass, true);
	}
	
	@Override
	public Information createInstancePointingToDir(String dir) {
		return new SimplifiedDefUseInformation(dir);
	}
	
}
