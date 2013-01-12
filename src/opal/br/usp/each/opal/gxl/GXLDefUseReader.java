package br.usp.each.opal.gxl;

import java.io.File;
import java.io.IOException;

import net.sourceforge.gxl.GXLAttr;
import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLEdge;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLGraphElement;
import net.sourceforge.gxl.GXLInt;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLSeq;
import net.sourceforge.gxl.GXLSet;
import net.sourceforge.gxl.GXLString;
import net.sourceforge.gxl.GXLTup;

import org.xml.sax.SAXException;

import br.usp.each.j2gxl.gxl.GXLDefUseBuilder.GXLDefUseElement;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.ProgramBlock;

/**
 * Builds a GXL DefUse graph
 * 
 * @author Felipe Albuquerque
 * modified by Roberto Andrioli (roberto.araujo [at] usp dot br, roberto.andrioli [at] gmail dot com)
 */
public class GXLDefUseReader {

	private GXLGraph graph;
	private GXLDocument gxlDoc;

	/**
	 * Constructor
	 * 
	 * @param dir
	 *            the path for the file directory
	 * @param file
	 *            the name of the file to be read
	 * @param graph
	 *            the name of the graph searched
	 * @throws IOException
	 *             when the GXL document could not be created
	 * @throws SAXException
	 *             exception raised by GXL
	 */
	public GXLDefUseReader(String dir, String file, String graph)
			throws IOException, SAXException {
		GXLReader gxlReader = new GXLReader(dir, file, graph);
		this.gxlDoc = gxlReader.getGXLDoc();
		this.graph = gxlReader.getGXLGraph();
	}

	/**
	 * Constructor
	 * 
	 * @param file
	 *            GXL graph file
	 * @param graph
	 *            the name of the graph searched
	 * @throws IOException
	 *             when the GXL document could not be created
	 * @throws SAXException
	 *             exception raised by GXL
	 */
	public GXLDefUseReader(File file, String graph)
			throws IOException, SAXException {
		GXLReader gxlReader = new GXLReader(file, graph);
		this.gxlDoc = gxlReader.getGXLDoc();
		this.graph = gxlReader.getGXLGraph();
	}

	/**
	 * Creates a map with the variables and their older ids, a map with the
	 * olders ids and the new ids, and an array with variables name.
	 */
	private void createVarsMaps(DFGraph dfGraph) {
		GXLNode node = null;
		GXLSeq seq = null;
		GXLAttr attr = null;
		GXLString varName = null;
		GXLInt varId;
		GXLTup tup = null;

		node = (GXLNode) gxlDoc.getElement("v1");
		attr = node.getAttr("Var");
		seq = (GXLSeq) attr.getValue();

		for (int i = 0; i < seq.getValueCount(); i++) {
			tup = (GXLTup) seq.getValueAt(i);

			varName = (GXLString) tup.getValueAt(0);
			varId = (GXLInt) tup.getValueAt(1);

			dfGraph.addVar(varName.getValue(), varId.getIntValue());
		}
	}

	/**
	 * Traverse the GXL graph to identify the different types of nodes to
	 * collect their information.
	 */
	private void traverseGXLGraph(DFGraph dfGraph) {
		GXLGraphElement elem;

		// Create all nodes
		for (int i = 0; i < graph.getGraphElementCount(); i++) {
			elem = graph.getGraphElementAt(i);
			if (elem.getType().getURI().toString()
					.equals(GXLDefUseElement.NODE.getHref())) {
				dfGraph.add(createDFNode((GXLNode) elem));
			}
		}
		// Create all edges
		for (int i = 0; i < graph.getGraphElementCount(); i++) {
			elem = graph.getGraphElementAt(i);
			if (elem.getType().getURI().toString()
					.equals(GXLDefUseElement.EDGE.getHref())) {
				createEdge((GXLEdge) elem, dfGraph);
			}
		}
	}

	/**
	 * Creates a noFD containing id, defs, undefs and cUses informations from a
	 * GXL node.
	 * 
	 * @param node
	 *            the GXL node
	 * @return NoFD created.
	 */
	private ProgramBlock createDFNode(GXLNode node) {
		GXLAttr attr;
		GXLSet set;
		GXLString value;

		int nodeId = Integer.parseInt(node.getID());
		ProgramBlock nodeDF = new ProgramBlock(nodeId);

		// Set def variables
		attr = node.getAttr("Def");
		if (attr != null) {
			set = (GXLSet) attr.getValue();
			if (set != null) {// there is Def set
				for (int i = 0; i < set.getChildCount(); i++) {
					value = (GXLString) set.getChildAt(i);
					nodeDF.def(Integer.parseInt(value.getValue()));
				}
			}
		}
		// Set cuse variables
		attr = node.getAttr("CUse");
		if (attr != null) {
			set = (GXLSet) attr.getValue();
			if (set != null) {// there is cuse set
				for (int i = 0; i < set.getChildCount(); i++) {
					value = (GXLString) set.getChildAt(i);
					nodeDF.cuse(Integer.parseInt(value.getValue()));
				}
			}
		}
		// Set undef of variables
		attr = node.getAttr("Undef");
		if (attr != null) {
			set = (GXLSet) attr.getValue();
			if (set != null) { // there is Undef set
				for (int i = 0; i < set.getChildCount(); i++) {
					value = (GXLString) set.getChildAt(i);
					nodeDF.undef(Integer.parseInt(value.getValue()));
				}
			}
		}
		// Set puse of variables
		attr = node.getAttr("PUse");
		if (attr != null) {
			set = (GXLSet) attr.getValue();
			if (set != null) { // there is PUse set
				for (int i = 0; i < set.getChildCount(); i++) {
					value = (GXLString) set.getChildAt(i);
					nodeDF.puse(Integer.parseInt(value.getValue()));
				}
			}
		}
		return nodeDF;
	}

	/**
	 * Associates an egde connecting the nodes in the grafoFD structure
	 * 
	 * @param edge
	 *            GXL edge
	 */
	private void createEdge(GXLEdge edge, DFGraph dfGraph) {
		int from = Integer.valueOf(edge.getAttribute("from"));
		int to = Integer.valueOf(edge.getAttribute("to"));
		
		ProgramBlock fromBlock = dfGraph.getProgramBlockById(from);
		ProgramBlock toBlock = dfGraph.getProgramBlockById(to);
		
		// Connect nodes through an edge
		dfGraph.addEdge(fromBlock, toBlock);

		// Set puse variables
		GXLAttr attr = edge.getAttr("PUse");
		if (attr != null) {
			GXLSet set = (GXLSet) attr.getValue();
			GXLString value;
			if (set != null) {// there is cuse set
				for (int i = 0; i < set.getChildCount(); i++) {
					value = (GXLString) set.getChildAt(i);
					dfGraph.getProgramBlockById(from).puse(Integer.parseInt(value.getValue()));
				}
			}
		}
	}
	
	public DFGraph getDFGraph(String className, int methodId) {
		DFGraph graph = new DFGraph(className, methodId);
		createVarsMaps(graph);
		traverseGXLGraph(graph);
		return graph;
	}
}
