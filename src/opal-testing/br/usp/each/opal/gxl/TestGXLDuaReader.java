package br.usp.each.opal.gxl;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.requirement.CUse;
import br.usp.each.opal.requirement.Dua;
import br.usp.each.opal.requirement.PUse;

public class TestGXLDuaReader {

	private GXLDuaReader duaReader;
	private DFGraph dfGraph;
	private Dua[] duas;

	@BeforeClass
	public void before() throws IOException, SAXException {
		String dir = "projects/opal-testing/data";
		String defUseFile = "defuse_Sort_sort(int[], int).gxl";
		String duaFile = "dua_Sort_sort(int[], int).gxl";
		String graphId = "sort";
		GXLDefUseReader defUseReader = new GXLDefUseReader(dir, defUseFile,	graphId);
		dfGraph = defUseReader.getDFGraph("Sort", 0);
		duaReader = new GXLDuaReader(dir, duaFile, graphId, dfGraph);
		duas = duaReader.getArrayOfDuas();
	}

	@Test
	public void testIDs() {
		for (int i = 0; i < duas.length; i++) {
			Dua d = duas[i];
			assertEquals(d.getId(), i);
		}
	}
	
	@Test
	public void testCover() {
		for (int i = 0; i < duas.length; i++) {
			Dua d = duas[i];
			assertEquals(d.isCovered(), false);
		}
	}

	@Test
	public void testVerifyCUseDua() {
		Dua d = duas[20];
		assertEquals(d.getId(), 20);
		assertEquals(d.getDef(), 10);
		assertEquals(d.getUse().getUseNode(), 51);
		assertEquals(d.getVariable(), dfGraph.getVarByName("L@5").getId());
		CUse cUse = d.getUse().CUse();
		assertEquals(cUse.getUseNode(), 51);
	}

	@Test
	public void testVerifyPUseDua() {
		Dua d = duas[23];
		assertEquals(d.getId(), 23);
		assertEquals(d.getDef(), 0);
		assertEquals(d.getUse().getUseNode(), 51);
		assertEquals(d.getVariable(), dfGraph.getVarByName("L@2").getId());
		PUse pUse = d.getUse().PUse();
		assertEquals(pUse.getOriginNode(), 20);
		assertEquals(pUse.getDestNode(), 51);
	}
}
