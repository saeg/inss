package br.usp.each.j2gxl.information;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;

/**
 * Generates complete DefUse informations
 * 
 * @author Felipe Albuquerque
 */
class CompleteDefUseInformation extends DefUseInformation {
	
	/**
	 * Constructor
	 */
	public CompleteDefUseInformation() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param dir the directory in which the GXL files will be written
	 */
	public CompleteDefUseInformation(String dir) {
		super(dir);
	}
	
	@Override
	public void generateFile(JavaClass fromClass) 
			throws IOException, BadJavaClassFileException {
		
		super.generateFile(fromClass, false);
	}

	@Override
	public Information createInstancePointingToDir(String dir) {
		return new CompleteDefUseInformation(dir);
	}
	
}

