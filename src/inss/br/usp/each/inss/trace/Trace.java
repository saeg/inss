package br.usp.each.inss.trace;

public interface Trace<T> {
	
	boolean hasNext();
	
	T next();
	
}
