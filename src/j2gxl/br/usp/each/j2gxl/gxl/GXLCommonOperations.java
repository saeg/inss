package br.usp.each.j2gxl.gxl;

import java.net.URI;
import java.net.URISyntaxException;

import net.sourceforge.gxl.GXL;
import net.sourceforge.gxl.GXLInt;
import net.sourceforge.gxl.GXLSet;
import net.sourceforge.gxl.GXLString;
import net.sourceforge.gxl.GXLTup;
import net.sourceforge.gxl.GXLType;

/**
 * Library with common GXL operations
 * 
 * @author Felipe Albuquerque
 */
final class GXLCommonOperations {

	/**
	 * Constructor
	 */
	private GXLCommonOperations() {
		super();
	}
	
	/**
	 * Creates a GXLSet containing the values given
	 * 
	 * @param values the values
	 * @return the set created
	 */
	public static GXLSet createGXLSet(String... values) {
		GXLSet gxlSet = new GXLSet();
		
		for (String value : values) {
			gxlSet.add(new GXLString(value));
		}
		
		return gxlSet;
	}

	/**
	 * Creates a GXLTup containing the values given
	 * 
	 * @param values the values
	 * @return the tuple created
	 */
	public static GXLTup createGXLTup(Integer... values) {
		GXLTup gxlTup = new GXLTup();
		
		for (Integer value : values) {
			gxlTup.add(new GXLInt(value));
		}
		
		return gxlTup;
	}
	
	/**
	 * Defines a GXLType for the given HREF and link type
	 * 
	 * @param href the HREF
	 * @param linkType the link type
	 * @return the GXLType defined
	 */
	public static GXLType createGXLType(String href, LinkType linkType) {
		GXLType type = null;
		
		try {
			type = new GXLType(new URI(GXL.GXL_XMLNS_XLINK));
		} catch (URISyntaxException syntaxException) {
			throw new RuntimeException(syntaxException);
		}
		
		type.setAttribute(GXL.XLINK_HREF, href);
		type.setAttribute(GXL.XLINK_TYPE, linkType.getValue());

		return type;
	}
	
}
