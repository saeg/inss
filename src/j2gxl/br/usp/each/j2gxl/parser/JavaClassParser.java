package br.usp.each.j2gxl.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import br.usp.each.commons.io.Files;

/**
 * Generates JavaClass classes from the given file
 * 
 * @author Felipe Albuquerque
 */
public class JavaClassParser {

	private String fileName;
	private File file;
	
	/**
	 * Constructor
	 * 
	 * @param fileName the file from witch the JavaClasses will be generated 
	 * @throws FileNotFoundException if fileName does not exists
	 */
	public JavaClassParser(String fileName) throws FileNotFoundException {
		this.fileName = fileName;
		this.file = new File(fileName);
		if(!file.exists()) {
			throw new FileNotFoundException("File not found: " + fileName);
		}
	}
	
	/**
	 * Generates the JavaClass classes from the file
	 * 
	 * @return the JavaClass classes created
	 * @throws IOException if an I/O error has occurred 
	 */
	public List<JavaClass> generateJavaClasses() throws IOException {
		List<JavaClass> javaClasses = new ArrayList<JavaClass>();
		List<InputStream> inputStreams = Collections.emptyList();
		List<String> classesNames = Collections.emptyList();
		
		if (this.file.isFile() && 
				(this.fileName.endsWith(".jar") || this.fileName.endsWith(".zip"))) {
			
			ZipFile file = null;
			Enumeration<? extends ZipEntry> entries = null;
			
			inputStreams = new ArrayList<InputStream>();
			classesNames = new ArrayList<String>();
			
			if (this.fileName.endsWith(".jar")) {
				file = new JarFile(this.file);
			} else {
				file = new ZipFile(this.file);
			}

			entries = file.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String className = entry.getName();

				if (className.endsWith(".class")) {
					inputStreams.add(file.getInputStream(entry));
					classesNames.add(className);	
				}
				
			}
			
		} else if (file.isDirectory()) {
			inputStreams = new ArrayList<InputStream>();
			classesNames = new ArrayList<String>();
			List<File> files = Files.listRecursive(file, new FilenameFilter() {			
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".class");
				}
			});
			for (File file : files) {
				inputStreams.add(new FileInputStream(file));
				classesNames.add(file.getName());
			}
		} else {
			
			if (!this.fileName.endsWith(".class")) {
				throw new IllegalArgumentException(
						"The file extension must be .class, .jar or .zip");	
			}

		}
		
		if (!inputStreams.isEmpty()) {
			
			int posClass = 0;
			Iterator<InputStream> iterator = inputStreams.iterator();
			while (iterator.hasNext()) {
				javaClasses.add(new ClassParser(iterator.next(), classesNames.get(posClass)).parse());
				posClass++;
			}
			
		} else {
			javaClasses = Collections.singletonList(new ClassParser(this.fileName).parse());
		}
		
		return javaClasses;
	}
	
}
