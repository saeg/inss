package br.usp.each.j2gxl.gxl;

import java.io.File;
import java.io.IOException;

import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLValidationException;

import org.xml.sax.SAXException;

/**
 * Writes a graph in files
 * 
 * @author Felipe Albuquerque
 */
public class GXLWriter {

	private GXLDocument document;

	/**
	 * Constructor
	 * 
	 * @param graph
	 *            the graph to be written
	 */
	public GXLWriter(GXLGraph graph) {

		try {
			this.document = new GXLDocument(this.getClass().getResource("template.gxl"));
		} catch (IOException ioException) {
			throw new GXLDocumentCreationException(
					"The specified template does not exist or could not be read");
		} catch (SAXException saxException) {
			throw new GXLDocumentCreationException(
					"The specified template is not a valid XML file");
		} catch (GXLValidationException validationException) {
			throw new GXLDocumentCreationException(
					"The specified template is not a valid GXL file");
		}

		this.document.getDocumentElement().add(graph);
	}

	/**
	 * Creates the GXL document in the directory specified, with the file name
	 * given
	 * 
	 * @param dirPath
	 *            the path for the file directory
	 * @param fileName
	 *            the name of the file to be written
	 * @throws IOException
	 *             when the GXL document could not be created
	 */
	public void write(String dirPath, String fileName) throws IOException {
		String destinationFileName = dirPath
				+ System.getProperty("file.separator") + fileName;

		this.createDirs(dirPath);

		this.document.write(new File(destinationFileName));
	}

	/**
	 * Creates the directories
	 * 
	 * @param path
	 *            the path to be created
	 */
	private boolean createDirs(String path) {
		File dirs = new File(path);

		if (!dirs.exists()) {
			return dirs.mkdirs();
		}
		return false;
	}
}
