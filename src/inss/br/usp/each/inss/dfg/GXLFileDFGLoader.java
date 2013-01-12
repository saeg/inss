package br.usp.each.inss.dfg;

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
import br.usp.each.opal.gxl.GXLDefUseReader;

public class GXLFileDFGLoader implements DFGLoader {
	
	private Map<String, GXLFileEntry> fileEntries;
	
	@SuppressWarnings("unchecked")
	public GXLFileDFGLoader(File dir) throws FileNotFoundException, IOException {
		if (!dir.exists()) {
			throw new FileNotFoundException("File not found: " + dir.getName());
		}
		if (!dir.isDirectory()) {
			throw new FileNotFoundException(dir.getName() + " is not a directory.");
		}
		File index = new File(dir, "defuse_index.ser");
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
	
	public GXLFileDFGLoader(String dir) throws FileNotFoundException, IOException {
		this(new File(dir));
	}

	@Override
	public DFGraph load(String className, int methodId) throws DFGLoaderException {
		GXLFileEntry entry = fileEntries.get(className);
		if (entry == null) {
			throw new DFGLoaderException(String.format(
					"def/use graph file not found for class: %s and method: %d", className, methodId));
		}
		try {
			GXLDefUseReader reader = new GXLDefUseReader(entry.getDefUseFile(methodId), entry.getMethodName(methodId));
			return reader.getDFGraph(className, methodId);
		} catch (Exception e) {
			throw new DFGLoaderException(e);
		} 
	}
	
	private void createIndex(File dir) throws IOException {
		fileEntries = new HashMap<String, GXLFileEntry>();
		Templater templater = new Templater();
		
		final Template defUseName = templater.template("template.inss.defuse_name");		
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().matches(defUseName.getRegex());
			}
		});
		for (File f : files) {
			String fileName = f.getName();
			String className = defUseName.get("className", fileName);
			String methodName = defUseName.get("methodName", fileName);
			defUseName.set("className", className.replace("$", "\\$"));
			defUseName.set("methodName", methodName.replace("[", "\\[").replace("]", "\\]").replace("$", "\\$"));
			int methodId = Integer.valueOf(defUseName.get("methodId", fileName));
			GXLFileEntry entry = fileEntries.get(className);
			if(entry == null) {
				entry = new GXLFileEntry();
				fileEntries.put(className, entry);
			}
			entry.addDefUseFile(methodId, methodName, f);
			defUseName.set("className", ".*");
			defUseName.set("methodName", ".*");
		}		
	}
	
	private static class GXLFileEntry implements Serializable {
		
		private static final long serialVersionUID = -5343620219710338170L;
		
		private List<File> defUseFiles;
		private List<String> methodNames;

		public GXLFileEntry() {
			defUseFiles = new ArrayList<File>();
			methodNames = new ArrayList<String>();
		}

		public void addDefUseFile(int i, String methodName, File f) {
			int j = i + 1;
			while(defUseFiles.size() < j)
				defUseFiles.add(null);
			while(methodNames.size() < j)
				methodNames.add(null);
			defUseFiles.set(i, f);
			methodNames.set(i, methodName.replace('[', '<').replace(']', '>'));
		}
		
		public File getDefUseFile(int i) {
			return defUseFiles.get(i);
		}
				
		public String getMethodName(int i) {
			return methodNames.get(i);
		}
	}

}
