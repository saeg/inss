package br.usp.each.opal.gxl;

/**
 * This exception is raised when the GXL document could not be read
 * 
 * @author Felipe Albuquerque
 */
public class GXLDocumentReadingException extends RuntimeException {

	private static final long serialVersionUID = -208535164631768666L;

	/**
	 * Constructor
	 * 
	 * @param message
	 *            the message
	 */
	public GXLDocumentReadingException(String message) {
		super(message);
	}
}
