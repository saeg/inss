package br.usp.each.j2gxl.information;

/**
 * This exception is raised when the Java class file being examined has some 
 * problem
 * 
 * @author Felipe Albuquerque
 */
public class BadJavaClassFileException extends Exception {

	private static final long serialVersionUID = 1660332574698733259L;
	
	public BadJavaClassFileException() {
		super();
	}

	public BadJavaClassFileException(Exception e) {
		super(e);
	}

}
