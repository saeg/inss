package br.usp.each.j2gxl.gxl;

/**
 * This exception is raised when the GXL document could not be created
 * 
 * @author Felipe Albuquerque
 */
public class GXLDocumentCreationException extends RuntimeException {

	private static final long serialVersionUID = 1826529493990878901L;

	/**
	 * Constructor
	 * 
	 * @param message
	 *            the message
	 */
	public GXLDocumentCreationException(String message) {
		super(message);
	}
}
