package br.usp.each.opal.gxl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLGraph;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class TestGXLReader {

	private File file;
	private String dir, fileName, graphName;
	private GXLDocument gxldoc;
	private GXLGraph gxlgrp;

	@BeforeClass
	public void before() {
		dir = "projects/opal-testing/data";
		fileName = "defuse_Sort_sort(int[], int).gxl";
		graphName = "sort";
		file = new File(dir, fileName);
		gxldoc = null;
		gxlgrp = null;
	}

	@Test
	public void testReadingFromFolderAndFileName() {
		try {
			GXLReader reader = new GXLReader(dir, fileName, graphName);
			gxldoc = reader.getGXLDoc();
			gxlgrp = reader.getGXLGraph();
		} catch (Exception e) {
			System.out.println("Error while reading: " + e);
		}
		Assert.assertNotNull(gxldoc);
		Assert.assertNotNull(gxlgrp);
	}

	@Test
	public void testReadingFromFile() {
		try {
			GXLReader reader = new GXLReader(file, graphName);
			gxldoc = reader.getGXLDoc();
			gxlgrp = reader.getGXLGraph();
		} catch (Exception e) {
			System.out.println("Error while reading: " + e);
		}
		Assert.assertNotNull(gxldoc);
		Assert.assertNotNull(gxlgrp);
	}

	@Test
	public void testInvalidGraphName() {
		try {
			GXLReader reader = new GXLReader(file, "dZrLMiNeNE");
			gxldoc = reader.getGXLDoc();
			gxlgrp = reader.getGXLGraph();
		} catch (Exception e) {
			System.out.println("Error while reading: " + e);
		}
		Assert.assertNotNull(gxldoc);
		Assert.assertNull(gxlgrp); // May be NULL
	}

	@Test(expectedExceptions = FileNotFoundException.class)
	public void testReadingFromNotFoundFolderAndFileName() throws IOException, SAXException {
		GXLReader reader = new GXLReader("XigKgrK70H", "W148G6wv4d", "EBAYWIpz35");
		gxldoc = reader.getGXLDoc();
		gxlgrp = reader.getGXLGraph();
	}

	@Test(expectedExceptions = FileNotFoundException.class)
	public void testReadingFromNotFoundFile() throws IOException, SAXException {
		GXLReader reader = new GXLReader(new File("XigKgrK70H", "W148G6wv4d"), "EBAYWIpz35");
		gxldoc = reader.getGXLDoc();
		gxlgrp = reader.getGXLGraph();
	}
}
