package br.usp.each.opal.requirement;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.each.commons.template.Template;
import br.usp.each.commons.template.Templater;
import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.gxl.GXLDuaReader;

public class DuaGXLLoader implements RequirementDetermination {
	
	private Map<String, GXLFileEntry> fileEntries;

	@SuppressWarnings("unchecked")
	public DuaGXLLoader(File dir) throws FileNotFoundException, IOException {
		if (!dir.exists()) {
			throw new FileNotFoundException("File not found: " + dir.getName());
		}
		if (!dir.isDirectory()) {
			throw new FileNotFoundException(dir.getName() + " is not a directory.");
		}
		File index = new File(dir, "dua_index.ser");
		if (index.exists()) {
			FileInputStream fileIn = new FileInputStream(index);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			try {
				fileEntries = (Map<String, GXLFileEntry>) in.readObject();
			} catch (ClassNotFoundException e) {
				createIndex(dir);
			}
		} else {
			createIndex(dir);
			FileOutputStream fileOut = new FileOutputStream(index);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(fileEntries);
			out.close();
			fileOut.close();
		}
	}
	
	@Override
	public Requirement[] requirement(DFGraph graph) {
		String className = graph.getClassName();
		int methodId = graph.getMethodId();
		
		GXLFileEntry entry = fileEntries.get(className);
		if (entry == null) {
			throw new RequirementDeterminationException(String.format(
					"duas file not found for class: %s and method: %d", className, methodId));
		}
		try {
			GXLDuaReader reader = new GXLDuaReader(entry.getDuasFile(methodId), entry.getMethodName(methodId), graph);
			return reader.getArrayOfDuas();
		} catch (Exception e) {
			throw new RequirementDeterminationException(e);
		}
	}
	
	private void createIndex(File dir) throws IOException {
		fileEntries = new HashMap<String, GXLFileEntry>();
		Templater templater = new Templater();
		
		final Template duaName = templater.template("template.inss.dua_name");
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().matches(duaName.getRegex());
			}
		});
		for (File f : files) {
			String fileName = f.getName();
			String className = duaName.get("className", fileName);
			String methodName = duaName.get("methodName", fileName);
			duaName.set("className", className.replace("$", "\\$"));
			duaName.set("methodName", methodName.replace("[", "\\[").replace("]", "\\]").replace("$", "\\$"));
			int methodId = Integer.valueOf(duaName.get("methodId", fileName));
			GXLFileEntry entry = fileEntries.get(className);
			if(entry == null) {
				entry = new GXLFileEntry();
				fileEntries.put(className, entry);
			}
			entry.addDuasFile(methodId, methodName, f);
			duaName.set("className", ".*");
			duaName.set("methodName", ".*");
		}		
	}

	private static class GXLFileEntry implements Serializable {

		private static final long serialVersionUID = -5390649936211108859L;

		private List<File> duasFiles;
		private List<String> methodNames;

		public GXLFileEntry() {
			duasFiles = new ArrayList<File>();
			methodNames = new ArrayList<String>();
		}

		public void addDuasFile(int i, String methodName, File f) {
			int j = i + 1;
			while (duasFiles.size() < j)
				duasFiles.add(null);
			while (methodNames.size() < j)
				methodNames.add(null);
			duasFiles.set(i, f);
			methodNames.set(i, methodName.replace('[', '<').replace(']', '>'));
		}

		public File getDuasFile(int i) {
			return duasFiles.get(i);
		}

		public String getMethodName(int i) {
			return methodNames.get(i);
		}
	}

}
