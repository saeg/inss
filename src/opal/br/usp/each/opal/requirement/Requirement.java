package br.usp.each.opal.requirement;

import java.io.Serializable;


public class Requirement implements Serializable {
	
	private static final long serialVersionUID = 317995348793884154L;

	/**
	 * True if this requirement is reached (covered) false otherwise
	 */
	private boolean covered;
	
	public Requirement() {
		covered = false;
	}
	
	public void cover() {
		covered = true;
	}
	
	public boolean isCovered() {
		return covered;
	}
	
}
