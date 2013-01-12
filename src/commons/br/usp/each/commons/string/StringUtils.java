package br.usp.each.commons.string;

public class StringUtils {
	
	public static boolean isEmpty(String str) {
		for (int i = 0; i < str.length(); i++) {
			if(!Character.isWhitespace(str.charAt(i)))
				return false;
		}
		return true;
	}

}
