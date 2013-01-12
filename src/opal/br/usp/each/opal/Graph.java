package br.usp.each.opal;

import java.util.Set;

public interface Graph<K> extends Iterable<K> {
	
	boolean add(K k);
	
	boolean adjacent(K a, K b);
	
	Set<K> neighbors(K k);
	
	boolean addEdge(K from, K to);
	
	boolean removeEdge(K from, K to);
	
	Graph<K> inverse();
	
	int size();
	
	Node<K> addNode(K k);
	
	Node<K> getNode(K k);
		
}
