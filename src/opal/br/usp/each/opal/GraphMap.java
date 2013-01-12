package br.usp.each.opal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class GraphMap<K> extends AbstractGraph<K> implements Graph<K> {

	private Map<K, Set<K>> edges;
	
	public GraphMap() {
		edges = new LinkedHashMap<K, Set<K>>();
	}
	
	public GraphMap(Collection<K> nodes) {
		this();
		for (K k : nodes) {
			add(k);
		}
	}
	
	@Override
	public boolean add(K k) {
		if (!edges.containsKey(k)) {
			edges.put(k, new HashSet<K>());
			return true;
		}
		return false;
	}
	
	@Override
	public boolean adjacent(K a, K b) {
		Set<K> set = edges.get(a);
		if(set != null) {
			return set.contains(b);
		}
		return false;
	}

	@Override
	public Set<K> neighbors(K k) {
		return edges.get(k);
	}

	@Override
	public boolean addEdge(K from, K to) {
		Set<K> set = edges.get(from);
		if(set != null) {
			if(edges.containsKey(to)) {
				return set.add(to);
			}
		}
		return false;
	}

	@Override
	public boolean removeEdge(K from, K to) {
		Set<K> set = edges.get(from);
		if(set != null) {
			return set.remove(to);
		}
		return false;
	}
	
	@Override
	public int size() {
		return edges.size();
	}
				
	public GraphMap<K> inverse() {
		GraphMap<K> g = new GraphMap<K>(edges.keySet());
		for(K from : this) {
			for(K to : getNode(from)) {
				g.addEdge(to, from);
			}
		}
		return g;
	}
	
	@Override
	public Node<K> addNode(K k) {
		if(add(k))
			return new NodeGraphMap(k);
		return null;
	}
	
	public Node<K> getNode(K node) {
		if(edges.containsKey(node))
			return new NodeGraphMap(node);
		return null;
	}
	
	@Override
	public Iterator<K> iterator() {
		return edges.keySet().iterator();
	}
	
	@Override
	public String toString() {
		return edges.toString();
	}
		
	public class NodeGraphMap implements Node<K> {
		
		private K i;
		
		public NodeGraphMap(K k) {
			this.i = k;
		}
		
		@Override
		public K getId() {
			return i;
		}
		
		@Override
		public boolean createEdgeTo(K k) {
			if(edges.containsKey(k)) {
				return edges.get(i).add(k);
			}
			return false;
		}
		
		@Override
		public boolean removeEdgeTo(K k) {
			if(edges.containsKey(k)) {
				return edges.get(i).remove(k);
			}
			return false;
		}				
		
		@Override
		public boolean adjacent(K k) {
			return edges.get(i).contains(k);
		}
		
		@Override
		public Set<K> neighbors() {
			return edges.get(i);
		}
		
		@Override
		public Node<K> addNode(K k) {
			if(add(k))
				return new NodeGraphMap(k);
			return null;
		}
		
		@Override
		public Node<K> getNode(K k) {
			if(edges.containsKey(k))
				return new NodeGraphMap(k);
			return null; 
		}

		@Override
		public Iterator<K> iterator() {		
			return edges.get(i).iterator();
		}
		
		@Override
		public String toString() {
			return i.toString();
		}
	}
}
