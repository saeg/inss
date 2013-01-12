package br.usp.each.j2gxl.information;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import br.jabuti.criteria.AllUses;
import br.jabuti.criteria.DefUse;
import br.jabuti.graph.CFG;
import br.jabuti.verifier.InvalidInstructionException;
import br.jabuti.verifier.InvalidStackArgument;
import br.usp.each.commons.logger.Log;
import br.usp.each.commons.string.StringUtils;
import br.usp.each.j2gxl.gxl.GXLAllUsesBuilder;
import br.usp.each.j2gxl.gxl.GXLWriter;

/**
 * Represents a AllUses information
 * 
 * @author Felipe Albuquerque
 */
abstract class AllUsesInformation implements Information {

	public static final Log log = new Log(AllUsesInformation.class);
	
	private String dir;
	
	/**
	 * Constructor
	 */
	public AllUsesInformation() {
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param dir the directory in which the GXL files will be written
	 */
	public AllUsesInformation(String dir) {
		this.dir = dir;
	}

	/**
	 * Generates the AllUses GXL file containing the informations retrieved 
	 * from the Java class
	 *  
	 * @param fromClass the class from which the information will be extracted
	 * @param isSimplified <code>true</code> if the DefUse graph is simplified 
	 * and <code>false</code>, otherwise
	 * @throws IOException when GXL the file cannot be written
	 * @throws BadJavaClassFileException then the class that is being examined 
	 * has a problem
	 */
	void generateFile(JavaClass fromClass, boolean isSimplified) 
			throws IOException, BadJavaClassFileException {
		
		int cfgOption = CFG.NONE;
		if (isSimplified) {
			cfgOption = CFG.NO_CALL_NODE;
		}
		
		if ((this.dir == null) || (this.dir.trim().length() == 0)) {
			throw new IllegalStateException(
					"The directory in which the GXL files will be generated must " 
							+ "be specified. Use the createInstanceToDir(String) " 
							+ "method to create an instance pointing to a dir.");
		}
		
		ClassGen clazz = new ClassGen(fromClass);
		ConstantPoolGen connstantPool = clazz.getConstantPool();
		Method[] methods = clazz.getMethods();
		String className = clazz.getClassName();
		
		for (int posMethod = 0; posMethod < methods.length; posMethod++) {
			Method method = methods[posMethod];
			MethodGen methodGen = new MethodGen(method, className, connstantPool);
			AllUses allUses = null;
			
			try {
				allUses = new AllUses(new CFG(methodGen, clazz, cfgOption), false);
			} catch (InvalidInstructionException e) {
				throw new BadJavaClassFileException(e);
			} catch (InvalidStackArgument e) {
				throw new BadJavaClassFileException(e);
			}
			
			GXLAllUsesBuilder builder = new GXLAllUsesBuilder(method.getName());

			int j = 0;
			for (Object obj : allUses.getRequirements()) {
				DefUse defUse = DefUse.class.cast(obj);

				try {
				if (defUse.getUseTo() != null) { // p-use
					builder.createPUseNode("dua_" + j++, defUse.getVar(), 
							Integer.valueOf(defUse.getDef()), 
							Integer.valueOf(defUse.getUseFrom()),
							Integer.valueOf(defUse.getUseTo()));
				} else { // c-use
					builder.createCUseNode("dua_" + j++, defUse.getVar(), 
							Integer.valueOf(defUse.getDef()), 
							Integer.valueOf(defUse.getUseFrom()));
				}
				}
				catch(java.lang.NumberFormatException eFormat) {
					log.error("Fail to generate dua for:%s.%s", clazz.getClassName(), method.getName());

					if (defUse.getUseTo() != null) { // p-use
						log.error("Puse:%s", defUse);						
					} else { // c-use
						log.error("Cuse:%s", defUse);
					}					
				}

			}
			
			new GXLWriter(builder.getGraph())
					.write(this.dir, this.createFileName(clazz, method,posMethod));
		}
		
	}

	/**
	 * Creates a file name, given a class and a method
	 * 
	 * @param clazz the class
	 * @param method the method
	 * @param method_id number identifying the method
	 * @return the file name created
	 */
	private String createFileName(ClassGen clazz, Method method, int methodId) {
		final String fileNameTemplate = "dua_{0}_{1}_{2}({3}).{4}";

		StringBuffer buffer = new StringBuffer();
		for (Type type : method.getArgumentTypes()) {
			String typeStr = type.toString();
			buffer.append(typeStr.substring(typeStr.lastIndexOf('.') + 1));
			buffer.append(", ");
		}
		String parameters = buffer.toString();
		
		if (!StringUtils.isEmpty(parameters)) {
			parameters = parameters.substring(0, parameters.length() - 2);
		}

		return MessageFormat.format(
				fileNameTemplate, 
				clazz.getClassName(),
				Integer.toString(methodId), 
				method.getName().replaceAll("<", "[").replaceAll(">", "]"),
				parameters,
				GXL_FILE_EXTENSION);
	}
	
}
