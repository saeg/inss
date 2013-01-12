package br.usp.each.opal;

import java.util.Set;

public interface Node<K> extends Iterable<K> {
	
	K getId();
	
	boolean createEdgeTo(K k);
	
	boolean removeEdgeTo(K k);
	
	boolean adjacent(K k);
	
	Set<K> neighbors();
	
	Node<K> getNode(K k);
	
	Node<K> addNode(K k);

}
