package br.usp.each.opal.dataflow;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.usp.each.opal.dataflow.DFGraph;
import br.usp.each.opal.dataflow.DFGraph.Var;
import br.usp.each.opal.dataflow.ProgramBlock;

public class TestDFGraph {

	private DFGraph dfGraph;

	@BeforeClass
	public void before() {
		dfGraph = new DFGraph("DFGraph", 0);
	}

	@Test
	public void testAddVars() {
		assertTrue(dfGraph.addVar("gjBOimaFCQ", 278204473)); // 0
		assertTrue(dfGraph.addVar("6Ot2zOkhy5", 315531025)); // 1
		assertTrue(dfGraph.addVar("CksFTvHNih", 897157079)); // 2
		assertEquals(dfGraph.varSize(), 3);
	}

	@Test(dependsOnMethods = "testAddVars")
	public void testGetVarByAddOrder() {
		Var var;
		var = dfGraph.getVar(0);
		assertEquals(var.getId(), 278204473);
		assertEquals(var.getName(), "gjBOimaFCQ");

		var = dfGraph.getVar(1);
		assertEquals(var.getId(), 315531025);
		assertEquals(var.getName(), "6Ot2zOkhy5");

		var = dfGraph.getVar(2);
		assertEquals(var.getId(), 897157079);
		assertEquals(var.getName(), "CksFTvHNih");
	}

	@Test(dependsOnMethods = "testAddVars")
	public void testGetVarByName() {
		Var var;
		var = dfGraph.getVarByName("gjBOimaFCQ");
		assertEquals(var.getId(), 278204473);
		assertEquals(var.getName(), "gjBOimaFCQ");

		var = dfGraph.getVarByName("6Ot2zOkhy5");
		assertEquals(var.getId(), 315531025);
		assertEquals(var.getName(), "6Ot2zOkhy5");

		var = dfGraph.getVarByName("CksFTvHNih");
		assertEquals(var.getId(), 897157079);
		assertEquals(var.getName(), "CksFTvHNih");
	}
	
	@Test
	public void testGetVarByInvalidName() {
		Var var;
		var = dfGraph.getVarByName("TAdNyYVO3j");
		assertNull(var);
	}

	@Test(dependsOnMethods = "testAddVars")
	public void testAddDuplicatedVarName() {
		assertFalse(dfGraph.addVar("gjBOimaFCQ", 551639700));
		assertFalse(dfGraph.addVar("6Ot2zOkhy5", 574110148));
	}

	@Test(dependsOnMethods = "testAddVars")
	public void testAddDuplicatedVarID() {
		assertFalse(dfGraph.addVar("75vSZZGpqS", 278204473));
		assertFalse(dfGraph.addVar("JhCK9yIWhh", 315531025));
	}

	@Test
	public void testAddNodes() {
		assertTrue(dfGraph.add(new ProgramBlock(343903852))); // 0
		assertTrue(dfGraph.add(new ProgramBlock(962863897))); // 1
		assertTrue(dfGraph.add(new ProgramBlock(066725263))); // 2
		assertEquals(dfGraph.size(), 3);
	}

	@Test(dependsOnMethods = "testAddNodes")
	public void testGetNodeByAddOrder() {
		ProgramBlock node;
		node = dfGraph.getProgramBlock(0);
		assertEquals(node.getId(), 343903852);

		node = dfGraph.getProgramBlock(1);
		assertEquals(node.getId(), 962863897);

		node = dfGraph.getProgramBlock(2);
		assertEquals(node.getId(), 066725263);
	}

	@Test(dependsOnMethods = "testAddNodes")
	public void testGetNodeById() {
		ProgramBlock node;
		node = dfGraph.getProgramBlockById(343903852);
		assertEquals(node.getId(), 343903852);

		node = dfGraph.getProgramBlockById(962863897);
		assertEquals(node.getId(), 962863897);

		node = dfGraph.getProgramBlockById(066725263);
		assertEquals(node.getId(), 066725263);
	}
	
	@Test
	public void testGetNodeByInvalidId() {
		ProgramBlock node;
		node = dfGraph.getProgramBlockById(635822729);
		assertNull(node);
	}

	@Test(dependsOnMethods = "testAddNodes")
	public void testAddDuplicatedNodeId() {
		assertFalse(dfGraph.add(new ProgramBlock(343903852)));
		assertFalse(dfGraph.add(new ProgramBlock(962863897)));
	}
}
