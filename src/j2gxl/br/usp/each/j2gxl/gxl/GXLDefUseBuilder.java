/**
 * 
 */
package br.usp.each.j2gxl.gxl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.gxl.GXLBool;
import net.sourceforge.gxl.GXLEdge;
import net.sourceforge.gxl.GXLElement;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLInt;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLSeq;
import net.sourceforge.gxl.GXLString;
import net.sourceforge.gxl.GXLTup;

/**
 * Builds a GXL DefUse graph
 * 
 * @author Felipe Albuquerque
 */
public class GXLDefUseBuilder {

	/**
	 * Defines the GXL DefUse graph elements
	 */
	public enum GXLDefUseElement {
		GRAPH ("graphDefUse.gxl#graphDefUse", LinkType.SIMPLE),
		NODE ("graphDefUse.gxl#Node", LinkType.SIMPLE),
		EDGE ("graphDefUse.gxl#Edge", LinkType.SIMPLE),
		VARS ("graphDefUse.gxl#Vars", LinkType.SIMPLE);
		
		private String href;
		private LinkType linkType;
		
		private GXLDefUseElement(String href, LinkType linkType) {
			this.href = href;
			this.linkType = linkType;
		}

		/**
		 * Gets the element HREF
		 * 
		 * @return the element HREF
		 */
		public String getHref() {
			return this.href;
		}
		
		/**
		 * Gets the element link type
		 * 
		 * @return the element link type
		 */
		public LinkType getLinkType() {
			return this.linkType;
		}
	}
	
	private GXLGraph graph;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 *            the graph ID
	 */
	public GXLDefUseBuilder(String id) {
		this.graph = new GXLGraph(id);
		this.graph.setEdgeIDs(true);
		this.graph.setEdgeMode("directed");
		this.graph.setAllowsHyperGraphs(false);
		this.graph.insert(this.createGXLType(GXLDefUseElement.GRAPH), 0);
	}
	
	/**
	 * Creates a var definition node
	 * 
	 * @param id
	 *            the node ID
	 * @param names
	 *            the mapping between the variables names and positions
	 */
	public void createVars(String id, Map<String, Integer> variables) {
		GXLNode node = null;
		GXLSeq seq = null;
		Set<String> sortedVariables = Collections.emptySet();

		node = new GXLNode(id);
		node.insert(this.createGXLType(GXLDefUseElement.VARS), 0);

		sortedVariables = new TreeSet<String>(variables.keySet());
		seq = new GXLSeq();

		for (String var : sortedVariables) {
			GXLTup tup = new GXLTup();

			tup.add(new GXLString(var));
			tup.add(new GXLInt(variables.get(var)));

			seq.add(tup);
		}

		node.setAttr("Var", seq);
		graph.insert(node, graph.getChildCount());
	}
	
	/**
	 * Creates a node containing defs, undefs and cUses informations
	 * 
	 * @param id
	 *            the node ID
	 * @param defs
	 *            the defs
	 * @param undefs
	 *            the undefs
	 * @param cUses
	 *            the cUses
	 */
	public void createNode(String id, String[] defs, String[] undefs, String[] cUses, String[] pUses) {
		GXLNode node = null;

		node = new GXLNode(id);
		node.insert(this.createGXLType(GXLDefUseElement.NODE), 0);
		node.setAttr("Def", GXLCommonOperations.createGXLSet(defs));
		node.setAttr("CUse", GXLCommonOperations.createGXLSet(cUses));
		node.setAttr("Undef", GXLCommonOperations.createGXLSet(undefs));
		node.setAttr("PUse", GXLCommonOperations.createGXLSet(pUses));

		graph.insert(node, graph.getChildCount());
	}

	/**
	 * Creates an edge containing the pUses, origin node and destination node
	 * informations
	 * 
	 * @param id
	 *            the edge ID
	 * @param to
	 *            the origin node
	 * @param from
	 *            the destination node
	 * @param pUses
	 *            the pUses
	 */
	public void createEdge(String id, String to, String from, String[] pUses) {
		GXLEdge edge = new GXLEdge(from, to);

		edge.setAttribute("id", id);
		edge.insert(this.createGXLType(GXLDefUseElement.EDGE), 0);
		edge.setAttr("PUse", GXLCommonOperations.createGXLSet(pUses));
		edge.setAttr("Unconstrained", new GXLBool(true));

		graph.insert(edge, graph.getChildCount());
	}

	/**
	 * Defines a GXLType for the given GXLDefUseElement
	 * 
	 * @param element the element
	 * @return the GXLType defined
	 */
	private GXLElement createGXLType(GXLDefUseElement element) {
		return GXLCommonOperations
				.createGXLType(element.getHref(), element.getLinkType());
	}

	/**
	 * Gets the graph that is being created
	 * 
	 * @return the graph that is being created
	 */
	public GXLGraph getGraph() {
		return this.graph;
	}
	
}
