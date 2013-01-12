package br.usp.each.opal.gxl;

import java.io.File;
import java.io.IOException;

import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLGraph;

import org.xml.sax.SAXException;

/**
 * Gets pointers to a GXL graph and document.
 * 
 * @author Marcos L. Chaim
 */
public class GXLReader {

	private GXLDocument gxlDoc;
	private GXLGraph graph;

	/**
	 * Read a GXL graph from a GXL document in the directory specified, with
	 * the file and graph name given
	 * 
	 * @param dirPath
	 *            the path for the file directory
	 * @param fileName
	 *            the name of the file to be read
	 * @param graphName
	 *            the name of the graph searched
	 * @throws IOException
	 *             when the GXL document could not be created
	 * @throws SAXException
	 *             exception raised by GXL
	 */

	public GXLReader(String dirPath, String fileName, String graphName)
			throws IOException, SAXException {
		String destinationFileName = dirPath
				+ System.getProperty("file.separator") + fileName;

		File file = new File(destinationFileName);
		gxlDoc = new GXLDocument(file);
		graph = (GXLGraph) gxlDoc.getElement(graphName);

	}

	/**
	 * Read a GXL graph from a GXL document in the file specified
	 * 
	 * @param file
	 *            GXL graph file
	 * @param graphName
	 *            the name of the graph searched
	 * @throws IOException
	 *             when the GXL file could not be read
	 * @throws SAXException
	 *             when the GXL document has a error
	 */
	public GXLReader(File file, String graphName) throws IOException,
			SAXException {
		gxlDoc = new GXLDocument(file);
		graph = (GXLGraph) gxlDoc.getElement(graphName);
	}

	/**
	 * Returns the pointer to the GXL document.
	 * 
	 * @return graph
	 */
	public GXLGraph getGXLGraph() {
		return graph;
	}

	/**
	 * Returns the pointer to the GXL document graph.
	 * 
	 * @return gxlDoc
	 */
	public GXLDocument getGXLDoc() {
		return gxlDoc;
	}
}
