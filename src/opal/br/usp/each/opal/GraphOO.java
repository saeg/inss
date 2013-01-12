package br.usp.each.opal;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class GraphOO<K> extends AbstractGraph<K> implements Graph<K> {
	
	private Map<K,Node<K>> nodes;
	
	public GraphOO() {
		nodes = new LinkedHashMap<K, Node<K>>();
	}
	
	public GraphOO(Collection<K> nodes) {
		this();
		for (K k : nodes) {
			add(k);
		}
	}
	
	@Override
	public boolean add(K k) {
		if (!nodes.containsKey(k)) {
			nodes.put(k, new NodeGraphOO(k));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean adjacent(K a, K b) {
		Node<K> n = getNode(a);
		if (n != null) {
			return n.adjacent(b);
		}
		return false;
	}
	
	@Override
	public Set<K> neighbors(K k) {
		Node<K> n = getNode(k);
		if (n != null) {
			return n.neighbors();
		}
		return null;
	}
	
	@Override
	public boolean addEdge(K from, K to) {
		Node<K> n = getNode(from);
		if (n != null) {
			return n.createEdgeTo(to);
		}
		return false;
	}
	
	@Override
	public boolean removeEdge(K from, K to) {
		Node<K> n = getNode(from);
		if (n != null) {
			return n.removeEdgeTo(to);
		}
		return false;
	}
	
	@Override
	public int size() {
		return nodes.size();
	}
	
	@Override
	public GraphOO<K> inverse() {
		GraphOO<K> g = new GraphOO<K>(nodes.keySet());
		for(K from : this) {
			for(K to : getNode(from)) {
				g.addEdge(to, from);
			}
		}
		return g;
	}
	
	@Override
	public Node<K> addNode(K id) {
		Node<K> node;
		if (!nodes.containsKey(id)) {
			node = new NodeGraphOO(id);
			nodes.put(id, node);
			return node;
		}
		return null;
	}
	
	@Override
	public Node<K> getNode(K id) {
		return nodes.get(id);
	}
	
	@Override
	public Iterator<K> iterator() {
		return nodes.keySet().iterator();
	}
	
	@Override
	public String toString() {
		return nodes.toString();
	}
	
	public class NodeGraphOO implements Node<K> {
		
		private K id;
		private Set<K> arcs;
		
		public NodeGraphOO(K id) {
			this.id = id;
			arcs = new LinkedHashSet<K>();
		}
		
		@Override
		public K getId() {
			return id;
		}
		
		@Override
		public boolean createEdgeTo(K k) {
			Node<K> to = getNode(k);
			if(to != null) {
				return arcs.add(k);
			}
			return false;
		}
		
		@Override
		public boolean removeEdgeTo(K k) {
			Node<K> to = getNode(k);
			if(to != null) {
				return arcs.remove(k);
			}
			return false;
		}
		
		@Override
		public boolean adjacent(K k) {
			return arcs.contains(k);
		}
		
		@Override
		public Set<K> neighbors() {
			return arcs;
		}
		
		public Node<K> addNode(K id) {
			Node<K> node;
			if (!nodes.containsKey(id)) {
				node = new NodeGraphOO(id);
				nodes.put(id, node);
				return node;
			}
			return null;
		}
		
		public Node<K> getNode(K id) {
			return nodes.get(id);
		}
				
		@Override
		public Iterator<K> iterator() {
			return arcs.iterator();
		}
				
		@Override
		public String toString() {
			return id.toString();
		}
	}
}
