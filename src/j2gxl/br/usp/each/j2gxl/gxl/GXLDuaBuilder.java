package br.usp.each.j2gxl.gxl;

import java.net.URI;
import java.net.URISyntaxException;

import net.sourceforge.gxl.GXL;
import net.sourceforge.gxl.GXLEdge;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLInt;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLSeq;
import net.sourceforge.gxl.GXLString;
import net.sourceforge.gxl.GXLTup;
import net.sourceforge.gxl.GXLType;
import br.usp.each.j2gxl.gxl.GXLAllUsesBuilder.GXLSubmissionElement;

/**
 * Builds a GXL Dua graph
 * 
 * @author Felipe Albuquerque
 */
public class GXLDuaBuilder {

	private GXLGraph graph;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            the graph ID
	 */
	public GXLDuaBuilder(String id) {
		this.graph = new GXLGraph(id);
		this.graph.setEdgeIDs(true);
		this.graph.setEdgeMode("directed");
		this.graph.setAllowsHyperGraphs(false);
		this.graph.insert(this.createGXLType(GXLSubmissionElement.GRAPH), 0);
	}

	/**
	 * Creates a node of the subsumption graph
	 * 
	 * @param id
	 *            the node ID
	 * 
	 */
	public void createNode(String id) {
		GXLNode node = null;

		node = new GXLNode(id);
		node.insert(this.createGXLType(GXLSubmissionElement.NODE), 0);
		graph.insert(node, graph.getChildCount());
	}

	/**
	 * Creates a node containing p-use dua information
	 * 
	 * @param id
	 *            the node ID
	 * @param defNode
	 *            the def node
	 * @param orig
	 *            origin node of the p-use arc
	 * @param dest
	 *            destiny node of the p-use arc
	 * @param var
	 *            number of the variable
	 */
	public void createPUseDua(String id, int defNode, int orig, int dest, int var) {
		GXLNode node = null;

		node = new GXLNode(id);
		node.insert(this.createGXLType(GXLSubmissionElement.P_USE), 0);
		node.setAttr("DefNode", new GXLInt(defNode));
		node.setAttr("Var", new GXLString(Integer.toString(var)));

		GXLSeq seq = new GXLSeq();
		GXLTup tup = new GXLTup();
		tup.add(new GXLInt(orig));
		tup.add(new GXLInt(dest));
		seq.add(tup);
		node.setAttr("UseArcDua", seq);

		graph.insert(node, graph.getChildCount());
	}

	/**
	 * Creates a node containing c-use dua information
	 * 
	 * @param id
	 *            the node ID
	 * @param defNode
	 *            the def node
	 * @param useNode
	 *            node where the use occurs
	 * @param var
	 *            number of the variable
	 */
	public void createCUseDua(String id, int defNode, int useNode, int var) {
		GXLNode node = null;

		node = new GXLNode(id);
		node.insert(this.createGXLType(GXLSubmissionElement.C_USE), 0);
		node.setAttr("DefNode", new GXLInt(defNode));
		node.setAttr("UseNodeDua", new GXLInt(useNode));
		node.setAttr("Var", new GXLString(Integer.toString(var)));

		graph.insert(node, graph.getChildCount());
	}

	/**
	 * Creates an edge connecting an origin node and destination node of the
	 * subsumption graph
	 * 
	 * @param id
	 *            the edge ID
	 * @param to
	 *            the origin node
	 * @param from
	 *            the destination node
	 */
	public void createEdge(String id, String to, String from) {
		GXLEdge edge = new GXLEdge(from, to);

		edge.setAttribute("id", id);
		edge.insert(this.createGXLType(GXLSubmissionElement.EDGE), 0);

		graph.insert(edge, graph.getChildCount());
	}

	/**
	 * Defines a GXLType for the GXLDuaElement given
	 * 
	 * @param element
	 *            the element
	 * @return the GXLType defined
	 */
	private GXLType createGXLType(GXLSubmissionElement element) {
		GXLType type = null;

		try {
			type = new GXLType(new URI(GXL.GXL_XMLNS_XLINK));
		} catch (URISyntaxException syntaxException) {
			throw new RuntimeException(syntaxException);
		}

		type.setAttribute(GXL.XLINK_HREF, element.getHref());
		type.setAttribute(GXL.XLINK_TYPE, element.getLinkType().getValue());

		return type;
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
