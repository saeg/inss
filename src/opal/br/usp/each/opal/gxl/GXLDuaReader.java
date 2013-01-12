package br.usp.each.opal.gxl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.gxl.GXLAttr;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLGraphElement;
import net.sourceforge.gxl.GXLInt;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLSeq;
import net.sourceforge.gxl.GXLString;
import net.sourceforge.gxl.GXLTup;

import org.xml.sax.SAXException;

import br.usp.each.j2gxl.gxl.GXLAllUsesBuilder.GXLSubmissionElement;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.requirement.CUse;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.PUse;

/**
 * Read a GXL Dua graph
 * 
 * @author Marcos L. Chaim 
 * modified by Roberto Andrioli (roberto.araujo [at] usp dot br, roberto.andrioli [at] gmail dot com)
 */
public class GXLDuaReader {

	private GXLGraph graph;
	private DFGraph dfGraph;

	private List<Dua> listDuas = new ArrayList<Dua>();
	private int idCounter = 0;

	/**
	 * Constructor
	 * 
	 * @param dir
	 *            directory where the file is locate.
	 * @param file
	 *            the name of the file.
	 * @param graph
	 *            method name.
	 */
	public GXLDuaReader(String dir, String file, String graph)
			throws IOException, SAXException {
		GXLReader gxlReader = new GXLReader(dir, file, graph);
		this.graph = gxlReader.getGXLGraph();
		traverseGXLGraph();
	}

	/**
	 * Constructor
	 * 
	 * @param file
	 *            the file to be read
	 * @param graph
	 *            method name
	 */
	public GXLDuaReader(File file, String graph) throws IOException,
			SAXException {
		GXLReader gxlReader = new GXLReader(file, graph);
		this.graph = gxlReader.getGXLGraph();
		traverseGXLGraph();
	}

	/**
	 * Constructor
	 * 
	 * @param dir
	 *            directory where the file is locate.
	 * @param file
	 *            the name of the file.
	 * @param graph
	 *            method name.
	 * @param dfGraph
	 *            DefUse structure
	 */
	public GXLDuaReader(String dir, String file, String graph, DFGraph dfGraph)
			throws IOException, SAXException {
		GXLReader gxlReader = new GXLReader(dir, file, graph);
		this.graph = gxlReader.getGXLGraph();
		this.dfGraph = dfGraph;
		traverseGXLGraph();
	}
	
	/**
	 * Constructor
	 * 
	 * @param file
	 *            the file to be read
	 * @param graph
	 *            method name
	 * @param dfGraph
	 *            DefUse structure
	 */
	public GXLDuaReader(File file, String graph, DFGraph dfGraph)
			throws IOException, SAXException {
		GXLReader gxlReader = new GXLReader(file, graph);
		this.graph = gxlReader.getGXLGraph();
		this.dfGraph = dfGraph;
		traverseGXLGraph();
	}

	/**
	 * Traverse the GXL graph to identify the different types of nodes to
	 * collect their information.
	 */
	private void traverseGXLGraph() {
		GXLGraphElement elem;
		String elemTypeStr;

		// Create all nodes
		for (int i = 0; i < graph.getGraphElementCount(); i++) {
			elem = graph.getGraphElementAt(i);
			elemTypeStr = elem.getType().getURI().toString();
			if (elemTypeStr.equals(GXLSubmissionElement.P_USE.getHref())
					|| elemTypeStr.equals(GXLSubmissionElement.C_USE.getHref())) {
				listDuas.add(createDua((GXLNode) elem));
			}
		}
	}

	/**
	 * Creates a Dua containing id, def node, use node or arc use from a GXL
	 * node.
	 * 
	 * @param the
	 *            GXL node
	 * @return Dua created.
	 */
	private Dua createDua(GXLNode node) throws GXLDocumentReadingException {
		Dua d;
		GXLAttr attr;
		GXLSeq seq;
		GXLTup tup;
		GXLInt defnode, usenode, arcnode1, arcnode2;
		GXLString variable;
		int varNo;

		// Get variable
		attr = node.getAttr("Var");
		if (attr != null) {
			variable = (GXLString) attr.getValue();
			try {
				varNo = Integer.parseInt((String) variable.getValue());
			} catch (NumberFormatException e) {
				if (dfGraph != null) {
					varNo = dfGraph.getVarByName(variable.getValue()).getId();
				} else {
					throw new GXLDocumentReadingException("GXLDefUse file was not read previously: " + e.getMessage());
				}
			}
		} else
			throw new GXLDocumentReadingException("Missing Var attribute");

		attr = node.getAttr("DefNode");
		if (attr != null) {
			defnode = (GXLInt) attr.getValue();
		} else
			throw new GXLDocumentReadingException("Missing DefNode attribute");

		attr = node.getAttr("UseArcDua");
		if (attr != null) { // It may be a puse
			seq = (GXLSeq) attr.getValue();
			tup = (GXLTup) seq.getValueAt(0);
			arcnode1 = (GXLInt) tup.getValueAt(0);
			arcnode2 = (GXLInt) tup.getValueAt(1);
			PUse pUso = new PUse(arcnode1.getIntValue(), arcnode2.getIntValue());
			d = new Dua(idCounter++, defnode.getIntValue(), pUso, varNo);
		} else {
			attr = node.getAttr("UseNodeDua");
			if (attr != null) {
				usenode = (GXLInt) attr.getValue();
			} else
				throw new GXLDocumentReadingException("Missing Use attributes");

			d = new Dua(idCounter++, defnode.getIntValue(), new CUse(usenode.getIntValue()), varNo);
		}
		return d;
	}

	/**
	 * Generates an array of Duas
	 * 
	 * @return array of Duas
	 */
	public Dua[] getArrayOfDuas() {
		Dua[] arrayDuas = listDuas.toArray(new Dua[listDuas.size()]);
		return arrayDuas;
	}
}
