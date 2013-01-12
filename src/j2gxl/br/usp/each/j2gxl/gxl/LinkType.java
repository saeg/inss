package br.usp.each.j2gxl.gxl;

/**
 * Defines the link types
 */
public enum LinkType {
	
	SIMPLE("simple");

	private String value;

	private LinkType(String value) {
		this.value = value;
	}

	/**
	 * Gets the link type value
	 * 
	 * @return the link type value
	 */
	public String getValue() {
		return value;
	}
	
}
