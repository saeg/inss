package br.usp.each.commons.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

public class Files {
	
	public static List<File> listRecursive(File dir, FilenameFilter filter) {
		if(!dir.isDirectory())
			throw new RuntimeException(dir + " is not a directory");
		
		List<File> files = new LinkedList<File>();
		for (File f : dir.listFiles(filter)) {
			files.add(f);
		}
		for (File f : dir.listFiles()) {
			if(f.isDirectory())
				files.addAll(listRecursive(f, filter));		
		}
		return files;
	}
	
}
