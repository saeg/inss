package br.usp.each.opal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GraphArray<K> extends AbstractGraph<K> implements Graph<K> {
	
	private List<K> nodes;
	private List<List<K>> edges;
	
	public GraphArray() {
		nodes = new ArrayList<K>();
		edges = new ArrayList<List<K>>();
	}
	
	public GraphArray(Collection<K> nodes) {
		this();
		for (K k : nodes) {
			add(k);
		}
	}
	
	@Override
	public boolean add(K k) {
		if (!nodes.contains(k)) {
			nodes.add(k);
			edges.add(new ArrayList<K>());
			return true;
		}
		return false;
	}
	
	@Override
	public boolean adjacent(K a, K b) {
		int pos = nodes.indexOf(a);
		if (pos != -1) {
			return edges.get(pos).contains(b);
		}
		return false;
	}
	
	@Override
	public Set<K> neighbors(K k) {
		int pos = nodes.indexOf(k);
		if (pos != -1)
			return new HashSet<K>(edges.get(pos));
		return null;
	}
	
	@Override
	public boolean addEdge(K from, K to) {
		int pos = nodes.indexOf(from);
		if(pos != -1 && nodes.contains(to)) {
			List<K> neighbors = edges.get(pos);
			if (!neighbors.contains(to)) {
				return neighbors.add(to);
			}
		}
		return false;
	}
	
	@Override
	public boolean removeEdge(K from, K to) {
		int pos = nodes.indexOf(from);
		if(pos != -1) {
			return edges.get(pos).remove(to);
		}
		return false;
	}

	@Override
	public int size() {
		return nodes.size();
	}
	
	@Override
	public Graph<K> inverse() {
		GraphArray<K> g = new GraphArray<K>(nodes);
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
			return new NodeGraphArray(k);
		return null;
	}

	@Override
	public Node<K> getNode(K k) {
		if(nodes.contains(k))
			return new NodeGraphArray(k);
		return null;
	}
	
	@Override
	public Iterator<K> iterator() {
		return nodes.iterator();
	}
	
	public class NodeGraphArray implements Node<K> {
		
		private K id;
		
		public NodeGraphArray(K id) {
			this.id = id;
		}
		
		@Override
		public K getId() {
			return id;
		}
		
		@Override
		public boolean createEdgeTo(K k) {
			return GraphArray.this.addEdge(id, k);
		}
		
		@Override
		public boolean removeEdgeTo(K k) {
			return GraphArray.this.removeEdge(id, k);
		}
		
		@Override
		public boolean adjacent(K k) {
			return GraphArray.this.adjacent(id, k);
		}
		
		@Override
		public Set<K> neighbors() {
			return GraphArray.this.neighbors(id);
		}
		
		public Node<K> addNode(K id) {
			return GraphArray.this.addNode(id);
		}
		
		public Node<K> getNode(K id) {
			return GraphArray.this.getNode(id);
		}
				
		@Override
		public Iterator<K> iterator() {
			int pos = nodes.indexOf(id);
			return edges.get(pos).iterator();
		}
				
		@Override
		public String toString() {
			return id.toString();
		}
	}

}
