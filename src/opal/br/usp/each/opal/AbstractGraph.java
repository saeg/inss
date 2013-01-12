package br.usp.each.opal;

import java.util.Set;

public abstract class AbstractGraph<K> implements Graph<K> {
	
	@Override
	public boolean adjacent(K a, K b) {
		Set<K> neighbors = neighbors(a);
		return neighbors != null ? neighbors.contains(b) : false;
	}

}
