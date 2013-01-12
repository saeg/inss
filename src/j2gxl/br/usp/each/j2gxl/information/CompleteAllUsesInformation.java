package br.usp.each.j2gxl.information;

import java.io.IOException;

import org.apache.bcel.classfile.JavaClass;

/**
 * Generates complete AllUses informations
 * 
 * @author Felipe Albuquerque
 */
public class CompleteAllUsesInformation extends AllUsesInformation {

	/**
	 * Constructor
	 */
	public CompleteAllUsesInformation() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param dir the directory in which the GXL files will be written
	 */
	public CompleteAllUsesInformation(String dir) {
		super(dir);
	}
	
	@Override
	public void generateFile(JavaClass fromClass) 
			throws IOException, BadJavaClassFileException {
		
		super.generateFile(fromClass, false);
	}

	@Override
	public Information createInstancePointingToDir(String dir) {
		return new CompleteAllUsesInformation(dir);
	}
	
}
