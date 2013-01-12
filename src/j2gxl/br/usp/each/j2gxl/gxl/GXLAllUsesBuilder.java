package br.usp.each.j2gxl.gxl;

import net.sourceforge.gxl.GXLElement;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLInt;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLSeq;
import net.sourceforge.gxl.GXLString;

/**
 * Builds a GXL AllUses graph
 * 
 * @author Felipe Albuquerque
 */
public class GXLAllUsesBuilder {

	/**
	 * Defines the GXL DefUse graph elements
	 */
	public enum GXLSubmissionElement {
	    GRAPH("graphSubmission.gxl#graphSubmission", LinkType.SIMPLE),
	    NODE("graphSubmission.gxl#Node", LinkType.SIMPLE),
	    EDGE("graphSubmission.gxl#Edge", LinkType.SIMPLE),
	    P_USE("graphSubmission.gxl#PUsoDua", LinkType.SIMPLE),
	    C_USE("graphSubmission.gxl#CUsoDua", LinkType.SIMPLE),
	    EDGEP("graphSubmission.gxl#EdgeP", LinkType.SIMPLE),
	    EDGEC("graphSubmission.gxl#EdgeC", LinkType.SIMPLE);
	    
	    private String href;
	    private LinkType linkType;

	    private GXLSubmissionElement(String href, LinkType linkType) {
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
	 * @param id the graph ID
	 */
	public GXLAllUsesBuilder(String id) {
		this.graph = new GXLGraph(id);
		this.graph.setEdgeIDs(true);
		this.graph.setEdgeMode("directed");
		this.graph.setAllowsHyperGraphs(false);
		this.graph.insert(this.createGXLType(GXLSubmissionElement.GRAPH), 0);	
	}

	/**
	 * Creates a node containing defs, undefs and cUses informations
	 * 
	 * @param id the node ID
	 * @param var the variable
	 * @param defNode the node in wich the variable is defined
	 * @param useNode the node in wich the variable is used
	 */
	public void createCUseNode(String id, String var, Integer defNode, 
			Integer useNode) {
		GXLNode node = null;

		node = new GXLNode(id);
		node.insert(this.createGXLType(GXLSubmissionElement.C_USE), 0);
		node.setAttr("Var", new GXLString(var));
		node.setAttr("DefNode", new GXLInt(defNode));
		node.setAttr("UseNodeDua", new GXLInt(useNode));

		this.graph.insert(node, this.graph.getChildCount());
	}
	
	/**
	 * Creates a node containing defs, undefs and cUses informations
	 * 
	 * @param id the node ID
	 * @param var the variable
	 * @param defNode the node in wich the variable is defined
	 * @param fromUseNode the from node
	 * @param toUseNode the to node
	 */
	public void createPUseNode(String id, String var, Integer defNode, 
			Integer fromUseNode, Integer toUseNode) {
		GXLNode node = null;
		GXLSeq seq = null;
		
		node = new GXLNode(id);
		node.insert(this.createGXLType(GXLSubmissionElement.P_USE), 0);
		node.setAttr("Var", new GXLString(var));
		node.setAttr("DefNode", new GXLInt(defNode));
		
		seq = new GXLSeq();
		seq.add(GXLCommonOperations.createGXLTup(fromUseNode, toUseNode));
		
		node.setAttr("UseArcDua", seq);

		this.graph.insert(node, this.graph.getChildCount());
	}

	/**
	 * Defines a GXLType for the given GXLDefUseElement
	 * 
	 * @param element the element
	 * @return the GXLType defined
	 */
	private GXLElement createGXLType(GXLSubmissionElement element) {
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
